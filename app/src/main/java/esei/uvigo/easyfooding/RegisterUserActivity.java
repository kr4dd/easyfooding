package esei.uvigo.easyfooding;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;
import java.util.regex.Pattern;

import esei.uvigo.easyfooding.database.DatabaseAccess;

public class RegisterUserActivity extends AppCompatActivity {
    String usuario;

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
                        OperationsUserActivity.setSession(this, usuario);

                        //Mandar a inicio
                        goToInicio();
                    }
                });

    }

    public boolean registrarUsuario(DatabaseAccess db) {
        //Recoger datos
        EditText editTextUsuario = findViewById(R.id.editTextUsuario);
        usuario = editTextUsuario.getText().toString();

        EditText editTextPass = findViewById(R.id.editTextPass);
        String pass = editTextPass.getText().toString();

        EditText editTextNombre_real = findViewById(R.id.editTextNombreReal);
        String nombre_real = editTextNombre_real.getText().toString();

        EditText editTextApellidos = findViewById(R.id.editTextApellidos);
        String apellidos = editTextApellidos.getText().toString();

        EditText editTextCorreo = findViewById(R.id.editTextCorreo);
        String correo = editTextCorreo.getText().toString();

        EditText editTextTlfno = findViewById(R.id.editTextTelefono);
        String tlfno = editTextTlfno.getText().toString();

        EditText editTextDireccion = findViewById(R.id.editTextDireccion);
        String direccion = editTextDireccion.getText().toString();

        EditText editTextLocalidad = findViewById(R.id.editTextLocalidad);
        String localidad = editTextLocalidad.getText().toString();

        EditText editTextCp = findViewById(R.id.editTextCodigoPostal);
        int cp;
        try {
            cp = Integer.parseInt(editTextCp.getText().toString());

        } catch (NumberFormatException e) {
            cp = 00000;
        }

        // Sample "02/08/22";
        String fechaAlta = OperationsUserActivity.getActualDateSpanishStrFormat();


        //Validarlos
        if(!validateInputFields(usuario, pass, nombre_real, apellidos, correo, tlfno,
                direccion, localidad, cp))
        {
             return false;
        }

        //Insertar en DB
        db.open();
        boolean res = db.insertarUsuario(usuario, OperationsUserActivity.hashearMD5(pass), nombre_real, apellidos, correo, tlfno,
                direccion, localidad, cp, fechaAlta);
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
        Pattern p = Pattern.compile("^[a-zA-Z0-9]{3,40}$");

        showErrMessagesForRegisterTxtViews(p, input, R.id.errUsuario);
    }

    public void validarPass(String input) {
        Pattern p = Pattern.compile("^[a-zA-Z0-9]{3,40}$");

        showErrMessagesForRegisterTxtViews(p, input, R.id.errContrasena);
    }

    public void validarNombreReal(String input) {
        Pattern p = Pattern.compile("^[a-zA-Z]{3,40}$");

        showErrMessagesForRegisterTxtViews(p, input, R.id.errNombreReal);
    }

    public void validarApellidos(String input) {
        Pattern p = Pattern.compile("^[a-zA-Z\\s]{2,60}$");

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
        Pattern p = Pattern.compile("^[a-zA-Zº0-9,.\\s-]{4,60}$");

        showErrMessagesForRegisterTxtViews(p, input, R.id.errDireccion);
    }

    public void validarLocalidad(String input) {
        Pattern p = Pattern.compile("^[a-zA-Z\\s]{4,35}$");

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