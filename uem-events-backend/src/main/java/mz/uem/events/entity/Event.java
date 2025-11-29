package mz.uem.events.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidade Event - Representa um evento da UEM com informações expandidas
 */
@Entity
@Table(name = "events")
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Título é obrigatório")
    @Size(min = 3, max = 200, message = "Título deve ter entre 3 e 200 caracteres")
    @Column(nullable = false, length = 200)
    private String title;
    
    @NotBlank(message = "Descrição é obrigatória")
    @Size(min = 10, max = 2000, message = "Descrição deve ter entre 10 e 2000 caracteres")
    @Column(nullable = false, length = 2000)
    private String description;
    
    @NotNull(message = "Data do evento é obrigatória")
    @Future(message = "Data do evento deve ser futura")
    @Column(nullable = false)
    private LocalDateTime eventDate;
    
    @NotBlank(message = "Local é obrigatório")
    @Size(max = 200)
    @Column(nullable = false, length = 200)
    private String location;
    
    @NotBlank(message = "Organizador é obrigatório")
    @Size(max = 150)
    @Column(nullable = false, length = 150)
    private String organizer;
    
    @NotNull(message = "Categoria é obrigatória")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private EventCategory category;
    
    @Min(value = 1, message = "Capacidade máxima deve ser pelo menos 1")
    @Column(nullable = false)
    private Integer maxCapacity;
    
    @Min(value = 0, message = "Inscrições não podem ser negativas")
    @Column(nullable = false)
    @Builder.Default
    private Integer currentRegistrations = 0;
    
    @NotNull(message = "Status é obrigatório")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    @Builder.Default
    private EventStatus status = EventStatus.UPCOMING;
    
    @Size(max = 500, message = "URL da imagem muito longa")
    @Column(length = 500)
    private String imageUrl;
    
    @ElementCollection
    @CollectionTable(name = "event_tags", joinColumns = @JoinColumn(name = "event_id"))
    @Column(name = "tag", length = 50)
    @Builder.Default
    private List<String> tags = new ArrayList<>();
    
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    
    /**
     * Calcula as vagas disponíveis
     */
    public Integer getAvailableCapacity() {
        return maxCapacity - currentRegistrations;
    }
    
    /**
     * Verifica se há vagas disponíveis
     */
    public boolean hasAvailableCapacity() {
        return getAvailableCapacity() > 0;
    }
    
    /**
     * Verifica se o evento está lotado
     */
    public boolean isFull() {
        return currentRegistrations >= maxCapacity;
    }
    
    /**
     * Incrementa o número de inscrições
     */
    public void registerParticipant() {
        if (isFull()) {
            throw new IllegalStateException("Evento lotado - sem vagas disponíveis");
        }
        this.currentRegistrations++;
    }
    
    /**
     * Calcula a percentagem de ocupação
     */
    public Double getOccupancyPercentage() {
        if (maxCapacity == 0) return 0.0;
        return (currentRegistrations.doubleValue() / maxCapacity.doubleValue()) * 100;
    }
}
