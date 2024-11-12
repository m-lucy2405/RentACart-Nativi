package Models;

import Database.Conexion;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class CuentaT {
    
     private int id;
    private int idCuenta;
    private String mes;
    private double totalDebitos;
    private double totalCreditos;
    private double saldoFinal;
    private Date fechaGeneracion;

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


public static void insertarCuentasTemporales(Date fechaInicio, Date fechaFin) {
    // Asegúrate de tener la conexión establecida
    String sql = "INSERT INTO cuentas_t_mensuales (id_cuenta_tm, id_cuenta, mes, total_debitos, total_creditos, saldo_final, fecha_generacion, es_temporal) "
               + "SELECT id_cuenta_tm, id_cuenta, mes, total_debitos, total_creditos, saldo_final, fecha_generacion, ? "
               + "FROM cuentas_t_mensuales "
               + "WHERE fecha_generacion BETWEEN ? AND ? AND es_temporal = false";

    try (Connection conn = Conexion.getConnection();
         PreparedStatement pst = conn.prepareStatement(sql)) {

        // Convertir las fechas a formato SQL
        java.sql.Date sqlFechaInicio = new java.sql.Date(fechaInicio.getTime());
        java.sql.Date sqlFechaFin = new java.sql.Date(fechaFin.getTime());

        // Establecer los parámetros de la consulta
        pst.setBoolean(1, true); // es_temporal = true
        pst.setDate(2, sqlFechaInicio); // fecha_inicio
        pst.setDate(3, sqlFechaFin); // fecha_fin

        // Ejecutar la consulta
        int rowsAffected = pst.executeUpdate();
        
        // Verifica si se insertaron registros
        if (rowsAffected > 0) {
            JOptionPane.showMessageDialog(null, "Datos temporales guardados correctamente");
        } else {
            JOptionPane.showMessageDialog(null, "No se insertaron registros");
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error al guardar los datos temporales: " + e.getMessage());
    }
}



    public static void cargarCuentasTemporales(JTable table) {
    // Limpiar la tabla antes de cargar nuevos datos
    DefaultTableModel model = (DefaultTableModel) table.getModel();
    model.setRowCount(0); // Limpiar la tabla antes de cargar nuevos registros

    String sql = "SELECT id_cuenta_tm, id_cuenta, mes, total_debitos, total_creditos, saldo_final, fecha_generacion "
               + "FROM cuentas_t_mensuales "
               + "WHERE es_temporal = true";

    try (Connection conn = Conexion.getConnection();
         PreparedStatement pst = conn.prepareStatement(sql);
         ResultSet rs = pst.executeQuery()) {

        // Iterar sobre los resultados y llenar la tabla
        while (rs.next()) {
            Object[] row = {
                rs.getInt("id_cuenta_tm"),
                rs.getInt("id_cuenta"),
                rs.getString("mes"),
                rs.getDouble("total_debitos"),
                rs.getDouble("total_creditos"),
                rs.getDouble("saldo_final"),
                rs.getDate("fecha_generacion")
            };
            model.addRow(row);
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error al cargar los datos: " + e.getMessage());
    }
}


}
