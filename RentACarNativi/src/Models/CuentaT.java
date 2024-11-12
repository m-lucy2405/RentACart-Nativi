package Models;

import Database.Conexion;
import java.sql.*;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class CuentaT {
    
    private int id;
    private int idCuenta;
    private String mes;
    private double totalDebitos;
    private double totalCreditos;
    private double saldoFinal;
    private Date fechaGeneracion;

    // Constructor vacío
    public CuentaT() {
    }

    // Constructor con parámetros
    public CuentaT(int id, int idCuenta, String mes, double totalDebitos, double totalCreditos, double saldoFinal, Date fechaGeneracion) {
        this.id = id;
        this.idCuenta = idCuenta;
        this.mes = mes;
        this.totalDebitos = totalDebitos;
        this.totalCreditos = totalCreditos;
        this.saldoFinal = saldoFinal;
        this.fechaGeneracion = fechaGeneracion;
    }

    // Getters y setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdCuenta() {
        return idCuenta;
    }

    public void setIdCuenta(int idCuenta) {
        this.idCuenta = idCuenta;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public double getTotalDebitos() {
        return totalDebitos;
    }

    public void setTotalDebitos(double totalDebitos) {
        this.totalDebitos = totalDebitos;
    }

    public double getTotalCreditos() {
        return totalCreditos;
    }

    public void setTotalCreditos(double totalCreditos) {
        this.totalCreditos = totalCreditos;
    }

    public double getSaldoFinal() {
        return saldoFinal;
    }

    public void setSaldoFinal(double saldoFinal) {
        this.saldoFinal = saldoFinal;
    }

    public Date getFechaGeneracion() {
        return fechaGeneracion;
    }

    public void setFechaGeneracion(Date fechaGeneracion) {
        this.fechaGeneracion = fechaGeneracion;
    }

    // Método para borrar las cuentas T temporales
    public static void borrarCuentasTemporales() {
        String sql = "DELETE FROM cuentas_t_mensuales WHERE es_temporal = true";
        try (Connection conn = Conexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para generar y guardar las cuentas T temporales
    public static void generarCuentasTemporales(Date fechaInicio, Date fechaFin) {
        String sql = "INSERT INTO cuentas_t_mensuales (id_cuenta, mes, total_debitos, total_creditos, saldo_final, fecha_generacion, es_temporal) "
                     + "SELECT c.id_cuenta, a.mes, SUM(a.total_debitos), SUM(a.total_creditos), SUM(a.saldo_final), CURRENT_DATE, true "
                     + "FROM cuentas c "
                     + "JOIN asientos a ON c.id_cuenta = a.id_cuenta "
                     + "WHERE a.fecha_asiento BETWEEN ? AND ? "
                     + "GROUP BY c.id_cuenta, a.mes";
        
        try (Connection conn = Conexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            // Convertir fechas a SQL Date
            java.sql.Date sqlFechaInicio = new java.sql.Date(fechaInicio.getTime());
            java.sql.Date sqlFechaFin = new java.sql.Date(fechaFin.getTime());
            
            stmt.setDate(1, sqlFechaInicio);
            stmt.setDate(2, sqlFechaFin);
            
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Cuentas T generadas temporalmente.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al generar las cuentas T: " + e.getMessage());
        }
    }

    // Método para obtener las cuentas T temporales desde la base de datos
    public static ArrayList<CuentaT> obtenerCuentasTemporales() {
        ArrayList<CuentaT> cuentas = new ArrayList<>();
        String sql = "SELECT id_cuenta_tm, id_cuenta, mes, total_debitos, total_creditos, saldo_final, fecha_generacion "
                     + "FROM cuentas_t_mensuales WHERE es_temporal = true";
        
        try (Connection conn = Conexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                CuentaT cuenta = new CuentaT();
                cuenta.setId(rs.getInt("id_cuenta_tm"));
                cuenta.setIdCuenta(rs.getInt("id_cuenta"));
                cuenta.setMes(rs.getString("mes"));
                cuenta.setTotalDebitos(rs.getDouble("total_debitos"));
                cuenta.setTotalCreditos(rs.getDouble("total_creditos"));
                cuenta.setSaldoFinal(rs.getDouble("saldo_final"));
                cuenta.setFechaGeneracion(rs.getDate("fecha_generacion"));
                cuentas.add(cuenta);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cuentas;
    }
}

