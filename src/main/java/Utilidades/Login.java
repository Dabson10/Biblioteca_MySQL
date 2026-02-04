package Utilidades;

import DAO.PersonaDaoImpl;
import Entidades.Usuarios.UsuarioDTO;
import Exceptions.CorreoNoValido;
import Interfaces.PersonaDAO;

import java.util.Scanner;

public class Login {

    Scanner sc = new Scanner(System.in);
    ValidarCorreo validarCorreo = new ValidarCorreo();
    PersonaDAO consultas = new PersonaDaoImpl();
    CompararClave compararClave = new CompararClave();


    /**
     * La siguiente función tendrá un bucle for el cual recorrerá solamente tres veces por si el usuario se equivoca al
     * ingresar su nombre.
     *
     * @return : Regresará un valor booleano este servirá para direccionar la funcionalidad con respecto al valor,
     *          si es un true entonces procede con agregar, etc, datos.
     */
    public boolean login(){
        boolean validar = false;
        for(int i = 0; i < 3; i++){

            validar = datos();
            //Si se encuentra un true entonces terminamos el bucle, si no entonces lo seguimos
            if(validar){
                System.out.println("Usuario validado. Ingresando.");
                break;
            }else{
                System.out.println("Ingreso incorrecto.\nEstas en el intento " + (i + 1));
            }
        }
        return validar;
    }

    /**
     * Esta función pedirá al usuario ingresar correo y contraseña. Contiene validaciones para redirigir el código a ciertas cosas <br>
     * La primera validación es para saber si el correo ingresado está bien escrito usando {@link #validarCorreo} <br>
     * La segunda validación proviene de una consulta a base de datos, en donde se obtendrán los datos del usuario como
     * {@code "correo"}, {@code "contraseña"} y  {@code "rol"} en base a esto se realizan dos validaciones con estos datos. <br>
     *  * La primera es validar que el objeto en donde según guardamos los datos tenga un valor diferente a null, para evitar {@code NPE} <br>
     *  * La segunda es validar que el rol del usuario ingresado no sea el de {@code "Usuario"}, ya que solamente los bibliotecarios
     *  podrás agregar nuevos usuarios y bibliotecarios. <br>
     *  Por último solicitamos una contraseña y comparamos la contraseña ingresada con la guardada en la base de datos,
     *  solamente saber que la contraseña en base de datos tuvo que ser Hasheada para no guardarla en texto plano.
     *
     *
     * @return El valor que regrese servirá para que el bucle sepa si realizara una repetición más ({@code false})
     * o terminara de repetir({@code true})
     */
    public boolean datos(){
        boolean validar = false;
            try{
                //Agrega el null por si te sale error jeje
                UsuarioDTO usuario ;
                System.out.println("\nIngresa correo electrónico y contraseña para poder entrar.");

                System.out.print("Correo electrónico: ");
                String correo = sc.nextLine();

                //Validación para saber si el correo se escribió bien.
                if(!validarCorreo.validarCorreo(correo)){
                    //Si regresa un false entonces retornamos
                    return false;
                }

                //Ahora sí toca la validación de correo electrónico.
                usuario = consultas.validarCredenciales(correo);
                //Esta validación es la más importante, ya que si no la implementamos
                //quebrará el programa.
                if(usuario == null){
                    return false;
                }

                //Si el correo ingresado es un usuario entonces no procedemos.
                if(usuario.getRol().equals("Usuario")){
                    //Si es usuario regresamos
                    System.out.println("Ingrese un correo de un bibliotecario");
                    return false;
                }

                //Solicitamos la contraseña del usuario.
                System.out.print("Contraseña: ");
                String clave = sc.nextLine();


                //Ahora sí toca la validación de las claves del usuario.
                String claveHash = usuario.getClaveHash();

                if(compararClave.compararClave(clave, claveHash)){
                    //Si regresa un true es por que la clave es correcta.
                    validar = true;
                }else{
                    System.out.println("Contraseña incorrecta.\nIngresa la contraseña correcta.\n");
                }


            }catch(CorreoNoValido correo){
                System.out.println(correo.getMessage());
            }


        return validar;
    }
}
