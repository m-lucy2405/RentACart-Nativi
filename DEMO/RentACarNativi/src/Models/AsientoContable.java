/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

import java.util.Date;

/**
 *
 * @author diego
 */
public class AsientoContable {
        private int id;
        private Date fecha;
        private String descripcion;
        private double monto;
        private CuentaContable cuenta;

    public AsientoContable(int id, Date fecha, String descripcion, double monto, CuentaContable cuenta) {
        this.id = id;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.monto = monto;
        this.cuenta = cuenta;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public CuentaContable getCuenta() {
        return cuenta;
    }

    public void setCuenta(CuentaContable cuenta) {
        this.cuenta = cuenta;
    }

    @Override
    public String toString() {
        return "AsientoContable{" + "id=" + id + ", fecha=" + fecha + ", descripcion=" + descripcion + ", monto=" + monto + ", cuenta=" + cuenta + '}';
    }
        
        

}
