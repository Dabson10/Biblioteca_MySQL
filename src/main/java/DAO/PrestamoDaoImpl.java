package DAO;

import Entidades.PrestamoDao;
import Interfaces.PrestamoDAO;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Esta clase sirve para realizar las consultas a base de datos, esta hereda de la clase
 * {@code Conexion} para establecer conexión al realizar consultas, e implementa de la clase
 * {@code PrestamoDAO} en donde se declararon las funciones que se utilizaran para realizar las consultas.
 *
 */
public class PrestamoDaoImpl extends Conexion implements PrestamoDAO {
    /**
     * Esta función sirve para guardar un prestamo nuevo.
     * @param prestamoID : ID del préstamo.
     * @param ejemplarID : ID del ejemplar prestado.
     * @param usuarioID : ID del usuario al que se le présto el ejemplar.
     * @param fecha_prestamo : La fecha en la que el usuario realizo el préstamo.
     * @param fecha_entrega : Será la fecha en la que se entregara el ejemplar, més o menos 2 semanas
     * @return : Si se realiza la consulta entonces regresara un {@code true}, si no un {@code false}
     */
    @Override
    public boolean setPrestamo(String prestamoID, String ejemplarID, String usuarioID,
                               String fecha_prestamo, String fecha_entrega) {
        boolean guardado = false;
        try{
            PreparedStatement ps;
            this.conectarse();
            ps = this.conectar.prepareStatement("INSERT INTO biblioteca_mix.prestamo(prestamo_ID, ejemplar_prestado, solicitado_por, fecha_prestamo, fecha_entrega )VALUES (?, ?, ?, ?, ?); ");
            ps.setString(1, prestamoID);
            ps.setString( 2,ejemplarID);
            ps.setString(3,usuarioID);
            ps.setDate(4, Date.valueOf(fecha_prestamo));
            ps.setDate( 5, Date.valueOf(fecha_entrega));
            int agregado = ps.executeUpdate();
            if(agregado > 0 ){
                //Si se agregó el valor entonces regresaremos un valor true;
                guardado = true;
            }

        }catch (Exception e){
            System.out.println("Error del tipo: " + e.getMessage());
//            System.out.println("Error del tipo: " + e.getLocalizedMessage());
            e.printStackTrace();
        }finally {
            this.cerrarConexion();
        }
        return guardado;
    }

    /**
     * Esta función servirá para saber cuál fue el último prestamo realizado, sabiendo esto podemos crear otro,
     * aunque en esta sección no se hace eso.
     * @return : Regresará el último ID o una cadena vacía.
     */
    @Override
    public String ultimoPrestamo() {
        String ID = "nada";
        try{
            PreparedStatement ps;
            ResultSet rs;
            this.conectarse();
            ps = this.conectar.prepareStatement("SELECT MAX(prestamo_ID) AS ultimoID  FROM biblioteca_mix.prestamo;");
            rs = ps.executeQuery();
            if(rs.next()){
                //Si se encontró
                ID = rs.getString("ultimoID");
            }
        }catch(Exception e){
            System.out.println("Error del tipo: " + e.getMessage());
        }finally {
            this.cerrarConexion();
        }
        return ID;
    }

    /**
     * Esta función es la más importante junto con {@link #usuarioPrestamos(String)} ya que ambas realizan una
     * búsqueda en la tabla de {@code Usuarios}, {@code Ejemplares} y {@code Libros} para asi poder obtener los datos
     * del prestamo.<br>
     * Pero en esta función solo se obtiene un prestamo.
     * @param prestamoID : Con el ID del préstamo realizaremos la búsqueda del libro, usuario, ejemplar.
     * @return Regresará un objeto del tipo PréstamoDao en el cual se guardaran valores fundamentales para mostrar.
     */
    @Override
    public PrestamoDao obtenerPrestamo(String prestamoID){
        PrestamoDao prestamo = null;
        try{
            PreparedStatement ps;
            ResultSet rs;
            this.conectarse();
            ps = this.conectar.prepareStatement("SELECT\n" +
                    "    biblioteca_mix.usuarios.personaID, nombres, apellidos, correo,\n" +
                    "    biblioteca_mix.libros.titulo, autor, categoria,\n" +
                    "    biblioteca_mix.ejemplar.codigo_ejemplar, fecha_prestamo, fecha_entrega, real_fecha_entrega\n" +
                    "FROM prestamo\n" +
                    "INNER JOIN biblioteca_mix.usuarios\n" +
                    "    ON biblioteca_mix.prestamo.solicitado_por = biblioteca_mix.usuarios.personaID\n" +
                    "INNER JOIN biblioteca_mix.ejemplar\n" +
                    "    ON biblioteca_mix.ejemplar.codigo_ejemplar = biblioteca_mix.prestamo.ejemplar_prestado\n" +
                    "INNER JOIN biblioteca_mix.libros\n" +
                    "        ON biblioteca_mix.libros.ISBN = biblioteca_mix.ejemplar.codigo_libro\n" +
                    "WHERE biblioteca_mix.prestamo.prestamo_ID = ? ;");
            ps.setString(1, prestamoID);
            rs = ps.executeQuery();
            if(rs.next()){
                //Si se realizó la consulta entonces guardamos los datos en el constructor creado
                String perID = rs.getString("personaID");
                String nombres = rs.getString("nombres");
                String apellidos = rs.getString("apellidos");
                String correo = rs.getString("correo");

                String libro = rs.getString("titulo");
                String autor = rs.getString("autor");
                String cate = rs.getString("categoria");
                String ejeID = rs.getString("codigo_ejemplar");
                String fe_prest = rs.getString("fecha_prestamo");
                Date fecha_entre = rs.getDate("fecha_entrega");
                Date entregadoEn = rs.getDate("real_fecha_entrega");
                prestamo = new PrestamoDao(perID, nombres, apellidos, correo, libro, autor, cate, ejeID, fe_prest, fecha_entre, entregadoEn);
            }
        }catch (Exception e){
            System.out.println("Error del tipo: " + e.getMessage());
        }finally {
            this.cerrarConexion();
        }
        return prestamo;
    }

    /**
     * Esta función es similar a {@link #obtenerPrestamo(String)} la unicadiferencia es que en esta función
     * buscamos mediante el correo electronico y obtenemos todos los préstamos realizados por el usuario
     * @param correo : Con el correo se realizará la búsqueda.
     * @return : Regresara una lista del tipo PrestamoDao, si no se encontro nada entonces se validara con un
     * {@code lista.isEmpty()}
     */
    @Override
    public List<PrestamoDao> usuarioPrestamos(String correo){
        List<PrestamoDao> lista = new ArrayList<>();
        try {
            PreparedStatement ps;
            ResultSet rs;
            this.conectarse();
            ps = this.conectar.prepareStatement("SELECT\n" +
                    "    biblioteca_mix.usuarios.personaID, nombres, apellidos, correo,\n" +
                    "    biblioteca_mix.libros.titulo, autor, categoria,\n" +
                    "    biblioteca_mix.ejemplar.codigo_ejemplar, fecha_prestamo, fecha_entrega, real_fecha_entrega\n" +
                    "FROM prestamo\n" +
                    "INNER JOIN biblioteca_mix.usuarios\n" +
                    "    ON biblioteca_mix.prestamo.solicitado_por = biblioteca_mix.usuarios.personaID\n" +
                    "INNER JOIN biblioteca_mix.ejemplar\n" +
                    "    ON biblioteca_mix.ejemplar.codigo_ejemplar = biblioteca_mix.prestamo.ejemplar_prestado\n" +
                    "INNER JOIN biblioteca_mix.libros\n" +
                    "        ON biblioteca_mix.libros.ISBN = biblioteca_mix.ejemplar.codigo_libro\n" +
                    "WHERE biblioteca_mix.usuarios.correo = ? ;");
            ps.setString(1, correo);
            rs = ps.executeQuery();
            while(rs.next()){
                //Si se realizó la consulta entonces guardamos los datos en el constructor creado
                String perID = rs.getString("personaID");
                String nombres = rs.getString("nombres");
                String apellidos = rs.getString("apellidos");
                String correoU = rs.getString("correo");

                String libro = rs.getString("titulo");
                String autor = rs.getString("autor");
                String cate = rs.getString("categoria");
                String ejeID = rs.getString("codigo_ejemplar");
                String fe_prest = rs.getString("fecha_prestamo");
                Date fecha_entre = rs.getDate("fecha_entrega");
                Date entregadoEn = rs.getDate("real_fecha_entrega");
                //Ahora guardamos la cantidad de objetos obtenidos en la consulta, en donde se
                //buscó mediante el correo del usuario.
                lista.add(new PrestamoDao(perID, nombres, apellidos, correoU, libro, autor, cate, ejeID, fe_prest, fecha_entre, entregadoEn));
            }
        }catch (Exception e){
            System.out.println("Error del tipo: " + e.getMessage());
        }finally {
            this.cerrarConexion();
        }
        return lista;
    }

    /**
     * En esta funcin se realiza el cambio de prestado a libro regresado por asi decirlo, ya que se cambia la fecha real
     * de entrega y el estatus del prestamo.
     * @param ID : Con este ID realizaremos la búsqueda del prestamo.
     * @param fechaEntrega : Ser la fecha de cuando se entregó el libro.
     * @return : Si se realizo la actualización entonces regresara un true.
     */
    @Override
    public boolean updatePrestamo(String ID, java.util.Date fechaEntrega){
        boolean actualizado = false;
        try{
            PreparedStatement ps;
            this.conectarse();
            ps = this.conectar.prepareStatement("UPDATE biblioteca_mix.prestamo SET real_fecha_entrega = ?, activo = NOT activo WHERE prestamo_ID = ?; ");
            ps.setDate(1, (Date) fechaEntrega);
            ps.setString(2, ID);
            int cambios = ps.executeUpdate();
            if(cambios > 0){
                //Si cambios es mayor a cero entonces regresamos un true;
                actualizado = true;
            }
        }catch (Exception e){
            System.out.println("Error del tipo: " + e.getMessage());
        }finally {
            this.cerrarConexion();
        }
        return actualizado;
    }

    @Override
    public boolean borrarPrestamo(String ID){
        boolean borrado = false;
        try{
            PreparedStatement ps;
            this.conectarse();
            ps = this.conectar.prepareStatement("DELETE FROM biblioteca_mix.prestamo WHERE prestamo_ID = ?;");
            ps.setString(1, ID);
            int eliminado = ps.executeUpdate();
            if(eliminado > 0 ){
                borrado = true;
            }
        }catch (Exception e){
            System.out.println("Error del tipo: " + e.getMessage());
        }finally {
            this.cerrarConexion();
        }
        return borrado;
    }

}
