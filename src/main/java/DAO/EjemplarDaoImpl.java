package DAO;

import Entidades.Ejemplar;
import Entidades.EjemplarDAO;
import Entidades.Libro;
import Interfaces.EjemplarDao;
import Interfaces.LibroDAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class EjemplarDaoImpl extends Conexion implements EjemplarDao {
    LibroDAO consultaLibro = new LibroDaoImp();

    @Override
    public boolean setEjemplar(String codigoEjemplar, String ISBN, String ubicacion, String tipo) {
        boolean salida = false;
        try{
            PreparedStatement ps ;
            ResultSet rs;
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
//                System.out.println("Se guardaron los datos correctamente.");
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

    @Override
    public EjemplarDAO ultimoEjemplar(String prefijo){
        EjemplarDAO ejemplar = null;
        String ultimoID = "";
        try{
            PreparedStatement ps;
            ResultSet rs;
            this.conectarse();
            ps = this.conectar.prepareStatement("SELECT codigo_ejemplar, codigo_libro, disponible FROM biblioteca_mix.ejemplar WHERE codigo_ejemplar LIKE ? ORDER BY codigo_ejemplar DESC LIMIT 1;");
            ps.setString(1, (prefijo + "%"));
            rs = ps.executeQuery();
            if(rs.next()){
                  ultimoID = rs.getString("codigo_ejemplar");
                  String ISBN = rs.getString("codigo_libro");
                  boolean disponible = rs.getBoolean("disponible");
                  ejemplar = new EjemplarDAO(ultimoID, ISBN, disponible);
            }
        }catch (Exception e){
            System.out.println("Error del tipo: " + e.getMessage());
        }finally {
            this.cerrarConexion();
        }
        return ejemplar;
    }

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

    @Override
    public String ultimoID(String prefijo){
        String ultimoID = "";
        try{
            PreparedStatement ps;
            ResultSet rs;
            this.conectarse();
            ps = this.conectar.prepareStatement("SELECT MAX(codigo_ejemplar) AS ultimoID FROM biblioteca_mix.ejemplar WHERE codigo_ejemplar LIKE ?;");
            ps.setString(1, (prefijo + "%"));
            rs = ps.executeQuery();
            if(rs.next()){
                ultimoID = rs.getString("ultimoID");
            }
        }catch (Exception e){
            System.out.println("Error del tipo: " + e.getMessage());
        }
        return ultimoID;
    }

}
