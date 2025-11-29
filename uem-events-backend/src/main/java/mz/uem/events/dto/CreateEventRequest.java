package mz.uem.events.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mz.uem.events.entity.EventCategory;
import mz.uem.events.entity.EventStatus;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO para criação de eventos
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateEventRequest {
    
    @NotBlank(message = "Título é obrigatório")
    @Size(min = 3, max = 200, message = "Título deve ter entre 3 e 200 caracteres")
    private String title;
    
    @NotBlank(message = "Descrição é obrigatória")
    @Size(min = 10, max = 2000, message = "Descrição deve ter entre 10 e 2000 caracteres")
    private String description;
    
    @NotNull(message = "Data do evento é obrigatória")
    @Future(message = "Data do evento deve ser futura")
    private LocalDateTime eventDate;
    
    @NotBlank(message = "Local é obrigatório")
    @Size(max = 200)
    private String location;
    
    @NotBlank(message = "Organizador é obrigatório")
    @Size(max = 150)
    private String organizer;
    
    @NotNull(message = "Categoria é obrigatória")
    private EventCategory category;
    
    @NotNull(message = "Capacidade máxima é obrigatória")
    @Min(value = 1, message = "Capacidade máxima deve ser pelo menos 1")
    private Integer maxCapacity;
    
    @Size(max = 500, message = "URL da imagem muito longa")
    private String imageUrl;
    
    private List<String> tags;
    
    private EventStatus status;
}
