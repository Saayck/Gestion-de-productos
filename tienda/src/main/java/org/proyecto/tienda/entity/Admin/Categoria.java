package org.proyecto.tienda.entity.Admin;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "categoria")
@Entity
public class Categoria {
    @Id
    Integer id;
    
    @Column(nullable = false, unique = true)
    String nombre;

    @Column(nullable = false)
    String descripcion;

    @ManyToOne
    @JoinColumn(name = "marca_id")
    Marca marca;
}
