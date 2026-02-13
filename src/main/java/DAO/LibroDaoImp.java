package DAO;

import Entidades.Libro;
import Interfaces.LibroDAO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Esta clase sirve para realizar las consultas a base de datos, esta hereda de la clase
 * {@code Conexion} para establecer conexión al realizar consultas, e implementa de la clase
 * {@code LibroDao} en donde se declararon las funciones que se utilizaran para realizar las consultas.
 */
public class LibroDaoImp extends Conexion implements LibroDAO {
    /**
     * Esta función sirve para guardar un Libro en la base de datos.
     * @param ISBN : Numero identificador del Libro
     * @param titulo : Nombre del libro
     * @param categoria : Categoria del libro.
     * @param autor : Nombre del autor
     * @param prefijo_ejemplar : El prefijo es una combinación de {@code Categoria} {@code Autor} {@code Titulo} usando las
     *                         dos primeras letras de cada atributo.
     * @return Si la consulta se concreto regresa un {@code true}, si no un {@code false}
     */
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

    /**
     * Esta función sirve para obtener los datos del libro
     * @param ISBN : Medio por el cual buscamos el libro.
     * @return Si la consulta se concretó regresa el objeto, si no un null, por esto mismo es necesario saber manejar
     * el valor de regreso, ya que nos puede salir un  {@code NPE}
     */
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

    /**
     * Esta función sirve para listar los libros del autor.
     * @param autor : Con el nombre realizaremos la búsqueda de libros.
     * @return Regresará una lista con los libros del autor. Si no se encuentra al autor entonces regresará una
     * lista vacía.
     */
    @Override
    public List<Libro> listarPorAutor(String autor){
        List<Libro> libros = new ArrayList<>();
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

    /**
     * Esta función sirve para cuando se quiere agregar un nuevo libro válida que el nuevo libro no tenga el mismo
     * ISBN y asi no existan repetidos.
     * @param ISBN : Valor con el que realizaremos la búsqueda.
     * @return Si la consulta encontró un similar regresa un {@code true}, si no un {@code false}
     */
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
