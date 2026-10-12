package cl.edusaldo.ventas.service;

import cl.edusaldo.ventas.dto.CrearReservaDTO;
import cl.edusaldo.ventas.dto.ItemReservaDTO;
import cl.edusaldo.ventas.model.EstadoReserva;
import cl.edusaldo.ventas.model.Reserva;
import cl.edusaldo.ventas.model.Venta;
import cl.edusaldo.ventas.repository.ReservaRepository;
import cl.edusaldo.ventas.repository.VentaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final VentaRepository ventaRepository;

    public ReservaService(ReservaRepository reservaRepository, VentaRepository ventaRepository) {
        this.reservaRepository = reservaRepository;
        this.ventaRepository = ventaRepository;
    }

    @Transactional
    public Reserva crearReserva(CrearReservaDTO dto) {
        long total = dto.getItems().stream()
                .mapToLong(i -> (long) i.getCantidad() * i.getPrecioUnitario())
                .sum();

        String codigo = "RES-" + (1000 + (int)(Math.random() * 9000));
        Reserva r = new Reserva(codigo, dto.getAlumnoId(), dto.getApoderadoId(), total);

        for (ItemReservaDTO item : dto.getItems()) {
            r.agregarDetalle(item.getProductoId(), item.getNombreProducto(), item.getCantidad(), item.getPrecioUnitario());
        }

        return reservaRepository.save(r);
    }

    @Transactional
    public Reserva iniciarPreparacion(Long reservaId, Long encargadoId) {
        Reserva r = obtenerPorId(reservaId);
        r.iniciarPreparacion(encargadoId);
        return reservaRepository.save(r);
    }

    @Transactional
    public String etiquetarBolsa(Long reservaId) {
        Reserva r = obtenerPorId(reservaId);
        String codigoBolsa = r.generarEtiquetaBolsa();
        reservaRepository.save(r);
        return codigoBolsa;
    }

    @Transactional
    public Reserva marcarListaParaRetiro(Long reservaId, Long encargadoId) {
        Reserva r = obtenerPorId(reservaId);
        r.marcarListaParaRetiro(encargadoId);
        return reservaRepository.save(r);
    }

    @Transactional
    public Venta entregarReserva(Long reservaId, Long encargadoId) {
        Reserva r = obtenerPorId(reservaId);
        Venta v = r.entregar(encargadoId);
        reservaRepository.save(r);
        return ventaRepository.save(v);
    }

    @Transactional
    public Reserva cancelarReserva(Long reservaId, Long encargadoId, String motivo) {
        Reserva r = obtenerPorId(reservaId);
        r.cancelar(encargadoId, motivo);
        return reservaRepository.save(r);
    }

    @Transactional(readOnly = true)
    public Reserva obtenerPorId(Long id) {
        return reservaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reserva no encontrada con ID: " + id));
    }

    @Transactional(readOnly = true)
    public List<Reserva> listarPorAlumno(Long alumnoId) {
        return reservaRepository.findByAlumnoId(alumnoId);
    }

    @Transactional(readOnly = true)
    public List<Reserva> listarTodas() {
        return reservaRepository.findAll();
    }
}
