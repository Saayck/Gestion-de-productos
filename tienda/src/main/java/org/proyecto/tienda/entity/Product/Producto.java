package org.proyecto.tienda.entity.Product;

import org.proyecto.tienda.entity.Admin.Categoria;

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
@Table(name = "producto")
@Entity
public class Producto {
    @Id
    Integer id;
    String nombre;
    String descripcion;
    Double precio;
    Integer stock;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    Categoria categoria;
}
