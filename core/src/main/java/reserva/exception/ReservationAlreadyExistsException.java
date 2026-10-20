package reserva.exception;

public class ReservationAlreadyExistsException extends RuntimeException {

    public ReservationAlreadyExistsException(String mensaje) {
        super(mensaje);
    }
}