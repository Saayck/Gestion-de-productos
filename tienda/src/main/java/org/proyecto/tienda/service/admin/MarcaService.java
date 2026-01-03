package org.proyecto.tienda.service.admin;

import java.util.List;

import org.proyecto.tienda.entity.Admin.Marca;
import org.proyecto.tienda.model.Create.Marca.MarcaCreateDTO;
import org.proyecto.tienda.model.update.MarcaUpdateDTO;
import org.proyecto.tienda.repository.Admin.MarcaRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MarcaService {
    private final MarcaRepository marcaRepository;

    //Listar todos
    public List<Marca> getAllMarcas() {
        return marcaRepository.findAll();
    }

    //Crear nueva marca
    public Marca createMarca(MarcaCreateDTO marcaDTO){
        Marca marca = new Marca();
        marca.setNombre(marcaDTO.nombre());
        marca.setDescripcion(marcaDTO.descripcion());
        return marcaRepository.save(marca);
    }

    //Actualizar marca
    public Marca updateMarca(Integer id, MarcaUpdateDTO marcaDTO){
        Marca marca = marcaRepository.findById(id).orElseThrow();
        marca.setNombre(marcaDTO.nombre());
        marca.setDescripcion(marcaDTO.descripcion());
        return marcaRepository.save(marca);
    }

    //Eliminar marca
    public void deleteMarca(Integer id){
        Marca marca = marcaRepository.findById(id).orElseThrow();
        marcaRepository.delete(marca);
    }

    //Obtener marca por ID
    public Marca getMarcaById(Integer id){
        return marcaRepository.findById(id).orElseThrow();
    }
}
