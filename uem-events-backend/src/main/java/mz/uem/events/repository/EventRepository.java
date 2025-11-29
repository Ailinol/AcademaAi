package mz.uem.events.repository;

import mz.uem.events.entity.Event;
import mz.uem.events.entity.EventCategory;
import mz.uem.events.entity.EventStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository para acesso aos dados de eventos
 */
@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    
    /**
     * Busca eventos por categoria
     */
    Page<Event> findByCategory(EventCategory category, Pageable pageable);
    
    /**
     * Busca eventos por status
     */
    Page<Event> findByStatus(EventStatus status, Pageable pageable);
    
    /**
     * Busca eventos entre datas
     */
    Page<Event> findByEventDateBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);
    
    /**
     * Busca eventos após uma data específica
     */
    Page<Event> findByEventDateAfter(LocalDateTime date, Pageable pageable);
    
    /**
     * Busca eventos por tag
     */
    @Query("SELECT DISTINCT e FROM Event e JOIN e.tags t WHERE t = :tag")
    Page<Event> findByTag(@Param("tag") String tag, Pageable pageable);
    
    /**
     * Busca eventos próximos (upcoming)
     */
    @Query("SELECT e FROM Event e WHERE e.status = 'UPCOMING' AND e.eventDate >= :now ORDER BY e.eventDate ASC")
    List<Event> findUpcomingEvents(@Param("now") LocalDateTime now);
    
    /**
     * Busca eventos por organizador
     */
    Page<Event> findByOrganizerContainingIgnoreCase(String organizer, Pageable pageable);
    
    /**
     * Busca eventos com vagas disponíveis
     */
    @Query("SELECT e FROM Event e WHERE e.currentRegistrations < e.maxCapacity AND e.status = 'UPCOMING'")
    Page<Event> findEventsWithAvailableCapacity(Pageable pageable);
}
