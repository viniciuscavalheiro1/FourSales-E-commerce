package br.com.foursales.controller;

import br.com.foursales.dto.AverageTicketDTO;
import br.com.foursales.dto.MonthlyRevenueDTO;
import br.com.foursales.dto.TopBuyerDTO;
import br.com.foursales.service.AnalyticsService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {
    private final AnalyticsService analyticsService;
    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @Operation(summary = "Top 5 compradores por valor gasto")
    @GetMapping("/top-buyers")
    public List<TopBuyerDTO> topBuyers() {
        return analyticsService.getTopBuyers();
    }

    @Operation(summary = "Ticket médio por usuário")
    @GetMapping("/average-ticket")
    public List<AverageTicketDTO> avgTicket() {
        return analyticsService.getAverageTicket();
    }

    @Operation(summary = "Faturamento mensal (mês atual)")
    @GetMapping("/monthly-revenue")
    public MonthlyRevenueDTO monthlyRevenue() {
        return analyticsService.getMonthlyRevenue();
    }
}