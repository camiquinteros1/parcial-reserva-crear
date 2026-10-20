package usecase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import reserva.exception.PersistenceException;
import reserva.exception.ReservationAlreadyExistsException;
import reserva.modelo.EstadoReserva;
import reserva.modelo.Reserva;
import reserva.output.ReservationRepository;
import reserva.usecase.CreateReservationUseCase;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CrearReservaUseCaseTest {

    private ReservationRepository repository;
    private CreateReservationUseCase useCase;

    private final UUID id = UUID.randomUUID();
    private final String emailContacto = "cliente@gmail.com";
    private final LocalDate fecha = LocalDate.of(2026, 11, 15);

    @BeforeEach
    void setUp() {
        repository = mock(ReservationRepository.class);
        useCase = new CreateReservationUseCase(repository);
    }

    @Test
    void crearReservaExitosamente() {

        when(repository.existsActiveReservation(emailContacto, fecha))
                .thenReturn(false);

        when(repository.save(any(Reserva.class)))
                .thenReturn(true);

        Reserva reserva = useCase.execute(id, emailContacto, fecha, 4);

        assertNotNull(reserva);
        assertEquals(id, reserva.getId());
        assertEquals(emailContacto, reserva.getEmailContacto());
        assertEquals(fecha, reserva.getFecha());
        assertEquals(4, reserva.getCantidadPersonas());
        assertEquals(EstadoReserva.ACTIVA, reserva.getEstado());

        verify(repository).existsActiveReservation(emailContacto, fecha);
        verify(repository).save(same(reserva));
    }

    @Test
    void reservaDuplicada_LanzaException() {

        when(repository.existsActiveReservation(emailContacto, fecha))
                .thenReturn(true);

        assertThrows(ReservationAlreadyExistsException.class, () -> {
            useCase.execute(id, emailContacto, fecha, 4);
        });

        verify(repository).existsActiveReservation(emailContacto, fecha);
        verify(repository, never()).save(any(Reserva.class));
    }

    @Test
    void falloAlGuardar_LanzaException() {

        when(repository.existsActiveReservation(emailContacto, fecha))
                .thenReturn(false);

        when(repository.save(any(Reserva.class)))
                .thenReturn(false);

        assertThrows(PersistenceException.class, () -> {
            useCase.execute(id, emailContacto, fecha, 4);
        });

        verify(repository).existsActiveReservation(emailContacto, fecha);
        verify(repository).save(any(Reserva.class));
    }
}