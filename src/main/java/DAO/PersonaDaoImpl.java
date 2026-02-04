package DAO;

import Entidades.Usuarios.Bibliotecario;
import Entidades.Usuarios.Persona;
import Entidades.Usuarios.Usuario;
import Entidades.Usuarios.UsuarioDTO;
import Exceptions.UsuarioNoEncontrado;
import Interfaces.PersonaDAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PersonaDaoImpl extends Conexion implements PersonaDAO {
    /**
     * La siguiente función sirve para realizar una consulta del tipo INSERT en la tabla {@code usuarios} y los parámetros
     * son todos recibidos desde otra función.<br> <br>
     * @param personaID : ID del usuario.
     * @param nombres : nombre del usuario.
     * @param apellidos : Apellido del usuario.
     * @param correo : Correo del usuario.
     * @param clave : Clave del usuario.
     * @param rol : Rol del usuario
     * @param prestamo : Valor predefinido con respecto al rol del usuario, si es un {@code Bibliotecario} guarda un "No admite",
     *                 Si es un {@code "Usuario"} guarda un "Sin prestamos".
     */
    @Override
    public void agregarPersona(String personaID, String nombres, String apellidos, String correo, String clave, String rol, String prestamo) {
        try{
            PreparedStatement ps = null;
            this.conectarse();

            ps = this.conectar.prepareStatement("INSERT INTO biblioteca_mix.usuarios(personaID, nombres, apellidos, correo, clave, rol, prestamoID) VALUES(?, ?, ?, ?, ?, ?, ? );");
            ps.setString(1, personaID);
            ps.setString(2, nombres);
            ps.setString(3, apellidos);
            ps.setString(4, correo);
            ps.setString(5, clave);
            ps.setString(6, rol);
            ps.setString(7, prestamo);
            int agregado = ps.executeUpdate();
            if(agregado > 0 ){
                System.out.println("Datos guardados correctamente");
            }else{
                System.out.print("No se pudo guardar el usuario.");
            }
        }catch (Exception e){
            System.out.println("Error del tipo: " + e.getMessage());
        }finally {
            this.cerrarConexion();
        }
    }

    /**
     * Esta función realiza una búsqueda de un usuario mediante su ID y regresa un objeto de tipo persona
     * en el cual se instanciaran los datos mediante el rol del usuario a la clase correspondiente
     * @param ID : ID de usuario en cuestión. debe de llevar una estructura similar a esta {@code USU_0000} o
     *           {@code BIB_0000}
     * @return : Regresará un objeto del tipo persona ya sea con un valor ya sea de la clase {@code "Bibliotecario"},
     * {@code "Usuario"} o incluso un valor {@code null} por esto mismo tenemos que hacer una validacion desde la funcion
     * en la que se llama a esta consulta.
     */
    @Override
    public Persona obtenerPersona(String ID){
        Persona persona = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            this.conectarse();
             ps = this.conectar.prepareStatement("SELECT * FROM biblioteca_mix.usuarios WHERE personaID = ?;");
            ps.setString(1, ID);
            rs = ps.executeQuery();
            if(rs.next()){
                //Obtenemos los datos del usuario.
                String rol = rs.getString("rol");
                String personaID = rs.getString("personaID");
                String nombre = rs.getString("nombres");
                String apellidos = rs.getString("apellidos");
                String correo = rs.getString("correo");
                String prestamoID = rs.getString("prestamoID");
                //Filtramos según el rol con respecto a su objeto.
                switch(rol){
                    //Creacion del objeto persona para obtener los datos de este.
                    case "Bibliotecario" -> persona = new Bibliotecario(personaID, nombre, apellidos, correo);
                    case "Usuario" -> {
                        persona = new Usuario(personaID, nombre, apellidos, correo);
                        Usuario usu = (Usuario) persona;
                        //Si o si se guardara el ID del prestamo.
                        usu.setLibroPrestado(prestamoID);
                    }
                    default -> throw new UsuarioNoEncontrado("No se encontro el usuario");
                }
            }else{
                System.out.println("No se encontro al usuario");
            }

        }
        catch(UsuarioNoEncontrado usuarioNo){
            System.out.println("El usuario no exite o sus datos tiene un error.");
        }
        catch(Exception e){
            System.out.println("Error del tipo(Obtener persona): " + e.getMessage());
        }finally {
            this.cerrarConexion();
        }
        return persona;
    }

    @Override
    public void editarPersona(Persona datos, String nombre) {

    }

    /**
     * Esta función sirve para buscar algún correo con una similitud exacta.
      * @param correo : Valor con el cual se realizara la búsqueda.
     * @return : Regresará un valor del tipo boolean en donde si encuentra un valor exacto regresará un {@code true},
     *  y si no encuentra entonces un false.
     */
    @Override
    public boolean correoExistente(String correo){
        boolean existe = false;
        try{
            PreparedStatement ps = null;
            ResultSet rs = null;
            this.conectarse();
            ps = this.conectar.prepareStatement("SELECT correo FROM biblioteca_mix.usuarios WHERE correo = ?;");
            ps.setString(1, correo);
            rs = ps.executeQuery();
            if(rs.next()){
                //Si se encuentra un correo electronico entonces regresamos un false
                existe = true;
            }
        }catch(Exception e){
            System.out.println("Error del tipo: " + e.getMessage());
        }finally {
            this.cerrarConexion();
        }
        return existe;
    }

    /**
     * Esta función sirve para que realice una búsqueda mediante un prefijo, esto para obtener el último valor de la base de datos,
     * mediante el prefijo.
     *
     * @param prefijo : Normalmente, un ID tiene esta forma {@code USU_0000} o {@code BIB_0000}, por lo que buscara mediante
     *                las 3 primeras letras, el cual es el prefijo {@code USU} o {@code BIB}.
     * @return : Regresará el ultimo ID para asi poder agregar un nuevo usuario mediante este ID.
     */
    @Override
    public String obtenerUltimo(String prefijo){
        String ID = "nada";
        try{
            PreparedStatement ps = null;
            ResultSet rs = null;
            this.conectarse();
            ps = this.conectar.prepareStatement("SELECT MAX(personaID) AS ultimoID FROM biblioteca_mix.usuarios WHERE personaID LIKE ?;");
            ps.setString(1, (prefijo + "%"));
            rs = ps.executeQuery();
            if(rs.next()){
                //Si procede con la consulta entonces regresará un valor.
                ID = rs.getString("ultimoID");

            }else{
                ID = "ninguno";
            }
        }catch(Exception e){
            System.out.println("Error del tipo: " + e.getMessage());
        }finally {
            this.cerrarConexion();
        }

        return ID;
    }


    @Override
    public UsuarioDTO validarCredenciales(String correoIn){
        UsuarioDTO usuario = null;
        try{
            this.conectarse();
            PreparedStatement ps = null;
            ResultSet rs = null;
            ps = this.conectar.prepareStatement("SELECT correo, clave, rol FROM biblioteca_mix.usuarios WHERE correo = ? ;");
            ps.setString(1, correoIn);
            rs = ps.executeQuery();
            if(rs.next()){
                String correoBd = rs.getString("correo");
                String clave = rs.getString("clave");
                String rol = rs.getString("rol");
                usuario = new UsuarioDTO(correoBd, clave, rol);
            }else{
                System.out.println("No se encontró el usuario");
            }
        }catch(Exception e){
            System.out.println("Error del tipo(validarCredenciales): " + e.getMessage());
        }
        return usuario;
    }


}
