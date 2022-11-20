package esei.uvigo.easyfooding;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;
import java.util.regex.Pattern;

import esei.uvigo.easyfooding.database.DatabaseAccess;
import esei.uvigo.easyfooding.entities.Validators.UserValidator;

public class RegisterUserActivity extends AppCompatActivity {
    private UsuarioRegistro ur;
    private UserValidator validator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        //Ocultar la barra con el titulo
        Objects.requireNonNull(getSupportActionBar()).hide();

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);

        Button btnRegistrarse = findViewById(R.id.btnLogearse);
        btnRegistrarse.setOnClickListener(
                view -> {
                    if(registrarUsuario(databaseAccess)) {
                        //Crear sesion de usuario
                        OperationsUserActivity.setSession(this, ur.getUsuario());

                        //Mandar a inicio
                        goToInicio();
                    }
                });

    }

    public boolean registrarUsuario(DatabaseAccess db) {
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
                editTextLocalidad, editTextCp, OperationsUserActivity.getActualDateSpanishStrFormat());

        //Validar datos
        if(!validateInputFields(ur.getUsuario(),
                OperationsUserActivity.hashearMD5(ur.getPass()),
                ur.getNombreReal(), ur.getApellidos(),
                ur.getCorreo(), ur.getTlfno(),
                ur.getDireccion(), ur.getLocalidad(),
                ur.getCp()))
        {
             return false;
        }

        //Insertar datos en DB
        db.open();
        boolean res = db.insertarUsuario(ur.getUsuario(),
                                    OperationsUserActivity.hashearMD5(ur.getPass()),
                                    ur.getNombreReal(), ur.getApellidos(),
                                    ur.getCorreo(), ur.getTlfno(),
                                    ur.getDireccion(), ur.getLocalidad(),
                                    ur.getCp(), ur.getFechaAlta());
        db.close();

        return res;
    }

    public void goToInicio() {
        finish();

        startActivity(new Intent(this, InicioActivity.class));
    }

    public boolean validateInputFields(String usuario, String pass, String nombre_real,
                                       String apellidos, String correo, String tlfno,
                                       String direccion, String localidad, int codigoPostal)
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

        validarUsuario(usuario);
        validarPass(pass);
        validarNombreReal(nombre_real);
        validarApellidos(apellidos);
        validarCorreo(correo);
        validarTlfno(tlfno);
        validarDireccion(direccion);
        validarLocalidad(localidad);
        validarCodigoPostal(codigoPostal);

        return errUsuario.getVisibility() == View.GONE && errContrasena.getVisibility() == View.GONE
                && errNombreReal.getVisibility() == View.GONE && errApellidos.getVisibility() == View.GONE
                && errCorreo.getVisibility() == View.GONE && errTelefono.getVisibility() == View.GONE
                && errDireccion.getVisibility() == View.GONE && errLocalidad.getVisibility() == View.GONE
                && errCodigoPostal.getVisibility() == View.GONE;

    }

    public void validarUsuario(String input) {
        showErrMessagesForRegisterTxtViews(UserValidator.validarUsuario(input), R.id.errUsuario);
    }

    public void validarPass(String input) {
        showErrMessagesForRegisterTxtViews(UserValidator.validarPass(input), R.id.errContrasena);
    }

    public void validarNombreReal(String input) {
        showErrMessagesForRegisterTxtViews(UserValidator.validarNombreReal(input), R.id.errNombreReal);
    }

    public void validarApellidos(String input) {
        showErrMessagesForRegisterTxtViews(UserValidator.validarApellidos(input), R.id.errApellidos);
    }

    public void validarCorreo(String input) {
        showErrMessagesForRegisterTxtViews(UserValidator.validarCorreo(input), R.id.errCorreo);
    }

    public void validarTlfno(String input) {
        showErrMessagesForRegisterTxtViews(UserValidator.validarTlfno(input), R.id.errTelefono);
    }

    public void validarDireccion(String input) {
        showErrMessagesForRegisterTxtViews(UserValidator.validarDireccion(input), R.id.errDireccion);
    }

    public void validarLocalidad(String input) {
        showErrMessagesForRegisterTxtViews(UserValidator.validarLocalidad(input), R.id.errLocalidad);
    }

    public void validarCodigoPostal(int input) {
        showErrMessagesForRegisterTxtViews(UserValidator.validarCodigoPostal(input), R.id.errCodigoPostal);

    }

    public void showErrMessagesForRegisterTxtViews(boolean bool, int view) {
        TextView errMsg = findViewById(view);

        if (bool) {
            errMsg.setVisibility(View.VISIBLE);
        } else {
            errMsg.setVisibility(View.GONE);
        }
    }

}