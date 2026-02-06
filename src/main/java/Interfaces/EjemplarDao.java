package Interfaces;

import Entidades.Ejemplar;
import Entidades.EjemplarDAO;

public interface EjemplarDao {
    public boolean setEjemplar(String codigoEjemplar, String ISBN, String ubicacion, String tipo);
    public EjemplarDAO ultimoEjemplar(String prefijo);
    public Ejemplar obtenerEjemplar(String ID);
    public String ultimoID(String prefijo);
}
