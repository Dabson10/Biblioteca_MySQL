package Exceptions;

public class CorreoNoValido extends RuntimeException {
    public CorreoNoValido(String message) {
        super(message);
    }
}
