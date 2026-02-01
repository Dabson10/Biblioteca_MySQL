package Servicios;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
*En esta clase se realizaran las diferentes funcionalidades de los usuarios como
*menus, obtener información, etc.
* */
public class UsuariosServicio {
    private Scanner sc = new Scanner(System.in);
    public void menuUsuarios(){
        try{
            System.out.print("""
                \nBienvenido a la seccion usuarios.
                ¿Qué accion deseas realizar?
                1.Acceder a Bibliotecarios.
                2.Acceder a Usuarios.
                Ingresa tu opción:\s""");
            int opcMenu = sc.nextInt();
        }catch(InputMismatchException tipo){
            System.out.println("Ingrese un valor correcto por favor");
        }

    }

}
