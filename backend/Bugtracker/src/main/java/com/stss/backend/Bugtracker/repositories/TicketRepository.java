package com.stss.backend.Bugtracker.repositories;

import com.stss.backend.Bugtracker.enums.TicketType;
import com.stss.backend.Bugtracker.models.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Integer> {
    Ticket findByTicketId(int ticketId);
    Ticket findByTicketType(TicketType ticketType);
    @Query(value = "SELECT * FROM tickets t where t.user_id = :userId OR t.assigned_to = :userId",nativeQuery = true)
    List<Ticket> findByUserId(@Param("userId") Long userId);
}
