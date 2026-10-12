package cl.edusaldo.ventas.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public class CrearReservaDTO {

    @NotNull(message = "El ID del alumno es obligatorio")
    private Long alumnoId;

    @NotNull(message = "El ID del apoderado es obligatorio")
    private Long apoderadoId;

    @NotEmpty(message = "La reserva debe contener al menos un material")
    @Valid
    private List<ItemReservaDTO> items;

    public CrearReservaDTO() {}

    public Long getAlumnoId() { return alumnoId; }
    public void setAlumnoId(Long alumnoId) { this.alumnoId = alumnoId; }
    public Long getApoderadoId() { return apoderadoId; }
    public void setApoderadoId(Long apoderadoId) { this.apoderadoId = apoderadoId; }
    public List<ItemReservaDTO> getItems() { return items; }
    public void setItems(List<ItemReservaDTO> items) { this.items = items; }
}
