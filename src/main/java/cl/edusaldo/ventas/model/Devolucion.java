package cl.edusaldo.ventas.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "devoluciones")
public class Devolucion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo_devolucion", nullable = false, unique = true, length = 50)
    private String codigoDevolucion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venta_id", nullable = false)
    private Venta venta;

    @Column(name = "alumno_id", nullable = false)
    private Long alumnoId; // Ref escalar a MS Alumnos

    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha = LocalDateTime.now();

    @Column(name = "motivo", nullable = false)
    private String motivo;

    @Column(name = "total_reintegro", nullable = false)
    private Long totalReintegro;

    @Column(name = "encargado_id", nullable = false)
    private Long encargadoId;

    @OneToMany(mappedBy = "devolucion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleDevolucion> detalles = new ArrayList<>();

    public Devolucion() {}

    public Devolucion(String codigoDevolucion, Venta venta, Long alumnoId, String motivo, Long totalReintegro, Long encargadoId) {
        this.codigoDevolucion = codigoDevolucion;
        this.venta = venta;
        this.alumnoId = alumnoId;
        this.motivo = motivo;
        this.totalReintegro = totalReintegro;
        this.encargadoId = encargadoId;
        this.fecha = LocalDateTime.now();
    }

    public void agregarDetalle(Long detalleVentaId, Long productoId, String nombreProducto, int cantidad, Long precioUnitarioReintegro) {
        this.detalles.add(new DetalleDevolucion(this, detalleVentaId, productoId, nombreProducto, cantidad, precioUnitarioReintegro));
    }

    public Long getId() { return id; }
    public String getCodigoDevolucion() { return codigoDevolucion; }
    public Venta getVenta() { return venta; }
    public Long getAlumnoId() { return alumnoId; }
    public LocalDateTime getFecha() { return fecha; }
    public String getMotivo() { return motivo; }
    public Long getTotalReintegro() { return totalReintegro; }
    public Long getEncargadoId() { return encargadoId; }
    public List<DetalleDevolucion> getDetalles() { return detalles; }
}
