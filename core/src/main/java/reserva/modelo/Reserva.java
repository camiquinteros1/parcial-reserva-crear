package reserva.modelo;
import reserva.exception.*;
import java.time.LocalDate;
import java.util.UUID;

public class Reserva {

    private final UUID id;
    private final String emailContacto;
    private final LocalDate fecha;
    private final int cantidadPersonas;
    private EstadoReserva estado;

    private Reserva(UUID id, String emailContacto,
                    LocalDate fecha, int cantidadPersonas) {

        this.id = id;
        this.emailContacto = emailContacto;
        this.fecha = fecha;
        this.cantidadPersonas = cantidadPersonas;
        this.estado = EstadoReserva.ACTIVA;
    }

    public static Reserva instance(UUID id, String emailContacto,
                                   LocalDate fecha, int cantidadPersonas) {

        if (id == null) {
            throw new reservaSinIdLanzaLanzaException(
                    "La reserva no puede crearse sin id.");
        }

        if (fecha == null) {
            throw new reservaSinFechaLanzaException(
                    "La reserva no puede crearse sin fecha.");
        }

        if (emailContacto == null || emailContacto.isBlank()) {
            throw new reservaSinEmailLanzaException(
                    "La reserva no puede crearse sin email.");
        }

        if (cantidadPersonas <= 0) {
            throw new reservaConCantidadInvalidaLanzaException(
                    "La cantidad de personas debe ser mayor que cero.");
        }

        return new Reserva(id, emailContacto, fecha, cantidadPersonas);
    }

    public void cancelar() {
        if (estado == EstadoReserva.CANCELADA) {
            throw new ReservaYaCanceladaException("La reserva esta cancelada");
        }

        estado = EstadoReserva.CANCELADA;
    }

    public UUID getId() {
        return id;
    }

    public String getEmailContacto() {
        return emailContacto;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public int getCantidadPersonas() {
        return cantidadPersonas;
    }

    public EstadoReserva getEstado() {
        return estado;
    }
}