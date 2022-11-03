package esei.uvigo.easyfooding.core;

public class LineaPedidos {
    private int num_linea;
    private int num_pedido;
    private int codigo_comida;
    private int cantidad;
    private double precio;

    public LineaPedidos(int num_linea, int num_pedido, int codigo_comida, int cantidad, double precio) {
        this.num_linea = num_linea;
        this.num_pedido = num_pedido;
        this.codigo_comida = codigo_comida;
        this.cantidad = cantidad;
        this.precio = precio;
    }

    public int getNum_linea() {
        return num_linea;
    }

    public void setNum_linea(int num_linea) {
        this.num_linea = num_linea;
    }

    public int getNum_pedido() {
        return num_pedido;
    }

    public void setNum_pedido(int num_pedido) {
        this.num_pedido = num_pedido;
    }

    public int getCodigo_comida() {
        return codigo_comida;
    }

    public void setCodigo_comida(int codigo_comida) {
        this.codigo_comida = codigo_comida;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    @Override
    public String toString() {
        return "LineaPedidos{" +
                "num_linea=" + num_linea +
                ", num_pedido=" + num_pedido +
                ", codigo_comida=" + codigo_comida +
                ", cantidad=" + cantidad +
                ", precio=" + precio +
                '}';
    }
}
