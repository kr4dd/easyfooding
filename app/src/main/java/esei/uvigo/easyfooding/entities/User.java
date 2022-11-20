package esei.uvigo.easyfooding.entities;

public class User {
    private String nombre_usuario;
    private String nombre_real;
    private String apellidos;
    private String mail;
    private String telefono;
    private String direccion;
    private String localidad;
    private int codigo_postal;

    public User () {};

    public String getNombre_usuario() { return nombre_usuario; }

    public void setNombre_usuario(String nombre_usuario) { this.nombre_usuario = nombre_usuario; }

    public String getNombre_real() { return nombre_real; }

    public void setNombre_real(String nombre_real) { this.nombre_real = nombre_real; }

    public String getApellidos() { return apellidos; }

    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    public String getMail() { return mail; }

    public void setMail(String mail) { this.mail = mail; }

    public String getTelefono() { return telefono; }

    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getDireccion() { return direccion; }

    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getLocalidad() { return localidad; }

    public void setLocalidad(String localidad) { this.localidad = localidad; }

    public int getCodigo_postal() { return codigo_postal; }

    public void setCodigo_postal(int codigo_postal) { this.codigo_postal = codigo_postal; }
}
