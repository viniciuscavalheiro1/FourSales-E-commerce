package br.com.foursales.service;

import br.com.foursales.dto.AverageTicketDTO;
import br.com.foursales.dto.MonthlyRevenueDTO;
import br.com.foursales.dto.TopBuyerDTO;
import java.util.List;

public interface AnalyticsService {
    List<TopBuyerDTO> getTopBuyers();
    List<AverageTicketDTO> getAverageTicket();
    MonthlyRevenueDTO getMonthlyRevenue();
}