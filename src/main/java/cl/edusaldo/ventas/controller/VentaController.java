package cl.edusaldo.ventas.controller;

import cl.edusaldo.ventas.dto.VentaDirectaDTO;
import cl.edusaldo.ventas.dto.VentaResponseDTO;
import cl.edusaldo.ventas.model.Venta;
import cl.edusaldo.ventas.service.VentaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ventas")
@Tag(name = "Ventas", description = "Ventas presenciales directas y consultas de compras")
public class VentaController {

    private final VentaService ventaService;

    public VentaController(VentaService ventaService) {
        this.ventaService = ventaService;
    }

    @PostMapping("/directa")
    @Operation(summary = "Registrar venta presencial directa a alumno >= 12 años (HU34)")
    public ResponseEntity<VentaResponseDTO> ventaDirecta(@Valid @RequestBody VentaDirectaDTO dto) {
        Venta v = ventaService.registrarVentaDirecta(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new VentaResponseDTO(v));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar venta por ID")
    public ResponseEntity<VentaResponseDTO> obtenerPorId(@PathVariable Long id) {
        Venta v = ventaService.obtenerPorId(id);
        return ResponseEntity.ok(new VentaResponseDTO(v));
    }

    @GetMapping("/alumno/{alumnoId}")
    @Operation(summary = "Listar ventas de un alumno")
    public ResponseEntity<List<VentaResponseDTO>> listarPorAlumno(@PathVariable Long alumnoId) {
        List<VentaResponseDTO> list = ventaService.listarPorAlumno(alumnoId).stream()
                .map(VentaResponseDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }
}
