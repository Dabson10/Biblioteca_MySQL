package Interfaces;

import java.time.LocalDate;

public interface PrestamoDAO {
    public boolean setPrestamo(String prestamoID, String ejemplarID, String usuarioID,
                               LocalDate fecha_prestamo, LocalDate fecha_entrega);
    public String ultimoPrestamo();
}
