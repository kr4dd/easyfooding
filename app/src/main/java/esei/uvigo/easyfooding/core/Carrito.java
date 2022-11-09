package esei.uvigo.easyfooding.core;

public class Carrito {
    private int codigoComida;
    private int cantidad;
    private String nombreUsuario;

    public Carrito(int codigoComida, int cantidad, String nombreUsuario)
    {
        this.codigoComida = codigoComida;
        this.cantidad = cantidad;
        this.nombreUsuario = nombreUsuario;
    }

    public int getCodigoComida() {
        return codigoComida;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setCodigoComida(int codigoComida) {
        this.codigoComida = codigoComida;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    @Override
    public String toString() {
        return "Carrito{" +
                "codigoComida=" + codigoComida +
                ", cantidad=" + cantidad +
                ", nombreUsuario='" + nombreUsuario + '\'' +
                '}';
    }

}
