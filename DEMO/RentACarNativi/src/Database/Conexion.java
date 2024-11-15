/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Database;

/**
 *
 * @author diego
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

    // Configuración de la conexión a la base de datos
    private static final String URL = "jdbc:postgresql://localhost:5432/Db_RentACar_Nativi"; // Cambia la URL según tu configuración
    private static final String USER = "postgres"; // Usuario de la base de datos
    private static final String PASSWORD = "morataya123"; // Contraseña del usuario de la base de datos

    // Método estático para obtener la conexión a la base de datos
    public static Connection getConnection() throws SQLException {
        try {
            // Cargar el driver de PostgreSQL
            Class.forName("org.postgresql.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new SQLException("Driver de PostgreSQL no encontrado", e);
        }
    }
}



