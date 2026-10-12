package cl.edusaldo.ventas.service;

import cl.edusaldo.ventas.dto.ItemVentaDTO;
import cl.edusaldo.ventas.dto.VentaDirectaDTO;
import cl.edusaldo.ventas.model.OrigenVenta;
import cl.edusaldo.ventas.model.Venta;
import cl.edusaldo.ventas.repository.VentaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class VentaService {

    private final VentaRepository ventaRepository;

    public VentaService(VentaRepository ventaRepository) {
        this.ventaRepository = ventaRepository;
    }

    @Transactional
    public Venta registrarVentaDirecta(VentaDirectaDTO dto) {
        long total = dto.getItems().stream()
                .mapToLong(i -> (long) i.getCantidad() * i.getPrecioUnitario())
                .sum();

        String numero = "VTA-DIR-" + System.currentTimeMillis();
        Venta v = new Venta(numero, null, dto.getAlumnoId(), OrigenVenta.DIRECTA, total, dto.getEncargadoId());

        for (ItemVentaDTO item : dto.getItems()) {
            v.agregarDetalle(item.getProductoId(), item.getNombreProducto(), item.getCantidad(), item.getPrecioUnitario());
        }

        return ventaRepository.save(v);
    }

    @Transactional(readOnly = true)
    public Venta obtenerPorId(Long id) {
        return ventaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Venta no encontrada con ID: " + id));
    }

    @Transactional(readOnly = true)
    public List<Venta> listarPorAlumno(Long alumnoId) {
        return ventaRepository.findByAlumnoId(alumnoId);
    }
}
