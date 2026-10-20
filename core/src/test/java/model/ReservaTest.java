package model;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import reserva.exception.*;

public class ReservaTest {

    private final UUID id = UUID.randomUUID();
    private final String emailContacto = "cliente@gmail.com";
    private final LocalDate fecha = LocalDate.of(2024, 6, 15);

    @Test
    void testReservaValida() {
        Reserva reserva = Reserva.instance(id, emailContacto, fecha, 4);

        assertEquals(id, reserva.getId());
        assertEquals(emailContacto, reserva.getEmailContacto());
        assertEquals(fecha, reserva.getFecha());
        assertEquals(4, reserva.getCantidadPersonas());
        assertEquals(EstadoReserva.ACTIVA, reserva.getEstado());
    }

    @Test
    void reservaNuevaComienzaActiva() {
        Reserva reserva = Reserva.instance(id, emailContacto, fecha, 4);
        assertEquals(EstadoReserva.ACTIVA, reserva.getEstado());
    }

    @Test
    void reservaSinId_LanzaException() {
        assertThrows(reservaSinIdLanzaLanzaException.class, () -> {
            Reserva.instance(null, emailContacto, fecha, 4);
        });
    }

    @Test
    void reservaSinFecha_LanzaException(){
        assertThrows(reservaSinFechaLanzaException.class, () -> {
            Reserva.instance(id, emailContacto, null, 4);
        });
    }
    @Test
    void reservaSinEmail_LanzaException(){
        assertThrows(reservaSinEmailLanzaException.class, () -> {
            Reserva.instance(id, null, fecha, 4);
        });
    }
    @Test
    void reservaConEmailVacio_LanzaException(){
        assertThrows(reservaSinEmailLanzaException.class, () -> {
            Reserva.instance(id, "", fecha, 4);
        });
    }

    @Test
    void reservaConCeroPersonas_LanzaException(){
        assertThrows(reservaConCantidadInvalidaLanzaException.class, () -> {
            Reserva.instance(id, emailContacto, fecha, 0);
        });
    }

    @Test
    void reservaConCantidadPersonasNegativa_LanzaException(){
        assertThrows(reservaConCantidadInvalidaLanzaException.class, () -> {
            Reserva.instance(id, emailContacto, fecha, -1);
        });
    }

    @Test
    void reservaActivaSeCancelaCorrectamente() {
        Reserva reserva = Reserva.instance(id, emailContacto, fecha, 4);
        reserva.cancelar();
        assertEquals(EstadoReserva.CANCELADA, reserva.getEstado());
    }

    @Test
    void reservaCanceladaNoSePuedeCancelarNuevamente() {
        Reserva reserva = Reserva.instance(id, emailContacto, fecha, 4);
        reserva.cancelar();
        assertThrows(reservaCanceladaNoSePuedeCancelarNuevamenteLanzaException.class, () -> {
            reserva.cancelar();
        });
    }

}
