package Entidades;

public class Libro {
    private String ISBN;//PK
    private String titulo;
    private String autor;
    private String categoria;
    private String prefijoEjemplar;
    //PrefijoEjemplar servira pra guardar el prefijo que tendrás los ejemplares relacionados al libro.

    public Libro(String ISBN, String titulo, String autor, String categoria, String prefijoEjemplar){
        this.ISBN = ISBN;
        this.titulo = titulo;
        this.autor = autor;
        this.categoria = categoria;
        this.prefijoEjemplar = prefijoEjemplar;
    }

    public String getPrefijoEjemplar(){
        return prefijoEjemplar;
    }

    public String mostrarDatos(){
        return "\nTitulo: " + titulo +
                "\nAutor: " + autor +
                "\nCategoria: " + categoria +
                "\nISBN: " + ISBN +
                "\nPrefijo: " + prefijoEjemplar;
    }

}
