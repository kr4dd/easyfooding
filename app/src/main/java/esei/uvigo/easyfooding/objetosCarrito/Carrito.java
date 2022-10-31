package esei.uvigo.easyfooding.objetosCarrito;

public class Carrito {
    int codigoComida;
    int cantidad;
    String nombreUsuario;

    public Carrito(int codigoComida, int cantidad, String nombreUsuario) {
        this.codigoComida = codigoComida;
        this.cantidad = cantidad;
        this.nombreUsuario = nombreUsuario;
    }

    public int getCodigoComida() {
        return codigoComida;
    }

    public void setCodigoComida(int codigoComida) {
        this.codigoComida = codigoComida;
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

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }
}