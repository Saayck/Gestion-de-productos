package org.proyecto.tienda.entity.Admin;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "marca")
@Entity
public class Marca {
   @Id
    Integer id;
    
    @Column(nullable = false, unique = true)
    String nombre;
    @Column(nullable = false)
    String descripcion;
}
