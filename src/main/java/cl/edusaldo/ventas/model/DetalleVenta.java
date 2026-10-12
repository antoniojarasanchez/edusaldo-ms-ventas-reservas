package cl.edusaldo.ventas.model;

import jakarta.persistence.*;

@Entity
@Table(name = "detalles_venta")
public class DetalleVenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venta_id", nullable = false)
    private Venta venta;

    @Column(name = "producto_id", nullable = false)
    private Long productoId; // Ref escalar a MS Productos

    @Column(name = "nombre_producto", nullable = false, length = 150)
    private String nombreProducto;

    @Column(name = "cantidad", nullable = false)
    private int cantidad;

    @Column(name = "precio_unitario", nullable = false)
    private Long precioUnitario;

    @Column(name = "cantidad_devuelta", nullable = false)
    private int cantidadDevuelta = 0;

    public DetalleVenta() {}

    public DetalleVenta(Venta venta, Long productoId, String nombreProducto, int cantidad, Long precioUnitario) {
        this.venta = venta;
        this.productoId = productoId;
        this.nombreProducto = nombreProducto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.cantidadDevuelta = 0;
    }

    public int getCantidadDevolvible() {
        return this.cantidad - this.cantidadDevuelta;
    }

    public void registrarDevolucion(int cant) {
        if (cant > getCantidadDevolvible()) {
            throw new IllegalStateException("No se pueden devolver más unidades que las disponibles.");
        }
        this.cantidadDevuelta += cant;
    }

    public Long getId() { return id; }
    public Venta getVenta() { return venta; }
    public Long getProductoId() { return productoId; }
    public String getNombreProducto() { return nombreProducto; }
    public int getCantidad() { return cantidad; }
    public Long getPrecioUnitario() { return precioUnitario; }
    public int getCantidadDevuelta() { return cantidadDevuelta; }
}
