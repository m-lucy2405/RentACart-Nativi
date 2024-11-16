package Models;

import Database.Conexion;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class CuentaT {
    private int idTemporal;
    private int idCuenta;
    private String nombreCuenta;
    private Date fechaAsiento;
    private String descripcionAsiento;
    private double debito;
    private double credito;
    private Date fechaGeneracion;
    private double saldoFinal;

    // Constructor vacío
    public CuentaT() {}

    // Constructor con parámetros
    public CuentaT(int idTemporal, int idCuenta,  String nombreCuenta, Date fechaAsiento, String descripcionAsiento, double debito, double credito, double saldoFinal, Date fechaGeneracion) {
        this.idTemporal = idTemporal;
        this.idCuenta = idCuenta;
        this.nombreCuenta = nombreCuenta;
        this.fechaAsiento = fechaAsiento;
        this.descripcionAsiento = descripcionAsiento;
        this.debito = debito;
        this.credito = credito;
        this.saldoFinal = saldoFinal;
        this.fechaGeneracion = fechaGeneracion;
    }

    // Getters y Setters
    public int getIdTemporal() { return idTemporal; }
    public void setIdTemporal(int idTemporal) { this.idTemporal = idTemporal; }

    public int getIdCuenta() { return idCuenta; }
    public void setIdCuenta(int idCuenta) { this.idCuenta = idCuenta; }

    public String getNombreCuenta() {return nombreCuenta;}
    public void setNombreCuenta(String nombreCuenta) {this.nombreCuenta = nombreCuenta;}

    public double getSaldoFinal() {return saldoFinal;}
    public void setSaldoFinal(double saldoFinal) {this.saldoFinal = saldoFinal;}
    
    public Date getFechaAsiento() { return fechaAsiento; }
    public void setFechaAsiento(Date fechaAsiento) { this.fechaAsiento = fechaAsiento; }

    public String getDescripcionAsiento() { return descripcionAsiento; }
    public void setDescripcionAsiento(String descripcionAsiento) { this.descripcionAsiento = descripcionAsiento; }

    public double getDebito() { return debito; }
    public void setDebito(double debito) { this.debito = debito; }

    public double getCredito() { return credito; }
    public void setCredito(double credito) { this.credito = credito; }

    public Date getFechaGeneracion() { return fechaGeneracion; }
    public void setFechaGeneracion(Date fechaGeneracion) { this.fechaGeneracion = fechaGeneracion; }

   
public static void generarCuentasTemporales(java.sql.Date fechaInicio, java.sql.Date fechaFin) {
    String sql = "INSERT INTO cuentas_t_mensuales_temporal (id_cuenta, nombre_cuenta, fecha_asiento, descripcion_asiento, debito, credito, saldo_final) "
               + "SELECT d.id_cuenta, c.nombre_cuenta, a.fecha_asiento, a.descripcion_asiento, "
               + "COALESCE(d.monto_debito, 0) AS debito, COALESCE(d.monto_credito, 0) AS credito, "
               + "(COALESCE(d.monto_debito, 0) - COALESCE(d.monto_credito, 0)) AS saldo_final "
               + "FROM detalle_asientos d "
               + "JOIN asientos_contables a ON d.id_asiento = a.id_asiento "
               + "JOIN cuentas_contables c ON d.id_cuenta = c.id_cuenta "
               + "WHERE a.fecha_asiento BETWEEN ? AND ? "
               + "ORDER BY a.fecha_asiento;";

    try (Connection conn = Conexion.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setDate(1, fechaInicio);
        ps.setDate(2, fechaFin);

        int filasInsertadas = ps.executeUpdate();
        System.out.println(filasInsertadas + " filas insertadas en cuentas_t_mensuales_temporal.");
    } catch (SQLException e) {
        System.out.println("Error al generar cuentas temporales: " + e.getMessage());
    }
}

   


public static ArrayList<CuentaT> cargarCuentasTemporales() {
    ArrayList<CuentaT> cuentasT = new ArrayList<>();
    String query = "SELECT * FROM cuentas_t_mensuales_temporal";  // Elimina el filtro

    try (Connection conn = Conexion.getConnection();
         PreparedStatement ps = conn.prepareStatement(query);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            CuentaT cuenta = new CuentaT();
            cuenta.setIdTemporal(rs.getInt("id_temporal"));
            cuenta.setIdCuenta(rs.getInt("id_cuenta"));
            cuenta.setNombreCuenta(rs.getString("nombre_cuenta"));
            cuenta.setFechaAsiento(rs.getDate("fecha_asiento"));
            cuenta.setDescripcionAsiento(rs.getString("descripcion_asiento"));
            cuenta.setDebito(rs.getDouble("debito"));
            cuenta.setCredito(rs.getDouble("credito"));
            cuenta.setSaldoFinal(rs.getDouble("saldo_final"));
            cuenta.setFechaGeneracion(rs.getDate("fecha_generacion"));
            cuentasT.add(cuenta);
        }
    } catch (SQLException e) {
        e.printStackTrace();
        System.out.println("Error al cargar cuentas temporales de la base de datos.");
    }

    // Mensaje de depuración para ver cuántas cuentas se cargaron
    System.out.println("Número de cuentas cargadas: " + cuentasT.size());

    return cuentasT;
}


    
    
   public static void borrarCuentasTemporales() {
    String sql = "DELETE FROM cuentas_t_mensuales_temporal";

    try (Connection conn = Conexion.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        int filasBorradas = ps.executeUpdate();
        System.out.println("Cuentas temporales borradas: " + filasBorradas);
    } catch (SQLException e) {
        System.out.println("Error al borrar cuentas temporales: " + e.getMessage());
    }
}
   
}


