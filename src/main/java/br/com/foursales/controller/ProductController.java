package br.com.foursales.controller;

import br.com.foursales.dto.ProductRequestDTO;
import br.com.foursales.dto.ProductResponseDTO;
import br.com.foursales.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/produtos")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @Operation(summary = "Criar novo produto")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Produto criado com sucesso",
                    content = @Content(schema = @Schema(implementation = ProductResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @PostMapping
    public ResponseEntity<ProductResponseDTO> create(
            @Valid @RequestBody ProductRequestDTO dto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.create(dto));
    }

    @Operation(summary = "Listar todos os produtos")
    @GetMapping
    public List<ProductResponseDTO> findAll() {
        return service.findAll();
    }

    @Operation(summary = "Buscar produto por ID")
    @GetMapping("/{id}")
    public ProductResponseDTO findById(@PathVariable UUID id) {
        return service.findById(id);
    }

    @Operation(summary = "Atualizar produto")
    @PutMapping("/{id}")
    public ProductResponseDTO update(
            @PathVariable UUID id,
            @Validated @RequestBody ProductRequestDTO dto
    ) {
        return service.update(id, dto);
    }

    @Operation(summary = "Excluir produto")
    @ApiResponse(responseCode = "204", description = "Produto excluído com sucesso")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}