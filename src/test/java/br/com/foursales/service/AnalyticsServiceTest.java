package br.com.foursales.service;

import br.com.foursales.dto.AverageTicketDTO;
import br.com.foursales.dto.MonthlyRevenueDTO;
import br.com.foursales.dto.TopBuyerDTO;
import br.com.foursales.repository.OrderRepository;
import br.com.foursales.service.impl.AnalyticsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AnalyticsServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private AnalyticsServiceImpl analyticsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getTopBuyers_shouldReturnList() {
        List<TopBuyerDTO> expected = List.of(new TopBuyerDTO("user1", BigDecimal.valueOf(1000)));
        when(orderRepository.findTopBuyers()).thenReturn(expected);

        List<TopBuyerDTO> result = analyticsService.getTopBuyers();

        assertEquals(1, result.size());
        assertEquals("user1", result.get(0).getUsername());
    }

    @Test
    void getAverageTicket_shouldReturnList() {
        List<AverageTicketDTO> expected = List.of(new AverageTicketDTO("user2", BigDecimal.valueOf(500)));
        when(orderRepository.findAverageTicket()).thenReturn(expected);

        List<AverageTicketDTO> result = analyticsService.getAverageTicket();

        assertEquals(1, result.size());
        assertEquals("user2", result.get(0).getUsername());
    }

    @Test
    void getMonthlyRevenue_shouldReturnValue() {
        when(orderRepository.findMonthlyRevenue()).thenReturn(BigDecimal.valueOf(15000));

        MonthlyRevenueDTO result = analyticsService.getMonthlyRevenue();

        assertNotNull(result);
        assertEquals(BigDecimal.valueOf(15000), result.getRevenue());
    }

    @Test
    void getMonthlyRevenue_whenNull_shouldReturnZero() {
        when(orderRepository.findMonthlyRevenue()).thenReturn(null);

        MonthlyRevenueDTO result = analyticsService.getMonthlyRevenue();

        assertNotNull(result);
        assertEquals(BigDecimal.ZERO, result.getRevenue());
    }
}