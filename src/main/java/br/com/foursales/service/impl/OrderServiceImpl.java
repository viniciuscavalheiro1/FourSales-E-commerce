package br.com.foursales.service.impl;

import br.com.foursales.domain.*;
import br.com.foursales.dto.OrderRequestDTO;
import br.com.foursales.dto.OrderResponseDTO;
import br.com.foursales.exception.InsufficientStockException;
import br.com.foursales.exception.ResourceNotFoundException;
import br.com.foursales.mapper.OrderMapper;
import br.com.foursales.repository.OrderRepository;
import br.com.foursales.repository.ProductRepository;
import br.com.foursales.repository.UserRepository;
import br.com.foursales.service.OrderService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final OrderMapper mapper;

    public OrderServiceImpl(OrderRepository orderRepository,
                            ProductRepository productRepository,
                            UserRepository userRepository,
                            OrderMapper mapper) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    @Override
    public OrderResponseDTO create(OrderRequestDTO dto) {
        UserDetails principal = (UserDetails)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByUsername(principal.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuário não encontrado: " + principal.getUsername()));

        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.PENDING);

        List<OrderItem> items = dto.getItems().stream().map(i -> {
            Product p = productRepository.findById(i.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Produto não encontrado: " + i.getProductId()));
            if (p.getStock() < i.getQuantity()) {
                throw new InsufficientStockException(
                        "Estoque insuficiente para produto: " + p.getId());
            }
            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProduct(p);
            item.setQuantity(i.getQuantity());
            item.setUnitPrice(p.getPrice());
            return item;
        }).collect(Collectors.toList());

        order.setItems(items);
        Order saved = orderRepository.save(order);
        return mapper.toDto(saved);
    }

    @Override
    public OrderResponseDTO pay(UUID id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Pedido não encontrado: " + id));
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new IllegalStateException("Pedido não está em status PENDING");
        }
        order.getItems().forEach(item -> {
            Product p = item.getProduct();
            if (p.getStock() < item.getQuantity()) {
                order.setStatus(OrderStatus.CANCELLED);
                orderRepository.save(order);
                throw new InsufficientStockException(
                        "Estoque insuficiente para produto: " + p.getId());
            }
            p.setStock(p.getStock() - item.getQuantity());
            productRepository.save(p);
        });
        order.setStatus(OrderStatus.PAID);
        orderRepository.save(order);
        return mapper.toDto(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponseDTO> listByUser() {
        UserDetails principal = (UserDetails)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return orderRepository.findByUserUsername(principal.getUsername())
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }
}