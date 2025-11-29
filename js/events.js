/**
 * Events JavaScript - Frontend integration with Spring Boot API
 * Handles fetching, rendering, and filtering events
 */

const API_BASE_URL = 'http://localhost:8081/api/events';

// Category colors - Strict UEM Palette (Green/Gold variations)
const CATEGORY_COLORS = {
    'TECH': '#1A5D3A',       // Primary Green
    'ACADEMIC': '#237A4B',   // Secondary Green
    'CULTURAL': '#C1A240',   // Gold
    'SPORTS': '#1A5D3A',
    'WORKSHOP': '#237A4B',
    'SEMINAR': '#1A5D3A',
    'CONFERENCE': '#C1A240',
    'GRADUATION': '#C1A240',
    'CAREER': '#237A4B',
    'OTHER': '#666666'
};

// Portuguese Translations for Categories
const CATEGORY_TRANSLATIONS = {
    'TECH': 'Tecnologia',
    'ACADEMIC': 'Académico',
    'CULTURAL': 'Cultural',
    'SPORTS': 'Desporto',
    'WORKSHOP': 'Workshop',
    'SEMINAR': 'Seminário',
    'CONFERENCE': 'Conferência',
    'GRADUATION': 'Graduação',
    'CAREER': 'Carreira',
    'OTHER': 'Outro'
};

// Status colors - Minimalist
const STATUS_COLORS = {
    'UPCOMING': '#1A5D3A',   // Green
    'ONGOING': '#C1A240',    // Gold
    'COMPLETED': '#666666',  // Gray
    'CANCELLED': '#d32f2f'   // Red (Keep for alert)
};

// Portuguese Translations for Status
const STATUS_TRANSLATIONS = {
    'UPCOMING': 'Agendado',
    'ONGOING': 'Em Curso',
    'COMPLETED': 'Realizado',
    'CANCELLED': 'Cancelado'
};

// Sample data for fallback when backend is offline
const SAMPLE_EVENTS = [
    {
        id: 1,
        title: "Conferência de Tecnologia e Inovação",
        description: "Um encontro anual para discutir as últimas tendências em tecnologia, IA e desenvolvimento de software em Moçambique.",
        eventDate: "2025-11-15T09:00:00",
        location: "Centro Cultural Universitário",
        organizer: "Faculdade de Engenharia",
        category: "TECH",
        status: "UPCOMING",
        maxCapacity: 200,
        currentRegistrations: 150,
        imageUrl: "https://images.unsplash.com/photo-1540575467063-178a50c2df87?auto=format&fit=crop&q=80&w=1000",
        tags: ["Tecnologia", "Inovação", "Networking"]
    },
    {
        id: 2,
        title: "Cerimónia de Graduação 2024",
        description: "Cerimónia oficial de graduação para os finalistas de todos os cursos da UEM.",
        eventDate: "2025-12-10T08:00:00",
        location: "Estádio Universitário",
        organizer: "Reitoria",
        category: "ACADEMIC",
        status: "UPCOMING",
        maxCapacity: 5000,
        currentRegistrations: 4200,
        imageUrl: "https://images.unsplash.com/photo-1523580846011-d3a5bc25702b?auto=format&fit=crop&q=80&w=1000",
        tags: ["Graduação", "Académico", "Celebração"]
    },
    {
        id: 3,
        title: "Feira de Carreiras UEM 2025",
        description: "Oportunidade para estudantes conectarem-se com empresas líderes e explorarem oportunidades de estágio e emprego.",
        eventDate: "2025-10-25T10:00:00",
        location: "Campus Principal",
        organizer: "Gabinete de Apoio ao Estudante",
        category: "CAREER",
        status: "UPCOMING",
        maxCapacity: 1000,
        currentRegistrations: 300,
        imageUrl: "https://images.unsplash.com/photo-1559136555-9303baea8ebd?auto=format&fit=crop&q=80&w=1000",
        tags: ["Carreira", "Emprego", "Estágio"]
    },
    {
        id: 4,
        title: "Workshop de Desenvolvimento Web Moderno",
        description: "Aprenda as tecnologias mais recentes de desenvolvimento web: React, Node.js e Spring Boot.",
        eventDate: "2025-11-05T14:00:00",
        location: "Laboratório de Informática 3",
        organizer: "Departamento de Matemática e Informática",
        category: "WORKSHOP",
        status: "UPCOMING",
        maxCapacity: 30,
        currentRegistrations: 28,
        imageUrl: "https://images.unsplash.com/photo-1531482615713-2afd69097998?auto=format&fit=crop&q=80&w=1000",
        tags: ["Web Dev", "Programação", "Workshop"]
    },
    {
        id: 5,
        title: "Festival Cultural da UEM",
        description: "Celebração da diversidade cultural com música, dança, teatro e gastronomia.",
        eventDate: "2025-09-20T16:00:00",
        location: "Jardim Universitário",
        organizer: "Direcção de Cultura",
        category: "CULTURAL",
        status: "COMPLETED",
        maxCapacity: 2000,
        currentRegistrations: 2000,
        imageUrl: "https://images.unsplash.com/photo-1514525253440-b393452e8d26?auto=format&fit=crop&q=80&w=1000",
        tags: ["Cultura", "Música", "Arte"]
    },
    {
        id: 6,
        title: "Seminário de Pesquisa Científica",
        description: "Apresentação de resultados de pesquisas recentes realizadas por docentes e estudantes da UEM.",
        eventDate: "2025-11-30T09:00:00",
        location: "Auditório da Faculdade de Ciências",
        organizer: "Direcção Científica",
        category: "SEMINAR",
        status: "UPCOMING",
        maxCapacity: 100,
        currentRegistrations: 45,
        imageUrl: "https://images.unsplash.com/photo-1515187029135-18ee286d815b?auto=format&fit=crop&q=80&w=1000",
        tags: ["Pesquisa", "Ciência", "Académico"]
    }
];

let allEvents = [];

/**
 * Fetch all events from the API with fallback
 */
async function fetchEvents(filters = {}) {
    showLoading(true);
    try {
        let url = API_BASE_URL;

        // Construct correct API endpoint based on filters
        if (filters.category) {
            url = `${API_BASE_URL}/category/${filters.category}`;
        } else if (filters.status) {
            url = `${API_BASE_URL}/status/${filters.status}`;
        }

        // Try to fetch from API
        const response = await fetch(url);

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        const data = await response.json();
        allEvents = data.content || data;
        renderEvents(allEvents);

    } catch (error) {
        console.warn('Backend offline or error, using sample data:', error);

        // Fallback to sample data
        allEvents = [...SAMPLE_EVENTS];

        // Apply local filtering
        if (filters.category) {
            allEvents = allEvents.filter(e => e.category === filters.category);
        }
        if (filters.status) {
            allEvents = allEvents.filter(e => e.status === filters.status);
        }

        renderEvents(allEvents);

        // Show offline notification
        showOfflineNotification();
    } finally {
        showLoading(false);
    }
}

function showOfflineNotification() {
    const grid = document.querySelector('.events-grid');
    if (grid && !document.querySelector('.offline-notice')) {
        const notice = document.createElement('div');
        notice.className = 'offline-notice';
        notice.style.cssText = 'grid-column: 1/-1; text-align: center; padding: 10px; background: #fff3cd; color: #856404; border-radius: 4px; margin-bottom: 1rem; border: 1px solid #ffeeba;';
        notice.innerHTML = '⚠️ <strong>Modo de Demonstração</strong>: O backend está offline. Exibindo dados de exemplo.';
        grid.insertBefore(notice, grid.firstChild);
    }
}

/**
 * Render events in the grid
 */
function renderEvents(events) {
    const eventsGrid = document.querySelector('.events-grid');

    if (!eventsGrid) {
        console.error('Events grid not found');
        return;
    }

    if (!events || events.length === 0) {
        eventsGrid.innerHTML = `
            <div style="grid-column: 1/-1; text-align: center; padding: 3rem; color: #666;">
                <p style="font-size: 1.2rem;">Nenhum evento encontrado.</p>
            </div>
        `;
        return;
    }

    eventsGrid.innerHTML = events.map(event => createEventCard(event)).join('');
}

/**
 * Create HTML for an event card - Minimalist Version
 */
function createEventCard(event) {
    const eventDate = new Date(event.eventDate);
    const dateStr = formatDate(eventDate);
    const timeStr = formatTime(eventDate);

    const categoryColor = CATEGORY_COLORS[event.category] || '#666';
    const statusColor = STATUS_COLORS[event.status] || '#666';

    // Translate labels
    const categoryLabel = CATEGORY_TRANSLATIONS[event.category] || event.category;
    const statusLabel = STATUS_TRANSLATIONS[event.status] || event.status;

    // Calculate capacity
    const maxCap = event.maxCapacity || 100;
    const currentReg = event.currentRegistrations || 0;
    const available = maxCap - currentReg;
    const isFull = currentReg >= maxCap;
    const percentFull = (currentReg / maxCap) * 100;

    return `
        <div class="event-card">
            <div class="event-image" style="background-image: url('${event.imageUrl || '../assets/default-event.jpg'}')"></div>
            
            <div class="event-content">
                <div class="event-header">
                    <div class="event-date">${dateStr}</div>
                    <div class="event-badges">
                        <span class="event-badge" style="background-color: ${categoryColor}; color: white;">
                            ${categoryLabel}
                        </span>
                    </div>
                </div>
                
                <h3 class="event-title">${event.title}</h3>
                
                <div class="event-meta">
                    <div class="event-location">
                        <span>Local:</span> ${event.location}
                    </div>
                    <div class="event-organizer">
                        <span>Org:</span> ${event.organizer}
                    </div>
                </div>
                
                <div class="event-capacity">
                    <div class="capacity-info">
                        <span class="capacity-label">
                            ${isFull ? 'Lotado' : `${available} vagas`}
                        </span>
                        <span style="font-size: 0.7rem;">${currentReg}/${maxCap}</span>
                    </div>
                    <div class="capacity-bar">
                        <div class="capacity-fill ${isFull ? 'full' : ''}" 
                             style="width: ${percentFull}%"></div>
                    </div>
                </div>
                
                <button class="btn-view-event" onclick="showEventDetails(${event.id})">Ver Detalhes</button>
            </div>
        </div>
    `;
}

/**
 * Show event details in modal - Minimalist
 */
function showEventDetails(eventId) {
    const event = allEvents.find(e => e.id === eventId);
    if (!event) return;

    const modal = document.getElementById('event-modal');
    const modalTitle = document.getElementById('modal-title');
    const modalDate = document.getElementById('modal-date');
    const modalLocation = document.getElementById('modal-location');
    const modalDesc = document.getElementById('modal-desc');

    if (!modal) return;

    const eventDate = new Date(event.eventDate);
    const categoryLabel = CATEGORY_TRANSLATIONS[event.category] || event.category;
    const statusLabel = STATUS_TRANSLATIONS[event.status] || event.status;

    modalTitle.textContent = event.title;
    modalDate.textContent = formatDate(eventDate);
    modalLocation.textContent = `Local: ${event.location}`;

    // Detailed description with extra info
    modalDesc.innerHTML = `
        <p style="margin-bottom: 1.5rem; line-height: 1.6; color: #333;">${event.description}</p>
        
        <div style="background: #f9f9f9; padding: 1rem; border-radius: 4px; margin-bottom: 1rem; border: 1px solid #eee;">
            <p style="margin-bottom: 0.5rem;"><strong>Organizador:</strong> ${event.organizer}</p>
            <p style="margin-bottom: 0.5rem;"><strong>Categoria:</strong> ${categoryLabel}</p>
            <p style="margin-bottom: 0.5rem;"><strong>Status:</strong> ${statusLabel}</p>
            <p><strong>Tags:</strong> ${(event.tags || []).join(', ')}</p>
        </div>

        <button class="btn-register" onclick="registerForEvent(${event.id})" style="width: 100%; padding: 0.8rem; background: var(--uem-green-primary); color: white; border: none; border-radius: 4px; font-weight: bold; cursor: pointer;">
            Inscrever-se no Evento
        </button>
    `;

    modal.style.display = 'block';

    // Close handlers
    const closeBtn = document.querySelector('.btn-close');
    if (closeBtn) closeBtn.onclick = () => modal.style.display = 'none';
    window.onclick = (e) => {
        if (e.target === modal) modal.style.display = 'none';
    };
}

/**
 * Register for an event
 */
async function registerForEvent(eventId) {
    alert('Funcionalidade de inscrição será processada pelo backend quando online.');
    document.getElementById('event-modal').style.display = 'none';
}

// Helper functions
function formatDate(date) {
    return new Intl.DateTimeFormat('pt-MZ', {
        day: 'numeric', month: 'long', year: 'numeric'
    }).format(date);
}

function formatTime(date) {
    return new Intl.DateTimeFormat('pt-MZ', {
        hour: '2-digit', minute: '2-digit'
    }).format(date);
}

function showLoading(show) {
    const grid = document.querySelector('.events-grid');
    if (!grid) return;

    if (show) {
        grid.innerHTML = `
            <div class="loading-spinner">
                <p>Carregando eventos...</p>
            </div>
        `;
    }
}

// Filter functions
function filterByCategory(category) {
    updateActiveButton(event.target);
    fetchEvents({ category: category });
}

function filterByStatus(status) {
    updateActiveButton(event.target);
    fetchEvents({ status: status });
}

function updateActiveButton(clickedBtn) {
    document.querySelectorAll('.filter-btn').forEach(btn => btn.classList.remove('active'));
    if (clickedBtn) clickedBtn.classList.add('active');
}

// Initial load
document.addEventListener('DOMContentLoaded', () => {
    loadEvents();
});

function loadEvents() {
    const firstBtn = document.querySelector('.filter-btn');
    if (firstBtn) updateActiveButton(firstBtn);
    fetchEvents();
}
