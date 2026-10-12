package cl.edusaldo.ventas.model;

import jakarta.persistence.*;

@Entity
@Table(name = "detalles_reserva")
public class DetalleReserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reserva_id", nullable = false)
    private Reserva reserva;

    @Column(name = "producto_id", nullable = false)
    private Long productoId; // Ref escalar a MS Productos

    @Column(name = "nombre_producto", nullable = false, length = 150)
    private String nombreProducto;

    @Column(name = "cantidad", nullable = false)
    private int cantidad;

    @Column(name = "cantidad_preparada", nullable = false)
    private int cantidadPreparada = 0;

    @Column(name = "precio_unitario", nullable = false)
    private Long precioUnitario;

    public DetalleReserva() {}

    public DetalleReserva(Reserva reserva, Long productoId, String nombreProducto, int cantidad, Long precioUnitario) {
        this.reserva = reserva;
        this.productoId = productoId;
        this.nombreProducto = nombreProducto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.cantidadPreparada = 0;
    }

    public Long getSubtotal() {
        return (long) this.cantidad * this.precioUnitario;
    }

    public Long getId() { return id; }
    public Reserva getReserva() { return reserva; }
    public Long getProductoId() { return productoId; }
    public String getNombreProducto() { return nombreProducto; }
    public int getCantidad() { return cantidad; }
    public int getCantidadPreparada() { return cantidadPreparada; }
    public Long getPrecioUnitario() { return precioUnitario; }
}
