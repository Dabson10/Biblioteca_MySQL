package Interfaces;

import java.time.LocalDate;

public interface PrestamoDAO {
    public boolean setPrestamo(String prestamoID, String ejemplarID, String usuarioID,
                               String fecha_prestamo, String fecha_entrega);
    public String ultimoPrestamo();
}
