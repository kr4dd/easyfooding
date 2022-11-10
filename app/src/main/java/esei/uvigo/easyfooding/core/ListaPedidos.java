package esei.uvigo.easyfooding.core;

public class ListaPedidos {
    private int numPedido;
    private String nombreUsuario;
    private String fecha;
    private String direccion;
    private String localidad;
    private int codigoPostal;
    private double importe;
    private String observaciones;

    public ListaPedidos(int numPedido, String nombreUsuario, String fecha, String direccion,
                        String localidad, int codigoPostal, double importe, String observaciones)
    {
        this.numPedido = numPedido;
        this.nombreUsuario = nombreUsuario;
        this.fecha = fecha;
        this.direccion = direccion;
        this.localidad = localidad;
        this.codigoPostal = codigoPostal;
        this.importe = importe;
        this.observaciones = observaciones;
    }

    public double getImporte() {
        return importe;
    }

    public void setImporte(double importe) {
        this.importe = importe;
    }

    public int getNum_pedido() {
        return numPedido;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public String getFecha() {
        return fecha;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getLocalidad() {
        return localidad;
    }

    public int getCodioPostal() {
        return codigoPostal;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setNum_pedido(int numPedido) {
        this.numPedido = numPedido;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public void setCodioPostal(int codioPostal) {
        this.codigoPostal = codioPostal;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    @Override
    public String toString() {
        return "ListaPedidos{" +
                "numPedido=" + numPedido +
                ", nombreUsuario='" + nombreUsuario + '\'' +
                ", fecha='" + fecha + '\'' +
                ", direccion='" + direccion + '\'' +
                ", localidad='" + localidad + '\'' +
                ", codioPostal=" + codigoPostal +
                ", importe=" + importe +
                ", observaciones='" + observaciones + '\'' +
                '}';
    }
}
