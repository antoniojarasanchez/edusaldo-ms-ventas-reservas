package cl.edusaldo.ventas.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ventas")
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero", nullable = false, unique = true, length = 50)
    private String numero;

    @Column(name = "reserva_id")
    private Long reservaId;

    @Column(name = "alumno_id", nullable = false)
    private Long alumnoId; // Ref escalar a MS Alumnos

    @Enumerated(EnumType.STRING)
    @Column(name = "origen", nullable = false, length = 20)
    private OrigenVenta origen;

    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha = LocalDateTime.now();

    @Column(name = "total", nullable = false)
    private Long total;

    @Column(name = "encargado_id", nullable = false)
    private Long encargadoId; // Ref escalar a MS Auth

    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleVenta> detalles = new ArrayList<>();

    public Venta() {}

    public Venta(String numero, Long reservaId, Long alumnoId, OrigenVenta origen, Long total, Long encargadoId) {
        this.numero = numero;
        this.reservaId = reservaId;
        this.alumnoId = alumnoId;
        this.origen = origen;
        this.total = total;
        this.encargadoId = encargadoId;
        this.fecha = LocalDateTime.now();
    }

    public void agregarDetalle(Long productoId, String nombreProducto, int cantidad, Long precioUnitario) {
        this.detalles.add(new DetalleVenta(this, productoId, nombreProducto, cantidad, precioUnitario));
    }

    public Long getId() { return id; }
    public String getNumero() { return numero; }
    public Long getReservaId() { return reservaId; }
    public Long getAlumnoId() { return alumnoId; }
    public OrigenVenta getOrigen() { return origen; }
    public LocalDateTime getFecha() { return fecha; }
    public Long getTotal() { return total; }
    public Long getEncargadoId() { return encargadoId; }
    public List<DetalleVenta> getDetalles() { return detalles; }
}
