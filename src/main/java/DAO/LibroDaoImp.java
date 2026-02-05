package DAO;

import Entidades.Libro;
import Interfaces.LibroDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class LibroDaoImp extends Conexion implements LibroDAO {
    @Override
    public boolean setLibro(String ISBN, String titulo, String categoria, String autor, String prefijo_ejemplar) {
        boolean guardado = false;
        try{
            PreparedStatement ps;
            ResultSet rs ;
            this.conectarse();
            ps = this.conectar.prepareStatement("INSERT INTO biblioteca_mix.libros(ISBN, titulo, categoria, autor, prefijo_ejemplar) VALUES (?, ?, ?, ?, ?)");
            ps.setString(1, ISBN);
            ps.setString(2, titulo);
            ps.setString(3, categoria);
            ps.setString(4, autor);
            ps.setString(5, prefijo_ejemplar);
            int agregado = ps.executeUpdate();
            if(agregado > 0 ){
                //Si es mayor a cero entonces se guardó correctamente el Libro
                System.out.println("Se guardaron los datos correctamente.");
                guardado = true;
            }else{
                System.out.println("No se guardaron los datos.");
            }
        }catch(Exception e){
            System.out.println("Error del tipo: " + e.getMessage());
        }finally {
            this.cerrarConexion();
        }
        return guardado;
    }

    @Override
    public Libro obtenerLibro(String ISBN) {
        Libro libro = null;
        try{
            PreparedStatement ps;
            ResultSet rs;
            this.conectarse();
            ps = this.conectar.prepareStatement("SELECT * FROM biblioteca_mix.libros WHERE ISBN = ?;");
            ps.setString(1, ISBN);
            rs = ps.executeQuery();
            if(rs.next()){
                String titulo = rs.getString("titulo");
                String autorL =rs.getString("autor");
                String categoria = rs.getString("categoria");
                String prefijo = rs.getString("prefijo_ejemplar");
                libro = new Libro(ISBN, titulo, autorL, categoria, prefijo);
            }
        }catch(Exception e){
            System.out.println("Error del tipo: " + e.getMessage());
        }finally {
            this.cerrarConexion();
        }
        return libro;
    }

    @Override
    public List<Libro> listarPorAutor(String autor){
        List<Libro> libros = new ArrayList<>();
        Libro libro = null;
        try{
            this.conectarse();
            PreparedStatement ps;
            ResultSet rs;

            ps = this.conectar.prepareStatement("SELECT * FROM libros WHERE LOWER(autor) LIKE ?;");
            ps.setString(1, (autor.toLowerCase() + "%"));
            rs = ps.executeQuery();
            //Si se guardaron
            while(rs.next()){
                String ISBN = rs.getString("ISBN");
                String titulo = rs.getString("titulo");
                String autorL =rs.getString("autor");
                String categoria = rs.getString("categoria");
                String prefijo = rs.getString("prefijo_ejemplar");

                //Si encontró al menos un resultado entonces recorrerá el bucle
                libros.add(new Libro(ISBN, titulo,autorL, categoria, prefijo));
            }
        }catch(Exception e){
            System.out.println("Error del tipo(listarPorAutor): " + e.getMessage());
        }finally {
            this.cerrarConexion();
        }
        return libros;
    }

    @Override
    public void editarLibro() {

    }

    @Override
    public boolean validarISBN(String ISBN){
        boolean existe = false;
        try{
            PreparedStatement ps;
            ResultSet rs;
            this.conectarse();
            ps = this.conectar.prepareStatement("SELECT ISBN FROM biblioteca_mix.libros WHERE ISBN = ?;");
            ps.setString(1, ISBN);
            rs = ps.executeQuery();
            if(rs.next()){
                //Si procede entonces regresamos el valor.
                existe = true;
            }
        }catch(Exception e){
            System.out.println("Error del tipo(validarISBN): " + e.getMessage() );
        }finally {
            this.cerrarConexion();
        }
        return existe;
    }

}
