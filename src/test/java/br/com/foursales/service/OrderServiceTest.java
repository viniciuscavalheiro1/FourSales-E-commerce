package br.com.foursales.service;

import br.com.foursales.domain.*;
import br.com.foursales.dto.OrderItemDTO;
import br.com.foursales.dto.OrderRequestDTO;
import br.com.foursales.dto.OrderResponseDTO;
import br.com.foursales.exception.InsufficientStockException;
import br.com.foursales.mapper.OrderMapper;
import br.com.foursales.repository.OrderRepository;
import br.com.foursales.repository.ProductRepository;
import br.com.foursales.repository.UserRepository;
import br.com.foursales.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @InjectMocks
    private OrderServiceImpl orderService;

    @Mock private OrderRepository orderRepository;
    @Mock private ProductRepository productRepository;
    @Mock private UserRepository userRepository;
    @Mock private OrderMapper mapper;
    @Mock private UserDetails userDetails;
    @Mock private SecurityContext securityContext;
    @Mock private Authentication authentication;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("testUser");
    }

    @Test
    void testCreateOrder_success() {
        UUID productId = UUID.randomUUID();
        OrderRequestDTO dto = new OrderRequestDTO(List.of(new OrderItemDTO(productId, 2, BigDecimal.TEN)));

        User user = new User();
        Product product = new Product();
        product.setId(productId);
        product.setStock(5);
        product.setPrice(BigDecimal.TEN);

        Order savedOrder = new Order();
        OrderResponseDTO responseDTO = new OrderResponseDTO();

        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(orderRepository.save(any())).thenReturn(savedOrder);
        when(mapper.toDto(savedOrder)).thenReturn(responseDTO);

        OrderResponseDTO result = orderService.create(dto);

        assertNotNull(result);
        verify(orderRepository).save(any());
    }

    @Test
    void testCreateOrder_insufficientStock_throwsException() {
        UUID productId = UUID.randomUUID();
        OrderRequestDTO dto = new OrderRequestDTO(List.of(new OrderItemDTO(productId, 10, BigDecimal.TEN)));

        User user = new User();
        Product product = new Product();
        product.setId(productId);
        product.setStock(2);

        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        assertThrows(InsufficientStockException.class, () -> orderService.create(dto));
    }

    @Test
    void testPayOrder_success() {
        UUID orderId = UUID.randomUUID();

        Product product = new Product();
        product.setId(UUID.randomUUID());
        product.setStock(5);
        product.setPrice(BigDecimal.TEN);

        OrderItem item = new OrderItem();
        item.setProduct(product);
        item.setQuantity(2);
        item.setUnitPrice(BigDecimal.TEN);

        Order order = new Order();
        order.setStatus(OrderStatus.PENDING);
        order.setItems(List.of(item));

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(productRepository.save(any())).thenReturn(product);
        when(orderRepository.save(any())).thenReturn(order);
        when(mapper.toDto(order)).thenReturn(new OrderResponseDTO());

        OrderResponseDTO result = orderService.pay(orderId);

        assertNotNull(result);
        assertEquals(OrderStatus.PAID, order.getStatus());
    }

    @Test
    void testPayOrder_stockInsufficient_shouldCancelAndThrow() {
        UUID orderId = UUID.randomUUID();

        Product product = new Product();
        product.setId(UUID.randomUUID());
        product.setStock(1);

        OrderItem item = new OrderItem();
        item.setProduct(product);
        item.setQuantity(5);

        Order order = new Order();
        order.setStatus(OrderStatus.PENDING);
        order.setItems(List.of(item));

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderRepository.save(order)).thenReturn(order);

        assertThrows(InsufficientStockException.class, () -> orderService.pay(orderId));
        assertEquals(OrderStatus.CANCELLED, order.getStatus());
    }

    @Test
    void testListByUser_shouldReturnOrders() {
        Order order = new Order();
        OrderResponseDTO dto = new OrderResponseDTO();

        when(orderRepository.findByUserUsername("testUser")).thenReturn(List.of(order));
        when(mapper.toDto(order)).thenReturn(dto);

        List<OrderResponseDTO> result = orderService.listByUser();

        assertEquals(1, result.size());
        verify(orderRepository).findByUserUsername("testUser");
    }
}