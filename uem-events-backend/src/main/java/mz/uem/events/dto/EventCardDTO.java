package mz.uem.events.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mz.uem.events.entity.EventCategory;
import mz.uem.events.entity.EventStatus;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO resumido do evento (para cards/listagens)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventCardDTO {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime eventDate;
    private String location;
    private String organizer;
    private EventCategory category;
    private String categoryDisplayName;
    private EventStatus status;
    private String statusDisplayName;
    private String imageUrl;
    private List<String> tags;
    private Integer availableCapacity;
    private Boolean isFull;
}
