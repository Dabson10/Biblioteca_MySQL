package Interfaces;

import Entidades.Ejemplar;
import Entidades.EjemplarDAO;

public interface EjemplarDao {
    public boolean setEjemplar(String codigoEjemplar, String ISBN, String ubicacion, String tipo);
    public EjemplarDAO ultimoEjemplar(String prefijo);
    public Ejemplar obtenerEjemplar(String ID);
    public String obtenerTipo(String ID);
    public boolean existeEjemplar(String ID);
    public boolean editUbicacion(String ubicacion, String ID);
    public boolean editTipo(String tipo, String ID);

}
