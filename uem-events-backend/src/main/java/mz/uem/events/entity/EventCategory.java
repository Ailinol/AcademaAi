package mz.uem.events.entity;

/**
 * Categoria de eventos da UEM
 */
public enum EventCategory {
    ACADEMIC("Académico"),
    CULTURAL("Cultural"),
    SPORTS("Desporto"),
    TECH("Tecnologia"),
    WORKSHOP("Workshop"),
    SEMINAR("Seminário"),
    CONFERENCE("Conferência"),
    GRADUATION("Graduação"),
    CAREER("Carreira"),
    OTHER("Outro");
    
    private final String displayName;
    
    EventCategory(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}
