package Utilidades;

import Exceptions.LimiteIDs;

public class GenerarID {
    /**
     * Esta función sirve para generar un ID, esta función no solo lo genera, también válida
     * que no entren valores fuera del rango por ejemplo números negativos o que superen "10000"
     * @param prefijo : El prefijo será un identificado de área u objeto ya sea un bibliotecario como {@code BIB},
     *                un préstamo como {@code PREST}
     * @param numero : El número representa la cantidad de objetos o personas que hay, el ingresado debe ser un nuevo
     *               objeto o persona.
     * @return Regresará un ID compuesto del {@code Prefijo } + {@code _} + {@code numero} un ejemplo:
     * préstamo {@code "PREST_0024"}
     */
    public String generarID(String prefijo, int numero){
        String ID = "";
        try{
            validarCantidad(numero);
            String numeroFinal = "";
            if(numero < 10){
                //Si es menor a 10 entonces agregamos solo 3 ceros y la cantidad
                numeroFinal = "000" + numero;
            }else if(numero < 100){
                //Si es menor a 100 entonces agregamos solo 2 ceros y la cantidad
                numeroFinal = "00" + numero;
            }else if(numero < 1000){
                //Si es menor 1000 entonces agregamos solo 1 ceros y la cantidad
                numeroFinal = "0" + numero;
            }else if(numero < 10000){
                //Si es menor a 10,000 entonces agregamos la cantidad
                numeroFinal = String.valueOf(numero);
            }
            ID = prefijo + "_" + numeroFinal;
        }catch(LimiteIDs limiteIDs){
            System.out.println(limiteIDs.getMessage());
        }
        return ID;
    }

    /**
     * Esta función validá que no entren valores fuera del rango ya sean números negativos o superiores a
     * "10000".
     * @param cantidad :El número que se introducirá en el ID de la función de {@link #generarID(String, int)}
     */
    public void validarCantidad(int cantidad){
        if(cantidad <= 0 || cantidad >= 10000){
            throw new LimiteIDs("Posición del ID fuera del rango " + cantidad);
        }
    }
}
