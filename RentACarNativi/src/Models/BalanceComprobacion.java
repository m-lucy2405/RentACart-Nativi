package Models;

import Database.Conexion;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BalanceComprobacion {
    private int idBalance;
    private String codigoCuenta;
    private String nombreCuenta;
    private BigDecimal totalDebito;
    private BigDecimal totalCredito;
    private BigDecimal saldoFinal;
    private Date fechaGeneracion;

    // Constructor
    public BalanceComprobacion(int idBalance, String codigoCuenta, String nombreCuenta, BigDecimal totalDebito, 
                                BigDecimal totalCredito, BigDecimal saldoFinal, Date fechaGeneracion) {
        this.idBalance = idBalance;
        this.codigoCuenta = codigoCuenta;
        this.nombreCuenta = nombreCuenta;
        this.totalDebito = totalDebito;
        this.totalCredito = totalCredito;
        this.saldoFinal = saldoFinal;
        this.fechaGeneracion = fechaGeneracion;
    }

    private BalanceComprobacion() {
        
    }

    // Getters y setters

    public int getIdBalance() {
        return idBalance;
    }

    public void setIdBalance(int idBalance) {
        this.idBalance = idBalance;
    }
    
    
    public String getCodigoCuenta() {
        return codigoCuenta;
    }

    public void setCodigoCuenta(String codigoCuenta) {
        this.codigoCuenta = codigoCuenta;
    }

    public String getNombreCuenta() {
        return nombreCuenta;
    }

    public void setNombreCuenta(String nombreCuenta) {
        this.nombreCuenta = nombreCuenta;
    }

    public BigDecimal getTotalDebito() {
        return totalDebito;
    }

    public void setTotalDebito(BigDecimal totalDebito) {
        this.totalDebito = totalDebito;
    }

    public BigDecimal getTotalCredito() {
        return totalCredito;
    }

    public void setTotalCredito(BigDecimal totalCredito) {
        this.totalCredito = totalCredito;
    }

    public BigDecimal getSaldoFinal() {
        return saldoFinal;
    }

    public void setSaldoFinal(BigDecimal saldoFinal) {
        this.saldoFinal = saldoFinal;
    }

    public Date getFechaGeneracion() {
        return fechaGeneracion;
    }

    public void setFechaGeneracion(Date fechaGeneracion) {
        this.fechaGeneracion = fechaGeneracion;
    }

    public static List<BalanceComprobacion> generarBalanceComprobacion(Date fechaInicio, Date fechaFin) {
    String sql = "SELECT c.codigo_cuenta, c.nombre_cuenta, CURRENT_DATE AS fecha_generacion, " +
                 "SUM(d.monto_debito) AS total_debito, " +
                 "SUM(d.monto_credito) AS total_credito, " +
                 "SUM(d.monto_debito - d.monto_credito) AS saldo_final " +
                 "FROM detalle_asientos d " +
                 "INNER JOIN cuentas_contables c ON d.id_cuenta = c.id_cuenta " +
                 "INNER JOIN asientos_contables a ON d.id_asiento = a.id_asiento " +
                 "WHERE a.fecha_asiento BETWEEN ? AND ? " +
                 "GROUP BY c.codigo_cuenta, c.nombre_cuenta " +
                 "ORDER BY c.codigo_cuenta";

    List<BalanceComprobacion> balances = new ArrayList<>();

    try (Connection conn = Conexion.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        // Establece los valores de las fechas para el filtro
        stmt.setDate(1, new java.sql.Date(fechaInicio.getTime()));
        stmt.setDate(2, new java.sql.Date(fechaFin.getTime()));

        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            // Crea un objeto BalanceComprobacion por cada registro del resultado
            BalanceComprobacion balance = new BalanceComprobacion();
            balance.setCodigoCuenta(rs.getString("codigo_cuenta"));
            balance.setNombreCuenta(rs.getString("nombre_cuenta"));
            balance.setFechaGeneracion(rs.getDate("fecha_generacion"));
            balance.setTotalDebito(rs.getBigDecimal("total_debito"));
            balance.setTotalCredito(rs.getBigDecimal("total_credito"));
            balance.setSaldoFinal(rs.getBigDecimal("saldo_final"));

            balances.add(balance);
        }

        // Devuelve la lista de balances generados
        return balances;
    } catch (SQLException ex) {
        ex.printStackTrace();
        return balances; // Devuelve la lista, incluso si está vacía
    }
}



    @Override
    public String toString() {
        return "BalanceComprobacion{" +
                "idBalance=" + idBalance + '\'' +
               "codigoCuenta='" + codigoCuenta + '\'' +
               ", nombreCuenta='" + nombreCuenta + '\'' +
               ", totalDebito=" + totalDebito +
               ", totalCredito=" + totalCredito +
               ", saldoFinal=" + saldoFinal +
               ", fechaGeneracion=" + fechaGeneracion +
               '}';
    }
}

