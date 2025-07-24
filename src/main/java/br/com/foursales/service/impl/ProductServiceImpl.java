package br.com.foursales.service.impl;

import br.com.foursales.domain.Product;
import br.com.foursales.dto.ProductRequestDTO;
import br.com.foursales.dto.ProductResponseDTO;
import br.com.foursales.exception.ProductNotFoundException;
import br.com.foursales.mapper.ProductMapper;
import br.com.foursales.repository.ProductRepository;
import br.com.foursales.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;
    private final ProductMapper mapper;

    public ProductServiceImpl(ProductRepository repository, ProductMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public ProductResponseDTO create(ProductRequestDTO dto) {
        Product product = mapper.toEntity(dto);
        Product saved = repository.save(product);
        return mapper.toDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponseDTO findById(UUID id) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Produto não encontrado: " + id));
        return mapper.toDTO(product);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponseDTO> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ProductResponseDTO update(UUID id, ProductRequestDTO dto) {
        Product existing = repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Produto não encontrado: " + id));
        Product updated = mapper.toEntity(dto);
        updated.setId(existing.getId());
        Product saved = repository.save(updated);
        return mapper.toDTO(saved);
    }

    @Override
    public void delete(UUID id) {
        if (!repository.existsById(id)) {
            throw new ProductNotFoundException("Produto não encontrado: " + id);
        }
        repository.deleteById(id);
    }
}