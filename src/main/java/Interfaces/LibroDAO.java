package Interfaces;

import Entidades.Libro;

public interface LibroDAO {
    public void setLibro(String ISBN, String titulo, String categoria, String autor, String prefijo_ejemplar);
    public Libro obtenerLibro();
    public void editarLibro();
    public boolean validarISBN(String ISBN);
}
