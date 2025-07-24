package br.com.foursales.mapper;

import br.com.foursales.domain.Product;
import br.com.foursales.dto.ProductRequestDTO;
import br.com.foursales.dto.ProductResponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    Product toEntity(ProductRequestDTO dto);
    ProductResponseDTO toDTO(Product product);
}