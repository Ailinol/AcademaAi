package mz.uem.events.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mz.uem.events.entity.Event;
import mz.uem.events.entity.EventCategory;
import mz.uem.events.entity.EventStatus;
import mz.uem.events.repository.EventRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * Inicializa dados de exemplo no banco de dados
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {
    
    private final EventRepository eventRepository;
    
    @Bean
    public CommandLineRunner initData() {
        return args -> {
            if (eventRepository.count() == 0) {
                log.info("Populando banco de dados com eventos de exemplo...");
                
                List<Event> sampleEvents = Arrays.asList(
                    Event.builder()
                        .title("Conferência de Tecnologia e Inovação")
                        .description("Um encontro sobre as novas tendências tecnológicas em Moçambique e África. Palestrantes especializados irão apresentar as últimas inovações em IA, Cloud Computing, e desenvolvimento mobile.")
                        .eventDate(LocalDateTime.now().plusDays(5))
                        .location("Complexo Pedagógico - Auditório Principal")
                        .organizer("Faculdade de Engenharia - UEM")
                        .category(EventCategory.TECH)
                        .maxCapacity(150)
                        .currentRegistrations(45)
                        .status(EventStatus.UPCOMING)
                        .imageUrl("https://images.unsplash.com/photo-1540575467063-178a50c2df87?w=800")
                        .tags(Arrays.asList("Tecnologia", "Inovação", "IA", "Cloud"))
                        .build(),
                        
                    Event.builder()
                        .title("Cerimónia de Graduação 2024")
                        .description("Celebração da graduação dos estudantes finalistas de 2024. Uma ocasião solene para celebrar as conquistas académicas dos nossos formandos.")
                        .eventDate(LocalDateTime.now().plusDays(8))
                        .location("Estádio Universitário")
                        .organizer("Reitoria da UEM")
                        .category(EventCategory.GRADUATION)
                        .maxCapacity(500)
                        .currentRegistrations(320)
                        .status(EventStatus.UPCOMING)
                        .imageUrl("https://images.unsplash.com/photo-1523050854058-8df90110c9f1?w=800")
                        .tags(Arrays.asList("Graduação", "Formatura", "Celebração"))
                        .build(),
                        
                    Event.builder()
                        .title("Feira de Carreiras UEM 2025")
                        .description("Oportunidades de estágio e emprego para estudantes e recém-graduados. Principais empresas de Moçambique estarão presentes para recrutar talentos.")
                        .eventDate(LocalDateTime.now().plusDays(15))
                        .location("Biblioteca Central - Pavilhão de Exposições")
                        .organizer("Departamento de Carreiras e Empregabilidade")
                        .category(EventCategory.CAREER)
                        .maxCapacity(200)
                        .currentRegistrations(87)
                        .status(EventStatus.UPCOMING)
                        .imageUrl("https://images.unsplash.com/photo-1511578314322-379afb476865?w=800")
                        .tags(Arrays.asList("Carreira", "Emprego", "Estágio", "Networking"))
                        .build(),
                        
                    Event.builder()
                        .title("Workshop de Desenvolvimento Web Moderno")
                        .description("Aprenda a criar aplicações web profissionais usando React, Node.js e MongoDB. Workshop prático com projetos hands-on.")
                        .eventDate(LocalDateTime.now().plusDays(10))
                        .location("Laboratório de Informática - Sala 203")
                        .organizer("Centro de Informática da UEM")
                        .category(EventCategory.WORKSHOP)
                        .maxCapacity(30)
                        .currentRegistrations(28)
                        .status(EventStatus.UPCOMING)
                        .imageUrl("https://images.unsplash.com/photo-1498050108023-c5249f4df085?w=800")
                        .tags(Arrays.asList("Web Development", "React", "Node.js", "Programação"))
                        .build(),
                        
                    Event.builder()
                        .title("Seminário de Pesquisa Científica")
                        .description("Apresentação dos trabalhos de investigação científica desenvolvidos pelos estudantes de mestrado e doutoramento da UEM.")
                        .eventDate(LocalDateTime.now().plusDays(20))
                        .location("Anfiteatro Central")
                        .organizer("Pró-Reitoria de Pesquisa")
                        .category(EventCategory.SEMINAR)
                        .maxCapacity(100)
                        .currentRegistrations(35)
                        .status(EventStatus.UPCOMING)
                        .imageUrl("https://images.unsplash.com/photo-1591115765373-5207764f72e7?w=800")
                        .tags(Arrays.asList("Pesquisa", "Ciência", "Académico"))
                        .build(),
                        
                    Event.builder()
                        .title("Festival Cultural da UEM")
                        .description("Celebração da diversidade cultural moçambicana com apresentações de dança, música tradicional, teatro e gastronomia.")
                        .eventDate(LocalDateTime.now().plusDays(25))
                        .location("Campus Principal - Praça Central")
                        .organizer("Associação de Estudantes da UEM")
                        .category(EventCategory.CULTURAL)
                        .maxCapacity(300)
                        .currentRegistrations(145)
                        .status(EventStatus.UPCOMING)
                        .imageUrl("https://images.unsplash.com/photo-1533174072545-7a4b6ad7a6c3?w=800")
                        .tags(Arrays.asList("Cultura", "Arte", "Música", "Dança"))
                        .build(),
                        
                    Event.builder()
                        .title("Torneio de Futebol Inter-Faculdades")
                        .description("Competição desportiva anual entre as diversas faculdades da UEM. Junte-se para apoiar a sua faculdade!")
                        .eventDate(LocalDateTime.now().plusDays(12))
                        .location("Campo de Futebol da UEM")
                        .organizer("Departamento de Desportos")
                        .category(EventCategory.SPORTS)
                        .maxCapacity(250)
                        .currentRegistrations(180)
                        .status(EventStatus.UPCOMING)
                        .imageUrl("https://images.unsplash.com/photo-1574629810360-7efbbe195018?w=800")
                        .tags(Arrays.asList("Futebol", "Desporto", "Competição"))
                        .build(),
                        
                    Event.builder()
                        .title("Palestra: Empreendedorismo em Moçambique")
                        .description("Empresários de sucesso partilham as suas experiências e conselhos para jovens empreendedores moçambicanos.")
                        .eventDate(LocalDateTime.now().plusDays(18))
                        .location("Sala de Conferências - Edifício Principal")
                        .organizer("Incubadora de Empresas da UEM")
                        .category(EventCategory.CONFERENCE)
                        .maxCapacity(80)
                        .currentRegistrations(52)
                        .status(EventStatus.UPCOMING)
                        .imageUrl("https://images.unsplash.com/photo-1475721027785-f74eccf877e2?w=800")
                        .tags(Arrays.asList("Empreendedorismo", "Negócios", "Startups"))
                        .build()
                );
                
                eventRepository.saveAll(sampleEvents);
                log.info("✅ {} eventos de exemplo criados com sucesso!", sampleEvents.size());
            } else {
                log.info("Base de dados já contém eventos. Pulando inicialização.");
            }
        };
    }
}
