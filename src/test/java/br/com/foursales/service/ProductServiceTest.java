package br.com.foursales.service;

import br.com.foursales.domain.Category;
import br.com.foursales.domain.Product;
import br.com.foursales.dto.ProductRequestDTO;
import br.com.foursales.dto.ProductResponseDTO;
import br.com.foursales.exception.ProductNotFoundException;
import br.com.foursales.mapper.ProductMapper;
import br.com.foursales.repository.ProductRepository;
import br.com.foursales.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @Mock
    private ProductRepository repository;

    @Mock
    private ProductMapper mapper;

    @InjectMocks
    private ProductServiceImpl service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void create_shouldReturnSavedProductResponseDTO() {
        ProductRequestDTO dto = new ProductRequestDTO("Name", "Description", BigDecimal.TEN, Category.ELECTRONICS, 10);
        Product product = new Product();
        Product saved = new Product();
        ProductResponseDTO response = new ProductResponseDTO();

        when(mapper.toEntity(dto)).thenReturn(product);
        when(repository.save(product)).thenReturn(saved);
        when(mapper.toDTO(saved)).thenReturn(response);

        ProductResponseDTO result = service.create(dto);

        assertEquals(response, result);
        verify(repository).save(product);
    }

    @Test
    void findById_shouldReturnProductResponseDTO_whenExists() {
        UUID id = UUID.randomUUID();
        Product product = new Product();
        ProductResponseDTO response = new ProductResponseDTO();

        when(repository.findById(id)).thenReturn(Optional.of(product));
        when(mapper.toDTO(product)).thenReturn(response);

        ProductResponseDTO result = service.findById(id);

        assertEquals(response, result);
    }

    @Test
    void findById_shouldThrow_whenNotFound() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> service.findById(id));
    }

    @Test
    void findAll_shouldReturnListOfDTOs() {
        List<Product> list = List.of(new Product());
        List<ProductResponseDTO> dtos = List.of(new ProductResponseDTO());

        when(repository.findAll()).thenReturn(list);
        when(mapper.toDTO(any())).thenReturn(dtos.get(0));

        List<ProductResponseDTO> result = service.findAll();

        assertEquals(1, result.size());
    }

    @Test
    void update_shouldReturnUpdatedProduct() {
        UUID id = UUID.randomUUID();
        ProductRequestDTO dto = new ProductRequestDTO("Name", "Desc", BigDecimal.ONE, Category.ELECTRONICS, 5);
        Product existing = new Product();
        existing.setId(id);
        Product updated = new Product();
        Product saved = new Product();
        ProductResponseDTO response = new ProductResponseDTO();

        when(repository.findById(id)).thenReturn(Optional.of(existing));
        when(mapper.toEntity(dto)).thenReturn(updated);
        when(repository.save(any())).thenReturn(saved);
        when(mapper.toDTO(saved)).thenReturn(response);

        ProductResponseDTO result = service.update(id, dto);

        assertEquals(response, result);
    }

    @Test
    void delete_shouldRemove_whenExists() {
        UUID id = UUID.randomUUID();
        when(repository.existsById(id)).thenReturn(true);

        service.delete(id);

        verify(repository).deleteById(id);
    }

    @Test
    void delete_shouldThrow_whenNotFound() {
        UUID id = UUID.randomUUID();
        when(repository.existsById(id)).thenReturn(false);

        assertThrows(ProductNotFoundException.class, () -> service.delete(id));
    }
}