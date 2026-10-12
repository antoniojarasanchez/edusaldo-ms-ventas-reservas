package cl.edusaldo.ventas.dto;

import cl.edusaldo.ventas.model.EstadoReserva;
import cl.edusaldo.ventas.model.Reserva;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ReservaResponseDTO {
    private Long id;
    private String codigo;
    private String codigoBolsa;
    private Long alumnoId;
    private Long apoderadoId;
    private Long total;
    private EstadoReserva estado;
    private LocalDateTime fechaCreacion;
    private List<ItemReservaResponseDTO> items;

    public static class ItemReservaResponseDTO {
        private Long productoId;
        private String nombreProducto;
        private int cantidad;
        private Long precioUnitario;
        private Long subtotal;

        public ItemReservaResponseDTO(Long productoId, String nombreProducto, int cantidad, Long precioUnitario) {
            this.productoId = productoId;
            this.nombreProducto = nombreProducto;
            this.cantidad = cantidad;
            this.precioUnitario = precioUnitario;
            this.subtotal = (long) cantidad * precioUnitario;
        }

        public Long getProductoId() { return productoId; }
        public String getNombreProducto() { return nombreProducto; }
        public int getCantidad() { return cantidad; }
        public Long getPrecioUnitario() { return precioUnitario; }
        public Long getSubtotal() { return subtotal; }
    }

    public ReservaResponseDTO() {}

    public ReservaResponseDTO(Reserva r) {
        this.id = r.getId();
        this.codigo = r.getCodigo();
        this.codigoBolsa = r.getCodigoBolsa();
        this.alumnoId = r.getAlumnoId();
        this.apoderadoId = r.getApoderadoId();
        this.total = r.getTotal();
        this.estado = r.getEstado();
        this.fechaCreacion = r.getFechaCreacion();
        if (r.getDetalles() != null) {
            this.items = r.getDetalles().stream()
                    .map(d -> new ItemReservaResponseDTO(d.getProductoId(), d.getNombreProducto(), d.getCantidad(), d.getPrecioUnitario()))
                    .collect(Collectors.toList());
        }
    }

    public Long getId() { return id; }
    public String getCodigo() { return codigo; }
    public String getCodigoBolsa() { return codigoBolsa; }
    public Long getAlumnoId() { return alumnoId; }
    public Long getApoderadoId() { return apoderadoId; }
    public Long getTotal() { return total; }
    public EstadoReserva getEstado() { return estado; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public List<ItemReservaResponseDTO> getItems() { return items; }
}
