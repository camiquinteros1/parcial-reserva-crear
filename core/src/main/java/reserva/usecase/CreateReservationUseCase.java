package reserva.usecase;

import reserva.exception.PersistenceException;
import reserva.exception.ReservationAlreadyExistsException;
import reserva.modelo.Reserva;
import reserva.output.ReservationRepository;

import java.time.LocalDate;
import java.util.UUID;

public class CreateReservationUseCase {

    private final ReservationRepository repository;

    public CreateReservationUseCase(ReservationRepository repository) {
        this.repository = repository;
    }

    public Reserva execute(UUID id, String emailContacto,
                           LocalDate fecha, int cantidadPersonas) {

        Reserva reserva = Reserva.instance(
                id, emailContacto, fecha, cantidadPersonas
        );

        if (repository.existsActiveReservation(emailContacto, fecha)) {
            throw new ReservationAlreadyExistsException(
                    "Ya existe una reserva activa para ese correo y fecha."
            );
        }

        if (!repository.save(reserva)) {
            throw new PersistenceException(
                    "No se pudo guardar la reserva."
            );
        }

        return reserva;
    }
}