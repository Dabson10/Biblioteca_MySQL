package DAO;

import Entidades.Libro;
import Interfaces.LibroDAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LibroDaoImp extends Conexion implements LibroDAO {
    @Override
    public void setLibro(String ISBN, String titulo, String categoria, String autor, String prefijo_ejemplar) {
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
            }else{
                System.out.println("No se guardaron los datos.");
            }
        }catch(Exception e){
            System.out.println("Error del tipo: " + e.getMessage());
        }
    }

    @Override
    public Libro obtenerLibro() {
        return null;
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
        }
        return existe;
    }

}
