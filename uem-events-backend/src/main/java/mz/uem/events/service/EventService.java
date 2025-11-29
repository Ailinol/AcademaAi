package mz.uem.events.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mz.uem.events.dto.CreateEventRequest;
import mz.uem.events.dto.EventCardDTO;
import mz.uem.events.dto.EventDTO;
import mz.uem.events.entity.Event;
import mz.uem.events.entity.EventCategory;
import mz.uem.events.entity.EventStatus;
import mz.uem.events.exception.BusinessException;
import mz.uem.events.exception.ResourceNotFoundException;
import mz.uem.events.repository.EventRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service para lógica de negócio de eventos
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EventService {
    
    private final EventRepository eventRepository;
    
    /**
     * Busca todos os eventos com paginação
     */
    @Transactional(readOnly = true)
    public Page<EventCardDTO> getAllEvents(Pageable pageable) {
        log.debug("Buscando todos os eventos, página: {}", pageable.getPageNumber());
        return eventRepository.findAll(pageable).map(this::toCardDTO);
    }
    
    /**
     * Busca evento por ID
     */
    @Transactional(readOnly = true)
    public EventDTO getEventById(Long id) {
        log.debug("Buscando evento com ID: {}", id);
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Evento", "id", id));
        return toDTO(event);
    }
    
    /**
     * Cria um novo evento
     */
    @Transactional
    public EventDTO createEvent(CreateEventRequest request) {
        log.info("Criando novo evento: {}", request.getTitle());
        
        // Validações de negócio
        if (request.getEventDate().isBefore(LocalDateTime.now())) {
            throw new BusinessException("A data do evento deve ser futura");
        }
        
        Event event = Event.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .eventDate(request.getEventDate())
                .location(request.getLocation())
                .organizer(request.getOrganizer())
                .category(request.getCategory())
                .maxCapacity(request.getMaxCapacity())
                .currentRegistrations(0)
                .status(request.getStatus() != null ? request.getStatus() : EventStatus.UPCOMING)
                .imageUrl(request.getImageUrl())
                .tags(request.getTags())
                .build();
        
        Event savedEvent = eventRepository.save(event);
        log.info("Evento criado com sucesso, ID: {}", savedEvent.getId());
        
        return toDTO(savedEvent);
    }
    
    /**
     * Atualiza um evento existente
     */
    @Transactional
    public EventDTO updateEvent(Long id, CreateEventRequest request) {
        log.info("Atualizando evento ID: {}", id);
        
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Evento", "id", id));
        
        event.setTitle(request.getTitle());
        event.setDescription(request.getDescription());
        event.setEventDate(request.getEventDate());
        event.setLocation(request.getLocation());
        event.setOrganizer(request.getOrganizer());
        event.setCategory(request.getCategory());
        event.setMaxCapacity(request.getMaxCapacity());
        event.setImageUrl(request.getImageUrl());
        event.setTags(request.getTags());
        
        if (request.getStatus() != null) {
            event.setStatus(request.getStatus());
        }
        
        Event updatedEvent = eventRepository.save(event);
        log.info("Evento atualizado com sucesso");
        
        return toDTO(updatedEvent);
    }
    
    /**
     * Deleta um evento
     */
    @Transactional
    public void deleteEvent(Long id) {
        log.info("Deletando evento ID: {}", id);
        
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Evento", "id", id));
        
        eventRepository.delete(event);
        log.info("Evento deletado com sucesso");
    }
    
    /**
     * Filtra eventos por categoria
     */
    @Transactional(readOnly = true)
    public Page<EventCardDTO> getEventsByCategory(EventCategory category, Pageable pageable) {
        log.debug("Buscando eventos da categoria: {}", category);
        return eventRepository.findByCategory(category, pageable).map(this::toCardDTO);
    }
    
    /**
     * Filtra eventos por status
     */
    @Transactional(readOnly = true)
    public Page<EventCardDTO> getEventsByStatus(EventStatus status, Pageable pageable) {
        log.debug("Buscando eventos com status: {}", status);
        return eventRepository.findByStatus(status, pageable).map(this::toCardDTO);
    }
    
    /**
     * Busca eventos próximos (upcoming)
     */
    @Transactional(readOnly = true)
    public List<EventCardDTO> getUpcomingEvents() {
        log.debug("Buscando eventos próximos");
        return eventRepository.findUpcomingEvents(LocalDateTime.now())
                .stream()
                .map(this::toCardDTO)
                .toList();
    }
    
    /**
     * Busca eventos com vagas disponíveis
     */
    @Transactional(readOnly = true)
    public Page<EventCardDTO> getEventsWithAvailableCapacity(Pageable pageable) {
        log.debug("Buscando eventos com vagas disponíveis");
        return eventRepository.findEventsWithAvailableCapacity(pageable).map(this::toCardDTO);
    }
    
    /**
     * Registra um participante no evento
     */
    @Transactional
    public EventDTO registerParticipant(Long eventId) {
        log.info("Registrando participante no evento ID: {}", eventId);
        
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Evento", "id", eventId));
        
        if (event.getStatus() != EventStatus.UPCOMING) {
            throw new BusinessException("Apenas eventos próximos aceitam inscrições");
        }
        
        event.registerParticipant(); // Método da entidade que valida e incrementa
        
        Event updatedEvent = eventRepository.save(event);
        log.info("Participante registrado com sucesso");
        
        return toDTO(updatedEvent);
    }
    
    /**
     * Converte Event para EventDTO
     */
    private EventDTO toDTO(Event event) {
        return EventDTO.builder()
                .id(event.getId())
                .title(event.getTitle())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .location(event.getLocation())
                .organizer(event.getOrganizer())
                .category(event.getCategory())
                .categoryDisplayName(event.getCategory().getDisplayName())
                .maxCapacity(event.getMaxCapacity())
                .currentRegistrations(event.getCurrentRegistrations())
                .availableCapacity(event.getAvailableCapacity())
                .occupancyPercentage(event.getOccupancyPercentage())
                .status(event.getStatus())
                .statusDisplayName(event.getStatus().getDisplayName())
                .imageUrl(event.getImageUrl())
                .tags(event.getTags())
                .createdAt(event.getCreatedAt())
                .updatedAt(event.getUpdatedAt())
                .isFull(event.isFull())
                .hasAvailableCapacity(event.hasAvailableCapacity())
                .build();
    }
    
    /**
     * Converte Event para EventCardDTO
     */
    private EventCardDTO toCardDTO(Event event) {
        return EventCardDTO.builder()
                .id(event.getId())
                .title(event.getTitle())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .location(event.getLocation())
                .organizer(event.getOrganizer())
                .category(event.getCategory())
                .categoryDisplayName(event.getCategory().getDisplayName())
                .status(event.getStatus())
                .statusDisplayName(event.getStatus().getDisplayName())
                .imageUrl(event.getImageUrl())
                .tags(event.getTags())
                .availableCapacity(event.getAvailableCapacity())
                .isFull(event.isFull())
                .build();
    }
}
