package com.nttdata.ms_tickets;

import com.nttdata.ms_tickets.api.TicketsApiDelegate;
import com.nttdata.ms_tickets.model.Ticket;
import com.nttdata.ms_tickets.model.TicketRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

// TODO 11 [COMPLETADO]: implementar el delegate como servicio Spring.
@Service
public class TicketApiDelegateImpl implements TicketsApiDelegate {

    private static final int HORAS_PRIORIDAD_ALTA = 4;
    private static final int HORAS_PRIORIDAD_MEDIA = 8;
    private static final int HORAS_PRIORIDAD_BAJA = 24;

    // TODO 12 [COMPLETADO]: mantener los tickets iniciales y la secuencia correlativa de ids.
    private final List<Ticket> tickets = new CopyOnWriteArrayList<>(List.of(
            new Ticket(1L, "No carga el reporte mensual",
                    Ticket.PrioridadEnum.ALTA, Ticket.EstadoEnum.ABIERTO).horasEstimadas(4),
            new Ticket(2L, "Error de ortografía en el menú",
                    Ticket.PrioridadEnum.BAJA, Ticket.EstadoEnum.ABIERTO).horasEstimadas(24),
            new Ticket(3L, "Lentitud al iniciar sesión",
                    Ticket.PrioridadEnum.MEDIA, Ticket.EstadoEnum.CERRADO).horasEstimadas(8)
    ));
    private final AtomicLong siguienteId = new AtomicLong(4L);

    // TODO 13 [COMPLETADO]: listar todos o filtrar por estado sin distinguir mayúsculas y minúsculas.
    @Override
    public ResponseEntity<List<Ticket>> listarTickets(String estado) {
        List<Ticket> resultado = estado == null
                ? List.copyOf(tickets)
                : tickets.stream()
                        .filter(ticket -> ticket.getEstado().name().equalsIgnoreCase(estado))
                        .toList();
        return ResponseEntity.ok(resultado);
    }

    // TODO 14-15 [COMPLETADO]: devolver 200 al encontrar el ticket y 404 si no existe.
    @Override
    public ResponseEntity<Ticket> obtenerTicket(Long id) {
        return tickets.stream()
                .filter(ticket -> ticket.getId().equals(id))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // TODO 16 [COMPLETADO]: validar los campos requeridos y la longitud del titulo (RN-01 y RN-02).
    // TODO 17 [COMPLETADO]: asignar el id correlativo y el estado inicial ABIERTO (RN-03).
    // TODO 18 [COMPLETADO]: calcular las horas estimadas segun la prioridad usando constantes (RN-04).
    // TODO 19 [COMPLETADO]: agregar el ticket a memoria y responder 201 con el recurso creado.
    // TODO 20 [COMPLETADO]: permitir consultar el ticket creado mediante obtenerTicket.
    @Override
    public ResponseEntity<Ticket> crearTicket(TicketRequest request) {
        if (request == null || request.getTitulo() == null || request.getPrioridad() == null) {
            return ResponseEntity.badRequest().build();
        }

        String titulo = request.getTitulo().trim();
        if (titulo.length() < 5 || titulo.length() > 100) {
            return ResponseEntity.badRequest().build();
        }

        int horasEstimadas = switch (request.getPrioridad()) {
            case ALTA -> HORAS_PRIORIDAD_ALTA;
            case MEDIA -> HORAS_PRIORIDAD_MEDIA;
            case BAJA -> HORAS_PRIORIDAD_BAJA;
        };

        Ticket ticket = new Ticket(
                siguienteId.getAndIncrement(),
                titulo,
                Ticket.PrioridadEnum.valueOf(request.getPrioridad().name()),
                Ticket.EstadoEnum.ABIERTO
        ).horasEstimadas(horasEstimadas);
        tickets.add(ticket);

        return ResponseEntity.status(201).body(ticket);
    }
}
