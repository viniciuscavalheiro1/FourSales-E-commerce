package br.com.foursales.repository;

import br.com.foursales.domain.Order;
import br.com.foursales.dto.TopBuyerDTO;
import br.com.foursales.dto.AverageTicketDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {

    @Query(value = "SELECT u.username AS username, SUM(o.total_value) AS total_spent " +
            "FROM orders o JOIN users u ON o.user_id = u.id " +
            "WHERE o.status = 'PAID' GROUP BY u.username ORDER BY total_spent DESC LIMIT 5",
            nativeQuery = true)
    List<TopBuyerDTO> findTopBuyers();

    @Query(value = "SELECT u.username AS username, AVG(o.total_value) AS average_ticket " +
            "FROM orders o JOIN users u ON o.user_id = u.id " +
            "WHERE o.status = 'PAID' GROUP BY u.username", nativeQuery = true)
    List<AverageTicketDTO> findAverageTicket();

    @Query(value = "SELECT SUM(total_value) FROM orders " +
            "WHERE status = 'PAID' AND MONTH(created_at) = MONTH(CURRENT_DATE())", nativeQuery = true)
    BigDecimal findMonthlyRevenue();

    List<Order> findByUserUsername(String username);
}