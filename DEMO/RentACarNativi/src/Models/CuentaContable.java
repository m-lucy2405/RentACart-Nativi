package Models;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author diego
 */
public class CuentaContable {
    
        private int id;
        private String nombre;
        private String tipo;
        private String codigo;

    public CuentaContable(int id, String nombre, String tipo, String codigo) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.codigo = codigo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    @Override
    public String toString() {
        return "CuentaContable{" + "id=" + id + ", nombre=" + nombre + ", tipo=" + tipo + ", codigo=" + codigo + '}';
    }
        
        
}
