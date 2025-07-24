package br.com.foursales.service;

import br.com.foursales.dto.ProductRequestDTO;
import br.com.foursales.dto.ProductResponseDTO;

import java.util.List;
import java.util.UUID;

public interface ProductService {
    ProductResponseDTO create(ProductRequestDTO dto);
    ProductResponseDTO findById(UUID id);
    List<ProductResponseDTO> findAll();
    ProductResponseDTO update(UUID id, ProductRequestDTO dto);
    void delete(UUID id);
}