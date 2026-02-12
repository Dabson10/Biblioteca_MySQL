package DAO;

import Entidades.PrestamoDao;
import Interfaces.PrestamoDAO;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PrestamoDaoImpl extends Conexion implements PrestamoDAO {
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
