package Models;

import Database.Conexion;
import java.sql.*;
import java.util.ArrayList;

public class AsientoDetalle {
    private int idDetalleAsiento;
    private int idAsiento;
    private int idCuenta;
    private double montoDebito;
    private double montoCredito;

    // Constructores
    public AsientoDetalle(int idAsiento, int idCuenta, double montoDebito, double montoCredito) {
        this.idAsiento = idAsiento;
        this.idCuenta = idCuenta;
        this.montoDebito = montoDebito;
        this.montoCredito = montoCredito;
    }

    public AsientoDetalle(int idDetalleAsiento, int idAsiento, int idCuenta, double montoDebito, double montoCredito) {
        this.idDetalleAsiento = idDetalleAsiento;
        this.idAsiento = idAsiento;
        this.idCuenta = idCuenta;
        this.montoDebito = montoDebito;
        this.montoCredito = montoCredito;
    }

    // Getters y Setters
    public int getIdDetalleAsiento() { return idDetalleAsiento; }
    public int getIdAsiento() { return idAsiento; }
    public int getIdCuenta() { return idCuenta; }
    public double getMontoDebito() { return montoDebito; }
    public double getMontoCredito() { return montoCredito; }

    public void setIdDetalleAsiento(int idDetalleAsiento) { this.idDetalleAsiento = idDetalleAsiento; }
    public void setIdAsiento(int idAsiento) { this.idAsiento = idAsiento; }
    public void setIdCuenta(int idCuenta) { this.idCuenta = idCuenta; }
    public void setMontoDebito(double montoDebito) { this.montoDebito = montoDebito; }
    public void setMontoCredito(double montoCredito) { this.montoCredito = montoCredito; }

    // Método para guardar un nuevo detalle
    public boolean guardar() {
        String sql = "INSERT INTO DetalleAsientos (id_asiento, id_cuenta, monto_debito, monto_credito) VALUES (?, ?, ?, ?)";
        try (Connection conn = Conexion.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, this.idAsiento);
            ps.setInt(2, this.idCuenta);
            ps.setDouble(3, this.montoDebito);
            ps.setDouble(4, this.montoCredito);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Método para actualizar un detalle existente
    public boolean actualizar() {
        String sql = "UPDATE DetalleAsientos SET id_asiento = ?, id_cuenta = ?, monto_debito = ?, monto_credito = ? WHERE id_detalle_asiento = ?";
        try (Connection conn = Conexion.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, this.idAsiento);
            ps.setInt(2, this.idCuenta);
            ps.setDouble(3, this.montoDebito);
            ps.setDouble(4, this.montoCredito);
            ps.setInt(5, this.idDetalleAsiento);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Método para eliminar un detalle
    public static boolean eliminar(int id) {
        String sql = "DELETE FROM DetalleAsientos WHERE id_detalle_asiento = ?";
        try (Connection conn = Conexion.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Método para obtener todos los detalles
    public static ArrayList<AsientoDetalle> obtenerTodos() {
        ArrayList<AsientoDetalle> detalles = new ArrayList<>();
        String sql = "SELECT * FROM DetalleAsientos";
        try (Connection conn = Conexion.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int idDetalleAsiento = rs.getInt("id_detalle_asiento");
                int idAsiento = rs.getInt("id_asiento");
                int idCuenta = rs.getInt("id_cuenta");
                double montoDebito = rs.getDouble("monto_debito");
                double montoCredito = rs.getDouble("monto_credito");

                detalles.add(new AsientoDetalle(idDetalleAsiento, idAsiento, idCuenta, montoDebito, montoCredito));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return detalles;
    }

    // Método para obtener un detalle específico por ID
    public static AsientoDetalle obtenerPorId(int id) {
        String sql = "SELECT * FROM DetalleAsientos WHERE id_detalle_asiento = ?";
        try (Connection conn = Conexion.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int idAsiento = rs.getInt("id_asiento");
                int idCuenta = rs.getInt("id_cuenta");
                double montoDebito = rs.getDouble("monto_debito");
                double montoCredito = rs.getDouble("monto_credito");

                return new AsientoDetalle(id, idAsiento, idCuenta, montoDebito, montoCredito);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
