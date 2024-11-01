/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

import java.util.List;

/**
 *
 * @author diego
 */
public class BalanceComprobacion {
    
        private int id;
        private String periodo;
        private List<AsientoContable> asientos;

    public BalanceComprobacion(int id, String periodo, List<AsientoContable> asientos) {
        this.id = id;
        this.periodo = periodo;
        this.asientos = asientos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public List<AsientoContable> getAsientos() {
        return asientos;
    }

    public void setAsientos(List<AsientoContable> asientos) {
        this.asientos = asientos;
    }

    @Override
    public String toString() {
        return "BalanceComprobacion{" + "id=" + id + ", periodo=" + periodo + ", asientos=" + asientos + '}';
    }
        
        
        
}
