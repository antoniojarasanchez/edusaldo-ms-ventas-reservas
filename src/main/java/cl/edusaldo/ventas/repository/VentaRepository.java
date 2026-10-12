package cl.edusaldo.ventas.repository;

import cl.edusaldo.ventas.model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {
    Optional<Venta> findByNumero(String numero);
    List<Venta> findByAlumnoId(Long alumnoId);
}
