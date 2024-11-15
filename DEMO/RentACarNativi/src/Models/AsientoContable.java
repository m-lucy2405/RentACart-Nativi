package Models;

import Database.Conexion;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class AsientoContable {
    // Atributos
    private int idAsiento;
    private java.util.Date fechaAsiento; // Utilizamos java.util.Date
    private String descripcionAsiento;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    // Constructor que recibe un java.util.Date
    public AsientoContable(java.util.Date fecha, String descripcion) {
        this.fechaAsiento = fecha;
        this.descripcionAsiento = descripcion;
    }

    // Constructor que recibe la fecha como String y la convierte a java.util.Date
    public AsientoContable(String fechaAsientoStr, String descripcionAsiento) throws ParseException {
        this.fechaAsiento = dateFormat.parse(fechaAsientoStr); // Parseamos la fecha desde String
        this.descripcionAsiento = descripcionAsiento;
    }

    // Constructor que incluye idAsiento
    public AsientoContable(int idAsiento, java.util.Date fechaAsiento, String descripcionAsiento) {
        this.idAsiento = idAsiento;
        this.fechaAsiento = fechaAsiento;
        this.descripcionAsiento = descripcionAsiento;
    }

    // Getters y Setters
    public int getIdAsiento() { return idAsiento; }
    public void setIdAsiento(int idAsiento) { this.idAsiento = idAsiento; }

    // Obtener la fecha en formato String
    public String getFechaAsiento() {
        return dateFormat.format(fechaAsiento); // Devuelve la fecha como un String en el formato deseado
    }
    
    // Setear la fecha con validación de formato
    public void setFechaAsiento(String fechaAsiento) throws ParseException {
        dateFormat.setLenient(false);
        this.fechaAsiento = dateFormat.parse(fechaAsiento); // Usamos java.util.Date
    }

    public String getDescripcionAsiento() { return descripcionAsiento; }
    public void setDescripcionAsiento(String descripcionAsiento) { this.descripcionAsiento = descripcionAsiento; }

    // Método para obtener todos los asientos contables
    public static List<AsientoContable> obtenerAsientos() {
        List<AsientoContable> asientos = new ArrayList<>();
        String sql = "SELECT * FROM asientos_contables";

        try (Connection conn = Conexion.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int idAsiento = rs.getInt("id_asiento");
                java.util.Date fecha = rs.getDate("fecha_asiento"); // Obtenemos la fecha como java.util.Date
                String descripcion = rs.getString("descripcion_asiento");

                asientos.add(new AsientoContable(idAsiento, fecha, descripcion));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return asientos;
    }

    // Método para guardar el asiento contable en la base de datos
    public void guardar() {
        String sql = "INSERT INTO asientos_contables (fecha_asiento, descripcion_asiento) VALUES (?, ?)";
        try (Connection conn = Conexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            // Convertir java.util.Date a java.sql.Date al insertar
            stmt.setDate(1, new java.sql.Date(fechaAsiento.getTime())); // Convertir a java.sql.Date
            stmt.setString(2, descripcionAsiento);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para actualizar un asiento
    public void actualizar() throws SQLException {
        String sql = "UPDATE asientos_contables SET fecha_asiento = ?, descripcion_asiento = ? WHERE id_asiento = ?";

        try (Connection conn = Conexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            // Convertir java.util.Date a java.sql.Date al actualizar
            stmt.setDate(1, new java.sql.Date(fechaAsiento.getTime())); // Convertir a java.sql.Date
            stmt.setString(2, descripcionAsiento);
            stmt.setInt(3, idAsiento);
            stmt.executeUpdate();
        }
    }

    // Método para eliminar un asiento
    public static boolean eliminarAsiento(int idAsiento) {
        String sql = "DELETE FROM asientos_contables WHERE id_asiento = ?";

        try (Connection conn = Conexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idAsiento);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Método para obtener un asiento por ID
    public static AsientoContable obtenerAsientoPorId(int idAsiento) {
        AsientoContable asiento = null;
        String sql = "SELECT * FROM asientos_contables WHERE id_asiento = ?";

        try (Connection conn = Conexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idAsiento);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                java.util.Date fecha = rs.getDate("fecha_asiento"); // Obtener como java.util.Date
                String descripcion = rs.getString("descripcion_asiento");
                asiento = new AsientoContable(idAsiento, fecha, descripcion);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return asiento;
    }
}

