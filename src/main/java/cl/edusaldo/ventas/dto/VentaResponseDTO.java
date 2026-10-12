package cl.edusaldo.ventas.dto;

import cl.edusaldo.ventas.model.OrigenVenta;
import cl.edusaldo.ventas.model.Venta;

import java.time.LocalDateTime;

public class VentaResponseDTO {
    private Long id;
    private String numero;
    private Long reservaId;
    private Long alumnoId;
    private OrigenVenta origen;
    private Long total;
    private LocalDateTime fecha;

    public VentaResponseDTO() {}

    public VentaResponseDTO(Venta v) {
        this.id = v.getId();
        this.numero = v.getNumero();
        this.reservaId = v.getReservaId();
        this.alumnoId = v.getAlumnoId();
        this.origen = v.getOrigen();
        this.total = v.getTotal();
        this.fecha = v.getFecha();
    }

    public Long getId() { return id; }
    public String getNumero() { return numero; }
    public Long getReservaId() { return reservaId; }
    public Long getAlumnoId() { return alumnoId; }
    public OrigenVenta getOrigen() { return origen; }
    public Long getTotal() { return total; }
    public LocalDateTime getFecha() { return fecha; }
}
