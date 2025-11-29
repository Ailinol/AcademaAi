package mz.uem.events.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mz.uem.events.dto.CreateEventRequest;
import mz.uem.events.dto.EventCardDTO;
import mz.uem.events.dto.EventDTO;
import mz.uem.events.entity.EventCategory;
import mz.uem.events.entity.EventStatus;
import mz.uem.events.service.EventService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller para gerenciamento de eventos
 */
@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Events", description = "API de gerenciamento de eventos da UEM")
@CrossOrigin(origins = "*")
public class EventController {
    
    private final EventService eventService;
    
    /**
     * Lista todos os eventos com paginação
     */
    @GetMapping
    @Operation(summary = "Listar todos os eventos", description = "Retorna uma lista paginada de eventos")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de eventos retornada com sucesso")
    })
    public ResponseEntity<Page<EventCardDTO>> getAllEvents(
            @Parameter(description = "Número da página (começa em 0)") 
            @RequestParam(defaultValue = "0") int page,
            
            @Parameter(description = "Tamanho da página") 
            @RequestParam(defaultValue = "10") int size,
            
            @Parameter(description = "Campo para ordenação") 
            @RequestParam(defaultValue = "eventDate") String sortBy,
            
            @Parameter(description = "Direção da ordenação (ASC ou DESC)") 
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        
        log.info("GET /api/events - page: {}, size: {}", page, size);
        
        Sort sort = sortDirection.equalsIgnoreCase("DESC") 
                ? Sort.by(sortBy).descending() 
                : Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<EventCardDTO> events = eventService.getAllEvents(pageable);
        
        return ResponseEntity.ok(events);
    }
    
    /**
     * Busca evento por ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Buscar evento por ID", description = "Retorna os detalhes completos de um evento")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Evento encontrado"),
        @ApiResponse(responseCode = "404", description = "Evento não encontrado")
    })
    public ResponseEntity<EventDTO> getEventById(
            @Parameter(description = "ID do evento") 
            @PathVariable Long id) {
        
        log.info("GET /api/events/{}", id);
        EventDTO event = eventService.getEventById(id);
        return ResponseEntity.ok(event);
    }
    
    /**
     * Cria um novo evento
     */
    @PostMapping
    @Operation(summary = "Criar novo evento", description = "Cria um novo evento no sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Evento criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<EventDTO> createEvent(
            @Parameter(description = "Dados do novo evento") 
            @Valid @RequestBody CreateEventRequest request) {
        
        log.info("POST /api/events - Título: {}", request.getTitle());
        EventDTO createdEvent = eventService.createEvent(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEvent);
    }
    
    /**
     * Atualiza um evento existente
     */
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar evento", description = "Atualiza os dados de um evento existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Evento atualizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Evento não encontrado"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<EventDTO> updateEvent(
            @Parameter(description = "ID do evento") 
            @PathVariable Long id,
            
            @Parameter(description = "Dados atualizados do evento") 
            @Valid @RequestBody CreateEventRequest request) {
        
        log.info("PUT /api/events/{}", id);
        EventDTO updatedEvent = eventService.updateEvent(id, request);
        return ResponseEntity.ok(updatedEvent);
    }
    
    /**
     * Deleta um evento
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar evento", description = "Remove um evento do sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Evento deletado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Evento não encontrado")
    })
    public ResponseEntity<Void> deleteEvent(
            @Parameter(description = "ID do evento") 
            @PathVariable Long id) {
        
        log.info("DELETE /api/events/{}", id);
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }
    
    /**
     * Filtra eventos por categoria
     */
    @GetMapping("/category/{category}")
    @Operation(summary = "Filtrar por categoria", description = "Retorna eventos de uma categoria específica")
    public ResponseEntity<Page<EventCardDTO>> getEventsByCategory(
            @Parameter(description = "Categoria do evento") 
            @PathVariable EventCategory category,
            
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        log.info("GET /api/events/category/{}", category);
        Pageable pageable = PageRequest.of(page, size, Sort.by("eventDate").ascending());
        Page<EventCardDTO> events = eventService.getEventsByCategory(category, pageable);
        return ResponseEntity.ok(events);
    }
    
    /**
     * Filtra eventos por status
     */
    @GetMapping("/status/{status}")
    @Operation(summary = "Filtrar por status", description = "Retorna eventos com um status específico")
    public ResponseEntity<Page<EventCardDTO>> getEventsByStatus(
            @Parameter(description = "Status do evento") 
            @PathVariable EventStatus status,
            
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        log.info("GET /api/events/status/{}", status);
        Pageable pageable = PageRequest.of(page, size, Sort.by("eventDate").ascending());
        Page<EventCardDTO> events = eventService.getEventsByStatus(status, pageable);
        return ResponseEntity.ok(events);
    }
    
    /**
     * Busca eventos próximos
     */
    @GetMapping("/upcoming")
    @Operation(summary = "Eventos próximos", description = "Retorna os próximos eventos agendados")
    public ResponseEntity<List<EventCardDTO>> getUpcomingEvents() {
        log.info("GET /api/events/upcoming");
        List<EventCardDTO> events = eventService.getUpcomingEvents();
        return ResponseEntity.ok(events);
    }
    
    /**
     * Busca eventos com vagas disponíveis
     */
    @GetMapping("/available")
    @Operation(summary = "Eventos com vagas", description = "Retorna eventos que ainda têm vagas disponíveis")
    public ResponseEntity<Page<EventCardDTO>> getEventsWithAvailableCapacity(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        log.info("GET /api/events/available");
        Pageable pageable = PageRequest.of(page, size, Sort.by("eventDate").ascending());
        Page<EventCardDTO> events = eventService.getEventsWithAvailableCapacity(pageable);
        return ResponseEntity.ok(events);
    }
    
    /**
     * Registra um participante no evento
     */
    @PostMapping("/{id}/register")
    @Operation(summary = "Registrar participante", description = "Incrementa o contador de inscrições do evento")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Participante registrado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Evento lotado ou não aceita mais inscrições"),
        @ApiResponse(responseCode = "404", description = "Evento não encontrado")
    })
    public ResponseEntity<EventDTO> registerParticipant(
            @Parameter(description = "ID do evento") 
            @PathVariable Long id) {
        
        log.info("POST /api/events/{}/register", id);
        EventDTO event = eventService.registerParticipant(id);
        return ResponseEntity.ok(event);
    }
}
