package cl.edusaldo.ventas.controller;

import cl.edusaldo.ventas.dto.CrearReservaDTO;
import cl.edusaldo.ventas.dto.ReservaResponseDTO;
import cl.edusaldo.ventas.dto.VentaResponseDTO;
import cl.edusaldo.ventas.model.Reserva;
import cl.edusaldo.ventas.model.Venta;
import cl.edusaldo.ventas.service.ReservaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reservas")
@Tag(name = "Reservas", description = "Ciclo multietapa de pedidos y preparación de útiles escolares")
public class ReservaController {

    private final ReservaService reservaService;

    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @PostMapping
    @Operation(summary = "Crear nueva reserva de materiales")
    public ResponseEntity<ReservaResponseDTO> crear(@Valid @RequestBody CrearReservaDTO dto) {
        Reserva r = reservaService.crearReserva(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ReservaResponseDTO(r));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar reserva por ID")
    public ResponseEntity<ReservaResponseDTO> obtenerPorId(@PathVariable Long id) {
        Reserva r = reservaService.obtenerPorId(id);
        return ResponseEntity.ok(new ReservaResponseDTO(r));
    }

    @GetMapping("/alumno/{alumnoId}")
    @Operation(summary = "Listar reservas de un alumno")
    public ResponseEntity<List<ReservaResponseDTO>> listarPorAlumno(@PathVariable Long alumnoId) {
        List<ReservaResponseDTO> list = reservaService.listarPorAlumno(alumnoId).stream()
                .map(ReservaResponseDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @PutMapping("/{id}/iniciar-preparacion")
    @Operation(summary = "Iniciar empaque en librería (EN_PREPARACION)")
    public ResponseEntity<ReservaResponseDTO> iniciarPreparacion(
            @PathVariable Long id,
            @RequestParam Long encargadoId) {
        Reserva r = reservaService.iniciarPreparacion(id, encargadoId);
        return ResponseEntity.ok(new ReservaResponseDTO(r));
    }

    @PutMapping("/{id}/etiquetar")
    @Operation(summary = "Generar etiqueta de bolsa con código de barras (ETIQUETA_PENDIENTE)")
    public ResponseEntity<Map<String, String>> etiquetarBolsa(@PathVariable Long id) {
        String codigoBolsa = reservaService.etiquetarBolsa(id);
        return ResponseEntity.ok(Map.of("codigoBolsa", codigoBolsa));
    }

    @PutMapping("/{id}/marcar-lista")
    @Operation(summary = "Marcar reserva rotulada lista para retiro (LISTA_PARA_RETIRO)")
    public ResponseEntity<ReservaResponseDTO> marcarLista(
            @PathVariable Long id,
            @RequestParam Long encargadoId) {
        Reserva r = reservaService.marcarListaParaRetiro(id, encargadoId);
        return ResponseEntity.ok(new ReservaResponseDTO(r));
    }

    @PostMapping("/{id}/entregar")
    @Operation(summary = "Entregar reserva en mesón y convertir en venta definitiva")
    public ResponseEntity<VentaResponseDTO> entregar(
            @PathVariable Long id,
            @RequestParam Long encargadoId) {
        Venta v = reservaService.entregarReserva(id, encargadoId);
        return ResponseEntity.ok(new VentaResponseDTO(v));
    }

    @PutMapping("/{id}/cancelar")
    @Operation(summary = "Cancelar reserva antes de entrega")
    public ResponseEntity<ReservaResponseDTO> cancelar(
            @PathVariable Long id,
            @RequestParam Long encargadoId,
            @RequestParam String motivo) {
        Reserva r = reservaService.cancelarReserva(id, encargadoId, motivo);
        return ResponseEntity.ok(new ReservaResponseDTO(r));
    }
}
