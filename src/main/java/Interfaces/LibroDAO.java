package Interfaces;

import Entidades.Libro;

import java.util.List;

public interface LibroDAO {
    public boolean setLibro(String ISBN, String titulo, String categoria, String autor, String prefijo_ejemplar);
    public Libro obtenerLibro(String ISBN);
    public List<Libro> listarPorAutor(String autor);
    public void editarLibro();
    public boolean validarISBN(String ISBN);
}
