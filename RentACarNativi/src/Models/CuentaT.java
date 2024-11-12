package Models;

import Database.Conexion;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;

public class CuentaT {
    private int id;
    private int idCuenta;
    private String mes;
    private double totalDebitos;
    private double totalCreditos;
    private double saldoFinal;
    private Date fechaGeneracion;

    // Constructor
    public CuentaT(int id, int idCuenta, String mes, double totalDebitos, double totalCreditos, double saldoFinal, Date fechaGeneracion) {
        this.id = id;
        this.idCuenta = idCuenta;
        this.mes = mes;
        this.totalDebitos = totalDebitos;
        this.totalCreditos = totalCreditos;
        this.saldoFinal = saldoFinal;
        this.fechaGeneracion = fechaGeneracion;
    }

    private CuentaT() {
      
    }

    // Getters
    public int getId() { return id; }
    public int getIdCuenta() { return idCuenta; }
    public String getMes() { return mes; }
    public double getTotalDebitos() { return totalDebitos; }
    public double getTotalCreditos() { return totalCreditos; }
    public double getSaldoFinal() { return saldoFinal; }
    public Date getFechaGeneracion() { return fechaGeneracion; }
    
    //Setters
      public void setIdCuenta(int idCuenta) {
        this.idCuenta = idCuenta;
    }
      
      public void setTotalDebitos(double totalDebitos) {
        this.totalDebitos = totalDebitos;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }
      
      
      
      public void setTotalCreditos(double totalCreditos) {
        this.totalCreditos = totalCreditos;
    }
      
       public void setSaldoFinal(double saldoFinal) {
        this.saldoFinal = saldoFinal;
    }
       
        public void setFechaGeneracion(Date fechaGeneracion) {
        this.fechaGeneracion = fechaGeneracion;
    }

   

    public static void generarCuentasTemporales(java.sql.Date fechaInicio, java.sql.Date fechaFin, int idCuenta) {
    String sql = "INSERT INTO cuentas_t_mensuales_temporal (id_cuenta, fecha_asiento, descripcion_asiento, debito, credito) "
               + "SELECT d.id_cuenta, a.fecha_asiento, a.descripcion_asiento, "
               + "COALESCE(d.monto_debito, 0) AS debito, COALESCE(d.monto_credito, 0) AS credito "
               + "FROM detalle_asientos d "
               + "JOIN asientos_contables a ON d.id_asiento = a.id_asiento "
               + "WHERE d.id_cuenta = ? AND a.fecha_asiento BETWEEN ? AND ? "
               + "ORDER BY a.fecha_asiento;";
    
    try (Connection conn = Conexion.getConnection(); 
         PreparedStatement ps = conn.prepareStatement(sql)) {
        
        ps.setInt(1, idCuenta);
        ps.setDate(2, fechaInicio);
        ps.setDate(3, fechaFin);
        
        int filasInsertadas = ps.executeUpdate();
        System.out.println(filasInsertadas + " filas insertadas en cuentas_t_mensuales_temporal.");
    } catch (SQLException e) {
        System.out.println("Error al generar cuentas temporales: " + e.getMessage());
    }
}


    public static ArrayList<CuentaT> cargarCuentasTemporales(int idCuenta) {
    ArrayList<CuentaT> cuentasT = new ArrayList<>();
    String query = "SELECT * FROM cuentas_t_mensuales_temporal WHERE id_cuenta = ?";  // Suponiendo que el parámetro es id_cuenta

    try (Connection conn = Conexion.getConnection();
         PreparedStatement ps = conn.prepareStatement(query)) {

        // Establecer el parámetro de la consulta
        ps.setInt(1, idCuenta);

        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                CuentaT cuenta = new CuentaT();
                cuenta.setId(rs.getInt("id"));
                cuenta.setIdCuenta(rs.getInt("id_cuenta"));
                cuenta.setMes(rs.getString("mes"));
                cuenta.setTotalDebitos(rs.getDouble("total_debitos"));
                cuenta.setTotalCreditos(rs.getDouble("total_creditos"));
                cuenta.setSaldoFinal(rs.getDouble("saldo_final"));
                cuenta.setFechaGeneracion(rs.getDate("fecha_generacion"));
                cuentasT.add(cuenta);
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
        System.out.println("Error al obtener cuentas temporales de la base de datos.");
    }
    return cuentasT;
}


    
    
    public static void borrarCuentasTemporales(int idCuenta) {
    String sql = "DELETE FROM cuentas_t_mensuales_temporal WHERE id_cuenta = ?";
    
    try (Connection conn = Conexion.getConnection(); 
         PreparedStatement ps = conn.prepareStatement(sql)) {
        
        ps.setInt(1, idCuenta);
        int filasBorradas = ps.executeUpdate();
        System.out.println("Cuentas temporales borradas: " + filasBorradas);
    } catch (SQLException e) {
        System.out.println("Error al borrar cuentas temporales: " + e.getMessage());
    }
}
    
    public static ArrayList<CuentaContable> obtenerCuentas() {
    ArrayList<CuentaContable> cuentas = new ArrayList<>();
    String query = "SELECT id_cuenta FROM cuentas_contables";  // Ajusta esta consulta a la estructura de tu base de datos

    try (Connection con = Conexion.getConnection();
         PreparedStatement pst = con.prepareStatement(query);
         ResultSet rs = pst.executeQuery()) {

        // Iterar a través de los resultados de la consulta
        while (rs.next()) {
            int idCuenta = rs.getInt("id_cuenta");
            cuentas.add(new CuentaContable(idCuenta));  // Crear un objeto Cuenta con el id y agregarlo a la lista
        }

    } catch (SQLException e) {
        e.printStackTrace();  // Manejo de errores
    }

    return cuentas;
}


}


