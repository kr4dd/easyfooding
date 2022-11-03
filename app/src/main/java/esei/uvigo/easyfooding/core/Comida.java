package esei.uvigo.easyfooding.core;

import java.io.Serializable;

import esei.uvigo.easyfooding.R;

public class Comida implements Serializable {
    private String nombre;
    private int imagen;
    private double precio;
    private int cantidad;
    private int codigo;
    private double precioTotal;

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public Comida(String nombre, double precio, int cantidad, int codigo) {
        this.nombre = nombre;
        this.imagen = R.mipmap.logo;
        this.precio = precio;
        this.cantidad = cantidad;
        this.codigo = codigo;
    }
    public Comida(String nombre, double precio, int cantidad,int codigo, Double precioTotal) {
        this.nombre = nombre;
        this.imagen = R.mipmap.logo;
        this.precio = precio;
        this.cantidad = cantidad;
        this.codigo = codigo;
        this.precioTotal = precioTotal;
    }
    public Comida(){
    }
    public String getNombre() {
        return nombre;
    }

    public int getImagen() {
        return imagen;
    }

    public double getPrecio() {
        return precio;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setImagen(int imagen) {
        this.imagen = imagen;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getCodigo() {
        return codigo;
    }

    @Override
    public String toString() {
        return "Comida{" +
                "nombre='" + nombre + '\'' +
                ", imagen=" + imagen +
                ", precio=" + precio +
                ", cantidad=" + cantidad +
                ", codigo=" + codigo +
                ", precioTotal=" + precioTotal +
                '}';
    }
}