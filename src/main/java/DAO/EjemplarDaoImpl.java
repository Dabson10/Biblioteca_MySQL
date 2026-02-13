package DAO;

import Entidades.Ejemplar;
import Entidades.EjemplarDAO;
import Entidades.Libro;
import Interfaces.EjemplarDao;
import Interfaces.LibroDAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Esta clase sirve para realizar las consultas a base de datos, esta hereda de la clase
 * {@code Conexion} para establecer conexión al realizar consultas, e implementa de la clase
 * {@code EjemplarDao} en donde se declararon las funciones que se utilizaran para realizar las consultas.
 *
 */
public class EjemplarDaoImpl extends Conexion implements EjemplarDao {
    LibroDAO consultaLibro = new LibroDaoImp();

    /**
     *Esta función es simple sirve para crear un nuevo ejemplar
     * @param codigoEjemplar : Código identificador del ejemplar ejem: {@code "FISULO_0002"}
     * @param ISBN : El ISBN proviene del libro, para esto se necesita tener un libro existente.
     * @param ubicacion : En que parte de la biblioteca estará ubicado el ejemplar.
     * @param tipo : Será físico o Digital el libro.
     * @return Si la consulta se realizó regresara un valor true, si no un false
     */
    @Override
    public boolean setEjemplar(String codigoEjemplar, String ISBN, String ubicacion, String tipo) {
        boolean salida = false;
        try{
            PreparedStatement ps ;
            this.conectarse();
            ps = this.conectar.prepareStatement("INSERT INTO biblioteca_mix.ejemplar(codigo_ejemplar, codigo_libro, ubicacion, tipo) VALUE (?, ?, ?, ? )");
            ps.setString(1, codigoEjemplar);
            ps.setString(2, ISBN);
            ps.setString(3, ubicacion);
            ps.setString(4, tipo);

            int guardado = ps.executeUpdate();
            if(guardado > 0){
                //Si es mayor a 0 entonces significa que se guardaron los datos.
                //Como la función es de retorno entonces no mostramos mensaje, ya que
                // esto lo validamos en donde se llama la función.
                salida = true;
            }else{
                System.out.println("No se guardaron los datos.");
            }
        }catch (Exception e){
            System.out.println("Error del tipo(setEjemplar): " + e.getMessage());
        }finally {
            this.cerrarConexion();
        }
        return salida;
    }

    /**
     * Esta función servirá para buscar cuál es el último ejemplar, este es realmente necesario, ya que
     * si agregamos un nuevo libro se creará un ejemplar con posición 0 y si queremos agregar mas ejemplares
     * sobre los ya existentes tomará el que tiene el último número.
     * @param prefijo : El prefijo es el ID solamente tomando las letras antes del {@code "_"}.
     * @return Regresará un objeto tipo {@code EjemplarDAO}, este funcionará para no volver a escribir
     * los otros atributos y reciclar los ya guardados. Aunque tenemos que manejar bien los valores null
     * si es que no se encontró al usuario
     */
    @Override
    public EjemplarDAO ultimoEjemplar(String prefijo){
        EjemplarDAO ejemplar = null;
        String ultimoID = "";
        try{
            PreparedStatement ps;
            ResultSet rs;
            this.conectarse();
            ps = this.conectar.prepareStatement("SELECT codigo_ejemplar, codigo_libro, disponible, tipo, ubicacion FROM biblioteca_mix.ejemplar WHERE codigo_ejemplar LIKE ? ORDER BY codigo_ejemplar DESC LIMIT 1;");
            ps.setString(1, (prefijo + "%"));
            rs = ps.executeQuery();
            if(rs.next()){
                  ultimoID = rs.getString("codigo_ejemplar");
                  String ISBN = rs.getString("codigo_libro");
                  String ubicacion = rs.getString("ubicacion");
                  String tipo = rs.getString("tipo");
                  boolean disponible = rs.getBoolean("disponible");

                  ejemplar = new EjemplarDAO(ultimoID, ISBN, disponible, ubicacion, tipo);
            }
        }catch (Exception e){
            System.out.println("Error del tipo: " + e.getMessage());
        }finally {
            this.cerrarConexion();
        }
        return ejemplar;
    }

    /**
     * Esta función servirá para poder ver los datos fundamentales del ejemplar.
     * @param ID : En base al ID del ejemplar realizaremos la búsqueda en base de datos.
     * @return Regresará un valor del tipo {@code Ejemplar}, es necesario manejar los valores
     * null por si no se encuentra el ejemplar.
     */
    @Override
    public Ejemplar obtenerEjemplar(String ID){
        Ejemplar ejemplar = null;
        Libro libro;
        try{
            PreparedStatement ps;
            ResultSet rs;
            this.conectarse();
            ps = this.conectar.prepareStatement("SELECT * FROM biblioteca_mix.ejemplar WHERE codigo_ejemplar = ?;");
            ps.setString(1, ID);
            rs = ps.executeQuery();
            if(rs.next()) {
                String ISBN = rs.getString("codigo_libro");
                String ubicacion = rs.getString("ubicacion");
                String tipo = rs.getString("tipo");
                boolean disp = rs.getBoolean("disponible");
                //Para poder guardar el objeto Ejemplar es necesario tener un objeto tipo Libro
                libro = consultaLibro.obtenerLibro(ISBN);
                if(libro != null){
                    //Si el valor es diferente entonces realizamos el guardado de datos.
                    ejemplar = new Ejemplar(ID, libro, ubicacion, tipo);
                    ejemplar.setDisponible(disp);
                }

            }
        }catch (Exception e){
            System.out.println("Error del tipo: " + e.getMessage());
        }finally {
            this.cerrarConexion();
        }
        return ejemplar;
    }

    /**
     * Esta función es necesaria, obtener un ejemplar, esta función será el filtro para cuando queramos
     * obtener los datos del ejemplar, con esta sabremos si existe o no el ejemplar.
     * @param ID : Mediante el ID realizaremos la búsqueda de ejemplares.
     * @return Regresara un valor booleano en donde si encuentra el ejemplar regresa un {@code true}, si no un {@code false}
     */
    @Override
    public boolean existeEjemplar(String ID){
        boolean existe = false;
        try{
            PreparedStatement ps;
            ResultSet rs;
            this.conectarse();
            ps = this.conectar.prepareStatement("SELECT codigo_ejemplar FROM biblioteca_mix.ejemplar WHERE codigo_ejemplar = ?;");
            ps.setString(1, ID);
            rs = ps.executeQuery();
            //Si el usuario existe entonces regresamos un true;
            if(rs.next()){
                existe = true;
            }
            //Si el usuario no existe regresara un false;
        }catch (Exception e){
            System.out.println("Error del tipo: " + e.getMessage());
        }finally {
            this.cerrarConexion();
        }
        return existe;
    }

    /**
     * Esta función es por si se quiere realizar un cambio de ubicacion en los ejemplares.
     * @param ubicacion : Es el valor que se guardara en la base de datos.
     * @param ID : El filtro con el que se buscara el ejemplar
     * @return : Si se guardo entonces regresamos un true, si no un false
     */
    @Override
    public boolean editUbicacion(String ubicacion, String ID){
        boolean editado = false;
        try{
            PreparedStatement ps;
            this.conectarse();
            ps = this.conectar.prepareStatement("UPDATE biblioteca_mix.ejemplar SET ubicacion = ? WHERE codigo_ejemplar = ?");
            ps.setString(1, ubicacion);
            ps.setString(2, ID);
            int cambios = ps.executeUpdate();
            if(cambios > 0){
                //Si es mayor a cero entonces cambiamos el valor booleano
                editado = true;
            }
        }catch (Exception e){
            System.out.println("Error del tipo: " + e.getMessage());
        }finally {
            this.cerrarConexion();
        }
        return editado;
    }
    /**
     * Esta función es por si se quiere realizar un cambio del tipo del ejemplar.
     * @param tipo : Es el valor que se guardara en la base de datos.
     * @param ID : El filtro con el que se buscara el ejemplar
     * @return : Si se guardo entonces regresamos un true, si no un false
     */
    @Override
    public boolean editTipo(String tipo, String ID){
        boolean actualizado = false;
        try{
            PreparedStatement ps;
            this.conectarse();
            System.out.println("Valores " + tipo + " " + ID);
            ps = this.conectar.prepareStatement("UPDATE biblioteca_mix.ejemplar SET tipo = ? WHERE codigo_ejemplar = ?;");
            ps.setString(1, tipo);
            ps.setString(2, ID);
            int cambio = ps.executeUpdate();
            //Si se realizó el cambio entonces regresamos un valor booleano.
            if(cambio > 0){
                actualizado = true;
            }
        }catch (Exception e){
            System.out.println("Error del tipo: " + e.getMessage());
        }finally {
            this.cerrarConexion();
        }
        return actualizado;
    }

    /**
     * Este es sirve para saber qué tipo de libro es
     * @param ID : filtro para saber que libro se eligió.
     * @return : Regresara una cadena con el valor obtenido, Si lo encontró regresara un {@code Físico} o {@code Digital},
     * si no lo encontró entonces un {@code ""}
     */
    @Override
    public String obtenerTipo(String ID){
        String tipo = "";
        try{
            PreparedStatement ps;
            ResultSet rs;
            this.conectarse();
            ps = this.conectar.prepareStatement("SELECT tipo FROM biblioteca_mix.ejemplar WHERE codigo_ejemplar = ?;");
            ps.setString(1, ID);

            rs = ps.executeQuery();
            if(rs.next()){
                //Si se realiza la consulta entonces regresamos el tipo de libro
                tipo = rs.getString("tipo");
            }

        }catch (Exception e){
            System.out.println("Error del tipo: " + e.getMessage());
        }
        return tipo;
    }

    /**
     * Esta función solo invertirá el estado del ejemplar, si esta en {@code true} lo cambiara a
     * {@code false} y viceversa.
     * @param ejemplarID : Con esto buscaremos que ejemplar se cambiara.
     * @return Si se realiza el cambio regresara un {@code true}, si no un {@code false}
     */
    @Override
    public boolean cambiarEstado(String ejemplarID) {
        boolean actualizado = false;
        try{
            PreparedStatement ps;
            this.conectarse();
            ps = this.conectar.prepareStatement("UPDATE biblioteca_mix.ejemplar SET disponible = NOT disponible WHERE codigo_ejemplar = ?;");
            ps.setString(1, ejemplarID);
            int cambios = ps.executeUpdate();
            if(cambios > 0){
                actualizado = true;
            }
        }catch (Exception e ){
            System.out.println("Error del tipo: " + e.getMessage());
        }finally {
            this.cerrarConexion();
        }
        return actualizado;
    }

}
