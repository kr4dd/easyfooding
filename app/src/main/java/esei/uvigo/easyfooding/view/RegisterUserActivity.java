package esei.uvigo.easyfooding.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;
import java.util.regex.Pattern;

import esei.uvigo.easyfooding.core.OperationsUser;
import esei.uvigo.easyfooding.R;
import esei.uvigo.easyfooding.core.UsuarioRegistro;
import esei.uvigo.easyfooding.model.AccesoModelo;

public class RegisterUserActivity extends AppCompatActivity {
    private UsuarioRegistro ur;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        //Ocultar la barra con el titulo
        Objects.requireNonNull(getSupportActionBar()).hide();

        AccesoModelo db = new AccesoModelo(this);

        Button btnRegistrarse = findViewById(R.id.btnLogearse);
        btnRegistrarse.setOnClickListener(
                view -> {
                    if(registrarUsuario(db)) {
                        //Crear sesion de usuario
                        OperationsUser.setSession(this, ur.getUsuario());

                        //Mandar a inicio
                        goToInicio();
                    }
                });

    }

    public boolean registrarUsuario(AccesoModelo db) {
        //Recoger datos
        EditText editTextUsuario = findViewById(R.id.editTextUsuario);
        EditText editTextPass = findViewById(R.id.editTextPass);
        EditText editTextNombre_real = findViewById(R.id.editTextNombreReal);
        EditText editTextApellidos = findViewById(R.id.editTextApellidos);
        EditText editTextCorreo = findViewById(R.id.editTextCorreo);
        EditText editTextTlfno = findViewById(R.id.editTextTelefono);
        EditText editTextDireccion = findViewById(R.id.editTextDireccion);
        EditText editTextLocalidad = findViewById(R.id.editTextLocalidad);
        EditText editTextCp = findViewById(R.id.editTextCodigoPostal);

        ur = new UsuarioRegistro(editTextUsuario, editTextPass, editTextNombre_real,
                editTextApellidos, editTextCorreo, editTextTlfno, editTextDireccion,
                editTextLocalidad, editTextCp, OperationsUser.getActualDateSpanishStrFormat());

        //Validar datos
        if(!validateInputFields(ur))
        {
             return false;
        }

        //Insertar datos en DB
        return db.insertarUsuario(ur);
    }

    public void goToInicio() {
        finish();

        startActivity(new Intent(this, InicioActivity.class));
    }

    public boolean validateInputFields(UsuarioRegistro ur)
    {

        TextView errUsuario = findViewById(R.id.errUsuario);
        TextView errContrasena = findViewById(R.id.errContrasena);
        TextView errNombreReal = findViewById(R.id.errNombreReal);
        TextView errApellidos = findViewById(R.id.errApellidos);
        TextView errCorreo = findViewById(R.id.errCorreo);
        TextView errTelefono = findViewById(R.id.errTelefono);
        TextView errDireccion = findViewById(R.id.errDireccion);
        TextView errLocalidad = findViewById(R.id.errLocalidad);
        TextView errCodigoPostal = findViewById(R.id.errCodigoPostal);

        validarUsuario(ur.getUsuario());
        validarPass(ur.getPass());
        validarNombreReal(ur.getNombreReal());
        validarApellidos(ur.getApellidos());
        validarCorreo(ur.getCorreo());
        validarTlfno(ur.getTlfno());
        validarDireccion(ur.getDireccion());
        validarLocalidad(ur.getLocalidad());
        validarCodigoPostal(ur.getCp());

        return errUsuario.getVisibility() == View.GONE && errContrasena.getVisibility() == View.GONE
                && errNombreReal.getVisibility() == View.GONE && errApellidos.getVisibility() == View.GONE
                && errCorreo.getVisibility() == View.GONE && errTelefono.getVisibility() == View.GONE
                && errDireccion.getVisibility() == View.GONE && errLocalidad.getVisibility() == View.GONE
                && errCodigoPostal.getVisibility() == View.GONE;

    }

    public void validarUsuario(String input) {
        Pattern p = Pattern.compile("^[a-zA-Z0-9áéíóúÁÉÍÓÚ]{3,40}$");

        showErrMessagesForRegisterTxtViews(p, input, R.id.errUsuario);
    }

    public void validarPass(String input) {
        Pattern p = Pattern.compile("^[a-zA-Z0-9áéíóúÁÉÍÓÚ]{3,40}$");

        showErrMessagesForRegisterTxtViews(p, input, R.id.errContrasena);
    }

    public void validarNombreReal(String input) {
        Pattern p = Pattern.compile("^[a-zA-ZáéíóúÁÉÍÓÚ]{3,40}$");

        showErrMessagesForRegisterTxtViews(p, input, R.id.errNombreReal);
    }

    public void validarApellidos(String input) {
        Pattern p = Pattern.compile("^[a-zA-Z\\sáéíóúÁÉÍÓÚ]{2,60}$");

        showErrMessagesForRegisterTxtViews(p, input, R.id.errApellidos);
    }

    public void validarCorreo(String input) {
        Pattern p = Pattern.compile("^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$");

        showErrMessagesForRegisterTxtViews(p, input, R.id.errCorreo);
    }

    public void validarTlfno(String input) {
        Pattern p = Pattern.compile("^[0-9]{9}$");

        showErrMessagesForRegisterTxtViews(p, input, R.id.errTelefono);
    }

    public void validarDireccion(String input) {
        Pattern p = Pattern.compile("^[a-zA-Zº0-9áéíóúÁÉÍÓÚ,.\\s-]{4,60}$");

        showErrMessagesForRegisterTxtViews(p, input, R.id.errDireccion);
    }

    public void validarLocalidad(String input) {
        Pattern p = Pattern.compile("^[a-zA-ZáéíóúÁÉÍÓÚ\\s]{4,35}$");

        showErrMessagesForRegisterTxtViews(p, input, R.id.errLocalidad);
    }

    public void validarCodigoPostal(int input) {
        Pattern p = Pattern.compile("^[0-9]{5}$");

        showErrMessagesForRegisterTxtViews(p, Integer.toString(input), R.id.errCodigoPostal);

    }

    public void showErrMessagesForRegisterTxtViews(Pattern p, String input, int view) {
        TextView errMsg = findViewById(view);

        if(!p.matcher(input).matches()) {
            errMsg.setVisibility(View.VISIBLE); //Muestra el error

        } else {
            errMsg.setVisibility(View.GONE); //Hace que el error desaparezca
        }
    }

}