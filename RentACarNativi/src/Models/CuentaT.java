package Models;

import Database.Conexion;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class CuentaT {
    
    private int idCuentaTMensual;
    private int idCuenta;
    private String mes;  // En formato dd/mm/yy
    private double totalDebitos;
    private double totalCreditos;
    private double saldoFinal;
    private java.sql.Date fechaGeneracion;  // Cambié a java.sql.Date
    private String nombreCuenta;
    private int idCuentaTm;

    // Constructor con String para fechaGeneracion
    public CuentaT(int idCuentaTMensual, int idCuenta, String mes, double totalDebitos, double totalCreditos, double saldoFinal, String fechaGeneracion) {
        this.idCuentaTMensual = idCuentaTMensual;
        this.idCuenta = idCuenta;
        this.mes = mes;
        this.totalDebitos = totalDebitos;
        this.totalCreditos = totalCreditos;
        this.saldoFinal = saldoFinal;
        try {
            // Convertir fechaGeneracion (String) a java.sql.Date
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            this.fechaGeneracion = new java.sql.Date(sdf.parse(fechaGeneracion).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    // Constructor con java.sql.Date para fechaGeneracion
    public CuentaT(int idCuentaTm, int idCuenta, double totalDebitos, double totalCreditos, double saldoFinal, java.sql.Date fechaGeneracion) {
        this.idCuentaTm = idCuentaTm;
        this.idCuenta = idCuenta;
        this.totalDebitos = totalDebitos;
        this.totalCreditos = totalCreditos;
        this.saldoFinal = saldoFinal;
        this.fechaGeneracion = fechaGeneracion;
    }
    
    public CuentaT() {
    // Constructor vacío (puedes inicializar los valores predeterminados si es necesario)
    this.idCuentaTMensual = 0;
    this.idCuenta = 0;
    this.mes = "";
    this.totalDebitos = 0.0;
    this.totalCreditos = 0.0;
    this.saldoFinal = 0.0;
    this.fechaGeneracion = new java.sql.Date(System.currentTimeMillis());
}

    // Getters y Setters
    public int getIdCuentaTMensual() {
        return idCuentaTMensual;
    }

    public void setIdCuentaTMensual(int idCuentaTMensual) {
        this.idCuentaTMensual = idCuentaTMensual;
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

    public java.sql.Date getFechaGeneracion() {
        return fechaGeneracion;
    }

    public void setFechaGeneracion(java.sql.Date fechaGeneracion) {
        this.fechaGeneracion = fechaGeneracion;
    }

    public String getNombreCuenta() {
        return nombreCuenta;
    }

    public void setNombreCuenta(String nombreCuenta) {
        this.nombreCuenta = nombreCuenta;
    }

    public int getIdCuentaTm() {
        return idCuentaTm;
    }

    public void setIdCuentaTm(int idCuentaTm) {
        this.idCuentaTm = idCuentaTm;
    }

 public void generarCuentasT(JTable tableCuentasT, String fechaInicio, String fechaFin) {
    DefaultTableModel model = new DefaultTableModel();
    model.addColumn("ID Cuenta TM");
    model.addColumn("ID Cuenta");
    model.addColumn("Mes");
    model.addColumn("Total Débitos");
    model.addColumn("Total Créditos");
    model.addColumn("Saldo Final");
    model.addColumn("Fecha de Generación");

    try {
        // Obtener la conexión a la base de datos
        Connection connection = Conexion.getConnection();

        // Convertir las fechas ingresadas en formato String a Date
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        java.util.Date startDate = sdf.parse(fechaInicio);
        java.util.Date endDate = sdf.parse(fechaFin);

        // Convertir a java.sql.Date
        java.sql.Date sqlStartDate = new java.sql.Date(startDate.getTime());
        java.sql.Date sqlEndDate = new java.sql.Date(endDate.getTime());

        // Imprimir las fechas para depuración
        System.out.println("Fecha de inicio: " + sqlStartDate);
        System.out.println("Fecha de fin: " + sqlEndDate);

        // Primero, guardar los registros temporales en la base de datos
        guardarDatosTemporales(connection, sqlStartDate, sqlEndDate);

        // Consulta SQL para obtener los datos de la tabla cuentas_t_mensuales
        String sql = "SELECT * FROM cuentas_t_mensuales WHERE fecha_generacion BETWEEN ? AND ? AND es_temporal = true ORDER BY id_cuenta_tm";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDate(1, sqlStartDate);  // Usar java.sql.Date
            ps.setDate(2, sqlEndDate);    // Usar java.sql.Date

            // Ejecutar la consulta
            try (ResultSet rs = ps.executeQuery()) {
                int rowCount = 0;
                while (rs.next()) {
                    Object[] row = new Object[7];
                    row[0] = rs.getInt("id_cuenta_tm");
                    row[1] = rs.getInt("id_cuenta");
                    row[2] = rs.getString("mes");
                    row[3] = rs.getDouble("total_debitos");
                    row[4] = rs.getDouble("total_creditos");
                    row[5] = rs.getDouble("saldo_final");
                    row[6] = rs.getDate("fecha_generacion");

                    model.addRow(row);
                    rowCount++;
                }

                if (rowCount == 0) {
                    System.out.println("No se encontraron resultados.");
                }
            }
        }

        // Establecer el modelo de la tabla
        tableCuentasT.setModel(model);
        tableCuentasT.repaint();  // Actualizar la vista de la tabla

    } catch (SQLException | ParseException e) {
        e.printStackTrace();
    }
}

private void guardarDatosTemporales(Connection connection, java.sql.Date startDate, java.sql.Date endDate) {
    try {
        String sqlInsert = "INSERT INTO cuentas_t_mensuales (id_cuenta_tm, id_cuenta, mes, total_debitos, total_creditos, saldo_final, fecha_generacion, es_temporal) "
                         + "SELECT id_cuenta_tm, id_cuenta, mes, total_debitos, total_creditos, saldo_final, fecha_generacion, ? "
                         + "FROM cuentas_t_mensuales "
                         + "WHERE fecha_generacion BETWEEN ? AND ? AND es_temporal = false";

        try (PreparedStatement ps = connection.prepareStatement(sqlInsert)) {
            ps.setBoolean(1, true);  // Marcar como temporal
            ps.setDate(2, startDate);
            ps.setDate(3, endDate);
            ps.executeUpdate();
        }

        System.out.println("Datos temporales guardados correctamente.");

    } catch (SQLException e) {
        e.printStackTrace();
    }
}


public void eliminarDatosTemporales() {
    try (Connection connection = Conexion.getConnection()) {
        String sql = "DELETE FROM cuentas_t_mensuales WHERE es_temporal = true";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.executeUpdate();
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}



}
