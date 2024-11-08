package Models;

import Database.Conexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map.Entry;

public class AsientoDetalle {

    // Atributos de la clase
    private int idAsiento;
    private int idCuenta;
    private double montoDebito;
    private double montoCredito;

    // Constructor
    public AsientoDetalle(int idAsiento, int idCuenta, double montoDebito, double montoCredito) {
        this.idAsiento = idAsiento;
        this.idCuenta = idCuenta;
        this.montoDebito = montoDebito;
        this.montoCredito = montoCredito;
    }

    // Getters y Setters
    public int getIdAsiento() {
        return idAsiento;
    }

    public void setIdAsiento(int idAsiento) {
        this.idAsiento = idAsiento;
    }

    public int getIdCuenta() {
        return idCuenta;
    }

    public void setIdCuenta(int idCuenta) {
        this.idCuenta = idCuenta;
    }

    public double getMontoDebito() {
        return montoDebito;
    }

    public void setMontoDebito(double montoDebito) {
        this.montoDebito = montoDebito;
    }

    public double getMontoCredito() {
        return montoCredito;
    }

    public void setMontoCredito(double montoCredito) {
        this.montoCredito = montoCredito;
    }

    // Método CRUD - Crear (Insertar un nuevo detalle de asiento)
    public void guardar() {
        String sql = "INSERT INTO detalle_asientos (id_asiento, id_cuenta, monto_debito, monto_credito) VALUES (?, ?, ?, ?)";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, this.idAsiento);
            ps.setInt(2, this.idCuenta);
            ps.setDouble(3, this.montoDebito);
            ps.setDouble(4, this.montoCredito);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();  // Manejo de errores
        }
    }

    // Método CRUD - Leer (Obtener todos los registros de detalle de asientos)
    public static List<AsientoDetalle> obtenerDetalles() {
        List<AsientoDetalle> detalles = new ArrayList<>();
        String sql = "SELECT id_asiento, id_cuenta, monto_debito, monto_credito FROM detalle_asientos";  // Consulta SQL

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int idAsiento = rs.getInt("id_asiento");
                int idCuenta = rs.getInt("id_cuenta");
                double montoDebito = rs.getDouble("monto_debito");
                double montoCredito = rs.getDouble("monto_credito");
                
                // Agregar el detalle a la lista
                detalles.add(new AsientoDetalle(idAsiento, idCuenta, montoDebito, montoCredito));
            }
        } catch (SQLException e) {
            e.printStackTrace();  // Manejo de errores
        }
        return detalles;
    }

    // Método CRUD - Actualizar (Actualizar un detalle de asiento)
    public void actualizar() {
        String sql = "UPDATE detalle_asientos SET id_asiento = ?, id_cuenta = ?, monto_debito = ?, monto_credito = ? WHERE id_asiento = ? AND id_cuenta = ?";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, this.idAsiento);
            ps.setInt(2, this.idCuenta);
            ps.setDouble(3, this.montoDebito);
            ps.setDouble(4, this.montoCredito);
            ps.setInt(5, this.idAsiento);  // Usamos el ID para encontrar el registro a actualizar
            ps.setInt(6, this.idCuenta);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();  // Manejo de errores
        }
    }

    // Método CRUD - Eliminar (Eliminar un detalle de asiento)
    public void eliminar() {
        String sql = "DELETE FROM detalle_asientos WHERE id_asiento = ? AND id_cuenta = ?";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, this.idAsiento);
            ps.setInt(2, this.idCuenta);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();  // Manejo de errores
        }
    }
    
// Simplificación de método genérico para obtener nombres
    private static List<Entry<Integer, String>> obtenerNombres(String tabla, String idColumna, String nombreColumna) {
        List<Entry<Integer, String>> lista = new ArrayList<>();
        String sql = "SELECT " + idColumna + ", " + nombreColumna + " FROM " + tabla;

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new AbstractMap.SimpleEntry<>(rs.getInt(idColumna), rs.getString(nombreColumna)));
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Manejo de errores
        }
        return lista;
    }

    // Método específico para obtener asientos con nombres
    public static List<Entry<Integer, String>> obtenerAsientosConNombres() {
        return obtenerNombres("asientos_contables", "id_asiento", "descripcion_asiento");
    }

    // Método específico para obtener cuentas con nombres
    public static List<Entry<Integer, String>> obtenerCuentasConNombres() {
        return obtenerNombres("cuentas_contables", "id_cuenta", "nombre_cuenta");
    }

}

