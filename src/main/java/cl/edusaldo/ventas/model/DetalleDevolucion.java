package cl.edusaldo.ventas.model;

import jakarta.persistence.*;

@Entity
@Table(name = "detalles_devolucion")
public class DetalleDevolucion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "devolucion_id", nullable = false)
    private Devolucion devolucion;

    @Column(name = "detalle_venta_id", nullable = false)
    private Long detalleVentaId;

    @Column(name = "producto_id", nullable = false)
    private Long productoId; // Ref escalar a MS Productos

    @Column(name = "nombre_producto", nullable = false, length = 150)
    private String nombreProducto;

    @Column(name = "cantidad", nullable = false)
    private int cantidad;

    @Column(name = "precio_unitario_reintegro", nullable = false)
    private Long precioUnitarioReintegro;

    @Column(name = "monto_reintegro", nullable = false)
    private Long montoReintegro;

    public DetalleDevolucion() {}

    public DetalleDevolucion(Devolucion devolucion, Long detalleVentaId, Long productoId, String nombreProducto, int cantidad, Long precioUnitarioReintegro) {
        this.devolucion = devolucion;
        this.detalleVentaId = detalleVentaId;
        this.productoId = productoId;
        this.nombreProducto = nombreProducto;
        this.cantidad = cantidad;
        this.precioUnitarioReintegro = precioUnitarioReintegro;
        this.montoReintegro = (long) cantidad * precioUnitarioReintegro;
    }

    public Long getId() { return id; }
    public Devolucion getDevolucion() { return devolucion; }
    public Long getDetalleVentaId() { return detalleVentaId; }
    public Long getProductoId() { return productoId; }
    public String getNombreProducto() { return nombreProducto; }
    public int getCantidad() { return cantidad; }
    public Long getPrecioUnitarioReintegro() { return precioUnitarioReintegro; }
    public Long getMontoReintegro() { return montoReintegro; }
}
