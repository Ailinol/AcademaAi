package mz.uem.events.entity;

/**
 * Status do evento
 */
public enum EventStatus {
    UPCOMING("Próximo"),
    ONGOING("Em Curso"),
    COMPLETED("Concluído"),
    CANCELLED("Cancelado");
    
    private final String displayName;
    
    EventStatus(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}
