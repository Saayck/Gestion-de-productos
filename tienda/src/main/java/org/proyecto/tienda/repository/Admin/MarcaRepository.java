package org.proyecto.tienda.repository.Admin;

import java.util.Optional;

import org.proyecto.tienda.entity.Admin.Marca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarcaRepository extends JpaRepository<Marca, Integer> {
    Optional<Marca> findByNombre(String nombre);
}
