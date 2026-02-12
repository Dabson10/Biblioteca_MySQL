package Interfaces;

import Entidades.PrestamoDao;

import java.util.List;

public interface PrestamoDAO {
    public boolean setPrestamo(String prestamoID, String ejemplarID, String usuarioID,
                               String fecha_prestamo, String fecha_entrega);
    public String ultimoPrestamo();

    public PrestamoDao obtenerPrestamo(String prestamoID);
    public List<PrestamoDao> usuarioPrestamos(String correo);
}
