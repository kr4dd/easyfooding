package esei.uvigo.easyfooding.objetosCarrito;

public class ListaPedidos {
    private int num_pedido;
    private String nombreUsuario;
    private String fecha;
    private String direccion;
    private String localidad;
    private int codioPostal;
    private double importe;
    private String observaciones;

    public ListaPedidos(int num_pedido, String nombreUsuario, String fecha, String direccion, String localidad, int codioPostal, double importe, String observaciones) {
        this.num_pedido = num_pedido;
        this.nombreUsuario = nombreUsuario;
        this.fecha = fecha;
        this.direccion = direccion;
        this.localidad = localidad;
        this.codioPostal = codioPostal;
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
        return num_pedido;
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
        return codioPostal;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setNum_pedido(int num_pedido) {
        this.num_pedido = num_pedido;
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
        this.codioPostal = codioPostal;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
