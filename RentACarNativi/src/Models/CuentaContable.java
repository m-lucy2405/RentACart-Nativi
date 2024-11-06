package Models;

import Database.Conexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CuentaContable {
    // Atributos
    private int idCuenta;
    private String nombreCuenta;
    private String tipoCuenta;
    private String codigoCuenta;
    private double saldoInicial;
    private boolean estado;  // Activo o Inactivo

    // Constructor
    public CuentaContable(int idCuenta, String nombreCuenta, String tipoCuenta, String codigoCuenta, double saldoInicial, boolean estado) {
        this.idCuenta = idCuenta;
        this.nombreCuenta = nombreCuenta;
        this.tipoCuenta = tipoCuenta;
        this.codigoCuenta = codigoCuenta;
        this.saldoInicial = saldoInicial;
        this.estado = estado;
    }

    public CuentaContable(String nombreCuenta, String tipoCuenta, String codigoCuenta, double saldoInicial, boolean estado) {
        this.nombreCuenta = nombreCuenta;
    this.tipoCuenta = tipoCuenta;
    this.codigoCuenta = codigoCuenta;
    this.saldoInicial = saldoInicial;
    this.estado = estado;
    }

    // Getters y Setters
    public int getIdCuenta() { return idCuenta; }
    public void setIdCuenta(int idCuenta) { this.idCuenta = idCuenta; }

    public String getNombreCuenta() { return nombreCuenta; }
    public void setNombreCuenta(String nombreCuenta) { this.nombreCuenta = nombreCuenta; }

    public String getTipoCuenta() { return tipoCuenta; }
    public void setTipoCuenta(String tipoCuenta) { this.tipoCuenta = tipoCuenta; }

    public String getCodigoCuenta() { return codigoCuenta; }
    public void setCodigoCuenta(String codigoCuenta) { this.codigoCuenta = codigoCuenta; }

    public double getSaldoInicial() { return saldoInicial; }
    public void setSaldoInicial(double saldoInicial) { this.saldoInicial = saldoInicial; }

    public boolean isEstado() { return estado; }
    public void setEstado(boolean estado) { this.estado = estado; }

    // Método para obtener todas las cuentas contables
    public static List<CuentaContable> obtenerCuentas() {
        List<CuentaContable> cuentas = new ArrayList<>();
        String sql = "SELECT * FROM cuentas_contables";
        
        try (Connection conn = Conexion.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
             
            while (rs.next()) {
                int idCuenta = rs.getInt("id_cuenta");
                String nombre = rs.getString("nombre_cuenta");
                String tipo = rs.getString("tipo_cuenta");
                String codigo = rs.getString("codigo_cuenta");
                double saldo = rs.getDouble("saldo_inicial");
                boolean estado = rs.getBoolean("estado");

                cuentas.add(new CuentaContable(idCuenta, nombre, tipo, codigo, saldo, estado));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cuentas;
    }

    // Método para guardar una nueva cuenta
    public void guardar() {
        String sql = "INSERT INTO cuentas_contables (nombre_cuenta, tipo_cuenta, codigo_cuenta, saldo_inicial, estado) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = Conexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, getNombreCuenta());
            stmt.setString(2, getTipoCuenta());
            stmt.setString(3, getCodigoCuenta());
            stmt.setDouble(4, getSaldoInicial());
            stmt.setBoolean(5, isEstado());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para actualizar una cuenta
   public void actualizar() throws SQLException {
    String sql = "UPDATE cuentas_contables SET nombre_cuenta = ?, tipo_cuenta = ?, codigo_cuenta = ?, saldo_inicial = ?, estado = ? WHERE id_cuenta = ?";
    try (Connection conn = Conexion.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, nombreCuenta);
        stmt.setString(2, tipoCuenta);
        stmt.setString(3, codigoCuenta);
        stmt.setDouble(4, saldoInicial);
        stmt.setBoolean(5, estado);
        stmt.setInt(6, idCuenta);  // Usa el ID para identificar el registro a actualizar
        stmt.executeUpdate();
    }
}



    // Método para eliminar una cuenta
    public static boolean eliminarCuenta(int idCuenta) {
        String sql = "DELETE FROM cuentas_contables WHERE id_cuenta = ?";
        
        try (Connection conn = Conexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idCuenta);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}

