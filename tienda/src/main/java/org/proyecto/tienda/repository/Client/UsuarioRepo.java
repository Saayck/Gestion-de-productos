package org.proyecto.tienda.repository.Client;


import java.util.Optional;

import org.proyecto.tienda.entity.Client.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepo extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByEmail(String email);
}
