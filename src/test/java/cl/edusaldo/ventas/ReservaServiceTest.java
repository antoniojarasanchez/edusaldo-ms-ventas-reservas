package cl.edusaldo.ventas;

import cl.edusaldo.ventas.dto.CrearReservaDTO;
import cl.edusaldo.ventas.dto.ItemReservaDTO;
import cl.edusaldo.ventas.model.EstadoReserva;
import cl.edusaldo.ventas.model.OrigenVenta;
import cl.edusaldo.ventas.model.Reserva;
import cl.edusaldo.ventas.model.Venta;
import cl.edusaldo.ventas.repository.ReservaRepository;
import cl.edusaldo.ventas.repository.VentaRepository;
import cl.edusaldo.ventas.service.ReservaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservaServiceTest {

    @Mock
    private ReservaRepository reservaRepository;

    @Mock
    private VentaRepository ventaRepository;

    @InjectMocks
    private ReservaService reservaService;

    private Reserva reservaPrueba;

    @BeforeEach
    void setUp() {
        reservaPrueba = new Reserva("RES-1001", 1L, 4L, 2500L);
        reservaPrueba.setId(101L);
        reservaPrueba.agregarDetalle(101L, "Cuaderno universitario", 2, 1250L);
    }

    @Test
    @DisplayName("1. Ciclo multietapa de preparación de pedidos y rotulado de bolsa (HU31)")
    void testCicloMultietapaPreparacionReserva() {
        when(reservaRepository.findById(101L)).thenReturn(Optional.of(reservaPrueba));
        when(reservaRepository.save(any(Reserva.class))).thenAnswer(i -> i.getArgument(0));

        // Inicial: PENDIENTE
        assertEquals(EstadoReserva.PENDIENTE, reservaPrueba.getEstado());
        assertNull(reservaPrueba.getCodigoBolsa());

        // Paso 1: Iniciar preparación -> EN_PREPARACION
        reservaService.iniciarPreparacion(101L, 2L);
        assertEquals(EstadoReserva.EN_PREPARACION, reservaPrueba.getEstado());

        // Paso 2: Generar etiqueta de bolsa con código de barra -> ETIQUETA_PENDIENTE
        String bolsa = reservaService.etiquetarBolsa(101L);
        assertNotNull(bolsa);
        assertTrue(bolsa.startsWith("BOL-"));
        assertEquals(EstadoReserva.ETIQUETA_PENDIENTE, reservaPrueba.getEstado());
        assertEquals(bolsa, reservaPrueba.getCodigoBolsa());

        // Paso 3: Marcar lista para retiro en estante -> LISTA_PARA_RETIRO
        reservaService.marcarListaParaRetiro(101L, 2L);
        assertEquals(EstadoReserva.LISTA_PARA_RETIRO, reservaPrueba.getEstado());
    }

    @Test
    @DisplayName("2. Entrega en mesón convierte reserva en venta definitiva (HU33)")
    void testEntregaReservaGeneraVentaDefinitiva() {
        reservaPrueba.marcarListaParaRetiro(2L);

        when(reservaRepository.findById(101L)).thenReturn(Optional.of(reservaPrueba));
        when(reservaRepository.save(any(Reserva.class))).thenAnswer(i -> i.getArgument(0));
        when(ventaRepository.save(any(Venta.class))).thenAnswer(i -> i.getArgument(0));

        Venta ventaResultante = reservaService.entregarReserva(101L, 2L);

        assertNotNull(ventaResultante);
        assertEquals(OrigenVenta.RESERVA, ventaResultante.getOrigen());
        assertEquals(2500L, ventaResultante.getTotal());
        assertEquals(101L, ventaResultante.getReservaId());
        assertEquals(1L, ventaResultante.getAlumnoId());
        assertEquals(EstadoReserva.ENTREGADA, reservaPrueba.getEstado());
    }

    @Test
    @DisplayName("3. Cancelación de reserva previene entregas posteriores (Invariante de entrega)")
    void testCancelarReservaNoPermiteEntrega() {
        when(reservaRepository.findById(101L)).thenReturn(Optional.of(reservaPrueba));
        when(reservaRepository.save(any(Reserva.class))).thenAnswer(i -> i.getArgument(0));

        reservaService.cancelarReserva(101L, 2L, "Apoderado desiste de la compra");
        assertEquals(EstadoReserva.CANCELADA, reservaPrueba.getEstado());

        // Intentar entregar una reserva cancelada debe arrojar excepción de negocio
        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> reservaService.entregarReserva(101L, 2L));
        assertTrue(ex.getMessage().contains("no está lista para entrega"));
    }

    @Test
    @DisplayName("4. Reserva ya entregada no puede ser cancelada (Invariante de inmutabilidad)")
    void testReservaEntregadaNoPuedeCancelarse() {
        reservaPrueba.marcarListaParaRetiro(2L);
        reservaPrueba.entregar(2L);
        assertEquals(EstadoReserva.ENTREGADA, reservaPrueba.getEstado());

        when(reservaRepository.findById(101L)).thenReturn(Optional.of(reservaPrueba));

        IllegalStateException ex = assertThrows(IllegalStateException.class, () ->
                reservaService.cancelarReserva(101L, 2L, "Intento de cancelación indebida")
        );
        assertTrue(ex.getMessage().contains("No se puede cancelar una reserva ya entregada"));
    }
}
