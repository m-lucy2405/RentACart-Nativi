/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

/**
 *
 * @author diego
 */
public class AsientoDetalle {
    
       private int id;
       private int asientoId;
       private int cuentaId;
       private double montoDebe;
       private double montoHaber;

    public AsientoDetalle(int id, int asientoId, int cuentaId, double montoDebe, double montoHaber) {
        this.id = id;
        this.asientoId = asientoId;
        this.cuentaId = cuentaId;
        this.montoDebe = montoDebe;
        this.montoHaber = montoHaber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAsientoId() {
        return asientoId;
    }

    public void setAsientoId(int asientoId) {
        this.asientoId = asientoId;
    }

    public int getCuentaId() {
        return cuentaId;
    }

    public void setCuentaId(int cuentaId) {
        this.cuentaId = cuentaId;
    }

    public double getMontoDebe() {
        return montoDebe;
    }

    public void setMontoDebe(double montoDebe) {
        this.montoDebe = montoDebe;
    }

    public double getMontoHaber() {
        return montoHaber;
    }

    public void setMontoHaber(double montoHaber) {
        this.montoHaber = montoHaber;
    }

    @Override
    public String toString() {
        return "AsientoDetalle{" + "id=" + id + ", asientoId=" + asientoId + ", cuentaId=" + cuentaId + ", montoDebe=" + montoDebe + ", montoHaber=" + montoHaber + '}';
    }
       
       
}
