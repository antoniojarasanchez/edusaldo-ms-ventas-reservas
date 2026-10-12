package cl.edusaldo.ventas.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "reservas")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo", nullable = false, unique = true, length = 50)
    private String codigo;

    @Column(name = "codigo_bolsa", length = 50)
    private String codigoBolsa;

    @Column(name = "alumno_id", nullable = false)
    private Long alumnoId; // Ref escalar a MS Alumnos

    @Column(name = "apoderado_id", nullable = false)
    private Long apoderadoId; // Ref escalar a MS Auth

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    @Column(name = "total", nullable = false)
    private Long total;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 30)
    private EstadoReserva estado = EstadoReserva.PENDIENTE;

    @Column(name = "fecha_inicio_preparacion")
    private LocalDateTime fechaInicioPreparacion;

    @Column(name = "fecha_lista_retiro")
    private LocalDateTime fechaListaRetiro;

    @Column(name = "preparada_por_id")
    private Long preparadaPorId;

    @Column(name = "fecha_entrega")
    private LocalDateTime fechaEntrega;

    @Column(name = "entregada_por_id")
    private Long entregadaPorId;

    @Column(name = "fecha_cancelacion")
    private LocalDateTime fechaCancelacion;

    @Column(name = "cancelada_por_id")
    private Long canceladaPorId;

    @Column(name = "motivo_cancelacion")
    private String motivoCancelacion;

    @OneToMany(mappedBy = "reserva", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleReserva> detalles = new ArrayList<>();

    public Reserva() {}

    public Reserva(String codigo, Long alumnoId, Long apoderadoId, Long total) {
        this.codigo = codigo;
        this.alumnoId = alumnoId;
        this.apoderadoId = apoderadoId;
        this.total = total;
        this.estado = EstadoReserva.PENDIENTE;
        this.fechaCreacion = LocalDateTime.now();
    }

    // Ciclo multietapa de preparación (libreria.js)
    public void iniciarPreparacion(Long encargadoId) {
        this.estado = EstadoReserva.EN_PREPARACION;
        this.preparadaPorId = encargadoId;
        this.fechaInicioPreparacion = LocalDateTime.now();
    }

    public String generarEtiquetaBolsa() {
        this.codigoBolsa = "BOL-" + (1000 + (int)(Math.random() * 9000));
        this.estado = EstadoReserva.ETIQUETA_PENDIENTE;
        return this.codigoBolsa;
    }

    public void marcarListaParaRetiro(Long encargadoId) {
        this.estado = EstadoReserva.LISTA_PARA_RETIRO;
        this.preparadaPorId = encargadoId;
        this.fechaListaRetiro = LocalDateTime.now();
    }

    public void cancelar(Long encargadoId, String motivo) {
        if (this.estado == EstadoReserva.ENTREGADA) {
            throw new IllegalStateException("No se puede cancelar una reserva ya entregada.");
        }
        this.estado = EstadoReserva.CANCELADA;
        this.canceladaPorId = encargadoId;
        this.motivoCancelacion = motivo;
        this.fechaCancelacion = LocalDateTime.now();
    }

    public Venta entregar(Long encargadoId) {
        if (this.estado != EstadoReserva.LISTA_PARA_RETIRO && this.estado != EstadoReserva.PENDIENTE) {
            throw new IllegalStateException("La reserva no está lista para entrega.");
        }
        this.estado = EstadoReserva.ENTREGADA;
        this.entregadaPorId = encargadoId;
        this.fechaEntrega = LocalDateTime.now();

        Venta v = new Venta("VTA-" + System.currentTimeMillis(), this.id, this.alumnoId, OrigenVenta.RESERVA, this.total, encargadoId);
        for (DetalleReserva det : this.detalles) {
            v.agregarDetalle(det.getProductoId(), det.getNombreProducto(), det.getCantidad(), det.getPrecioUnitario());
        }
        return v;
    }

    public void agregarDetalle(Long productoId, String nombreProducto, int cantidad, Long precioUnitario) {
        this.detalles.add(new DetalleReserva(this, productoId, nombreProducto, cantidad, precioUnitario));
    }

    public Long getId() { return id; }
    public String getCodigo() { return codigo; }
    public String getCodigoBolsa() { return codigoBolsa; }
    public Long getAlumnoId() { return alumnoId; }
    public Long getApoderadoId() { return apoderadoId; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public Long getTotal() { return total; }
    public EstadoReserva getEstado() { return estado; }
    public List<DetalleReserva> getDetalles() { return detalles; }
    public LocalDateTime getFechaEntrega() { return fechaEntrega; }
    public Long getEntregadaPorId() { return entregadaPorId; }
}
