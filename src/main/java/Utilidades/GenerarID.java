package Utilidades;

import Exceptions.LimiteIDs;

public class GenerarID {
    public String generarID(String prefijo, int numero){
        String ID = "";
        try{
            validarCantidad(numero);
            String numeroFinal = "";
            if(numero < 10){
                //Si es menor a 10entonces agregamos solo 3 ceros y la cantidad
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
    public void validarCantidad(int cantidad){
        if(cantidad <= 0 || cantidad >= 1000){
            throw new LimiteIDs("Posicion del ID fuera del rango " + cantidad);
        }
    }
}
