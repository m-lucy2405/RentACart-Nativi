/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

/**
 *
 * @author diego
 */
public class CuentaT {
    
         private int id;
         private int cuentaId;
         private double debe;
         private double haber;
         private String periodo;

    public CuentaT(int id, int cuentaId, double debe, double haber, String periodo) {
        this.id = id;
        this.cuentaId = cuentaId;
        this.debe = debe;
        this.haber = haber;
        this.periodo = periodo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCuentaId() {
        return cuentaId;
    }

    public void setCuentaId(int cuentaId) {
        this.cuentaId = cuentaId;
    }

    public double getDebe() {
        return debe;
    }

    public void setDebe(double debe) {
        this.debe = debe;
    }

    public double getHaber() {
        return haber;
    }

    public void setHaber(double haber) {
        this.haber = haber;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    @Override
    public String toString() {
        return "CuentaT{" + "id=" + id + ", cuentaId=" + cuentaId + ", debe=" + debe + ", haber=" + haber + ", periodo=" + periodo + '}';
    }
         
         
}
