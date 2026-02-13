<h2>BIBLIOTECA | MySQL - Java </h2>
<hr>
Este proyecto es un gestor de biblioteca 
en donde se manejan Usuario
(Bibliotecarios y Usuarios), Libros, Ejemplares y Préstamos.
<hr>
<h3>Base de datos </h3> 
Para crear la base de datos se dejó tanto el código 
de creación de la base de datos, como el diagrama 
Modelo Relacional.
<hr> 
<h3>Java</h3> 
Durante todo el código se manejó java en su versión <b>17</b>.
Uno de los puntos más importantes para que este código funcione es
en la utilización de clases, más que nada para simplificar y proteger los datos.
Se implemento un diagrama de clases para que el código no sea tan confuso y se 
entienda como se relacionaron las clases.<hr>
<h3>Separación de responsabilidades.</h3>
Para que la interfaz y la logica no se combinen buscamos separar responsabilidades
en clases <b>DAO</b> en donde se realizarón las consultas a base de datos para 
que solo se usen consultas necesarias.<hr> 

<h3>Tecnologías usadas</h4>
En este proyecto se utilizaron las siguientes tecnologías
<ol>
    <li>Java 17.0</li>
    <li>MySQL 8.0 o superior.</li>
    <li>JDBC</li>
    <li>Bcrypt</li>
    <li>DAO</li>
</ol>

<hr>
<h3>Funcionalidades</h3>
En este proyecto podemos manejar diferentes funcionalidades como:
<ol>
    <li>Usuarios y Bibliotecarios.
        <ol>
            <li>Usuarios.
                <ul>
                    <li>Buscar Usuarios.</li>
                    <li>Agregar Usuarios</li>
                </ul>
            </li>
            <li>Bibliotecarios.
                <ul>
                    <li>Buscar Bibliotecarios.</li>
                    <li>Agregar Bibliotecarios.</li>
                </ul>
            </li>
        </ol>
    </li>
    <li>Libros y Ejemplares
        <ol>
            <li>Libros.
                <ul>
                    <li>Obtener y mostrar Libros.</li>
                    <li>Agregar ma Libros.</li>
                </ul>
            </li>
            <li>Ejemplares.
                <ul>
                    <li>Obtener y mostrar Ejemplares.</li>
                    <li>Agregar más ejemplares.</li>
                    <li>Editar ejemplares.</li>
                </ul>
            </li>
        </ol>
    </li>
    <li>Prestamos
        <ul>
            <li>Buscar préstamo, por ID o por correo.</li>
            <li>Realizar un préstamo.</li>
            <li>Regresar un ejemplar.</li>
        </ul>
    </li>
</ol>

