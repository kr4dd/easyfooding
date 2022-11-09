package esei.uvigo.easyfooding;

import android.widget.EditText;

public class UsuarioRegistro {
    EditText usuario;
    EditText pass;
    EditText nombreReal;
    EditText apellidos;
    EditText correo;
    EditText tlfno;
    EditText direccion;
    EditText localidad;
    EditText cp;

    String fechaAlta;


    public UsuarioRegistro(EditText usuario, EditText pass, EditText nombreReal, EditText apellidos,
                           EditText correo, EditText tlfno, EditText direccion, EditText localidad,
                           EditText cp, String fechaAlta)
    {
        this.usuario = usuario;
        this.pass = pass;
        this.nombreReal = nombreReal;
        this.apellidos = apellidos;
        this.correo = correo;
        this.tlfno = tlfno;
        this.direccion = direccion;
        this.localidad = localidad;
        this.cp = cp;
        this.fechaAlta = fechaAlta;
    }

    public String getUsuario() {
        return usuario.getText().toString();
    }

    public String getPass() {
        return pass.getText().toString();
    }

    public String getNombreReal() {
        return nombreReal.getText().toString();
    }


    public String getApellidos() {
        return apellidos.getText().toString();
    }


    public String getCorreo() {
        return correo.getText().toString();
    }


    public String getTlfno() {
        return tlfno.getText().toString();
    }


    public String getDireccion() {
        return direccion.getText().toString();
    }


    public String getLocalidad() {
        return localidad.getText().toString();
    }

    public String getFechaAlta() {
        return OperationsUser.getActualDateSpanishStrFormat();
    }

    public int getCp() {
        int toret;
        try {
            toret = Integer.parseInt(cp.getText().toString());

        } catch (NumberFormatException e) {
            toret = 00000;
        }
        return toret;
    }

}
