package org.proyecto.tienda.entity.Client;

import org.proyecto.tienda.model.Enum.Access.Role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usuario", uniqueConstraints = { @UniqueConstraint(columnNames = { "email" }) })
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usuario_seq")
    @SequenceGenerator(name = "usuario_seq", sequenceName = "usuario_seq", allocationSize = 1)
    Integer id;
    @Column(nullable = false)
    @NotNull
    String nombres;
    @Column(nullable = false)
    @NotNull
    String apellidos;
    @Column(nullable = false, unique = true)
    @NotNull
    String email;
    @Column(name = "password", nullable = false)
    @NotNull
    String password;
    
    @Enumerated(EnumType.STRING)
    Role role;
}
