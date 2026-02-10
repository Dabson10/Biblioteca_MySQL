package DAO;

import Interfaces.PrestamoDAO;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;

public class PrestamoDaoImpl extends Conexion implements PrestamoDAO {
    @Override
    public boolean setPrestamo(String prestamoID, String ejemplarID, String usuarioID,
                               LocalDate fecha_prestamo, LocalDate fecha_entrega) {
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
}
