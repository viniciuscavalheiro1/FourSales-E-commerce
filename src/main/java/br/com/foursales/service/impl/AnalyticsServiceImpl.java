package br.com.foursales.service.impl;

import br.com.foursales.dto.AverageTicketDTO;
import br.com.foursales.dto.MonthlyRevenueDTO;
import br.com.foursales.dto.TopBuyerDTO;
import br.com.foursales.repository.OrderRepository;
import br.com.foursales.service.AnalyticsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class AnalyticsServiceImpl implements AnalyticsService {

    private final OrderRepository orderRepository;

    public AnalyticsServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public List<TopBuyerDTO> getTopBuyers() {
        return orderRepository.findTopBuyers();
    }

    @Override
    public List<AverageTicketDTO> getAverageTicket() {
        return orderRepository.findAverageTicket();
    }

    @Override
    public MonthlyRevenueDTO getMonthlyRevenue() {
        BigDecimal rev = orderRepository.findMonthlyRevenue();
        return new MonthlyRevenueDTO(rev != null ? rev : BigDecimal.ZERO);
    }
}