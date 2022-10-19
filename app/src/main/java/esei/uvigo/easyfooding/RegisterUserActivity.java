package esei.uvigo.easyfooding;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import esei.uvigo.easyfooding.database.DatabaseAccess;

public class RegisterUserActivity extends AppCompatActivity {
    String usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);

        Button btnRegistrarse = findViewById(R.id.btnLogearse);
        btnRegistrarse.setOnClickListener(
                view -> {
                    if(registrarUsuario(databaseAccess)) {
                        //Crear sesion de usuario
                        SharedPreferences.Editor ed = getSharedPreferences("data",
                                Context.MODE_PRIVATE).edit();
                        ed.putString("nombre_usuario", usuario);
                        ed.apply();

                        //Mandar a inicio
                        goToInicio();
                    } else {
                        goToWelcomeScreen();
                    }

                    //TODO anadir mensajes de error en campos de validacion
                    //TODO reducir funciones compartidas con Login
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

        String fechaAlta = getDateIntoSpanishStringFormat(); // Sample "02/08/22";

        //Validarlos
         if(!validateInputFields(usuario, pass, nombre_real, apellidos, correo, tlfno,
                direccion, localidad, cp))
         {
             showToastMsg("Campos de registro invalidos");
             return false;
         }

        //Insertar en DB
        db.open();
        boolean res = db.insertarUsuario(usuario, hashearMD5(pass), nombre_real, apellidos, correo, tlfno,
                direccion, localidad, cp, fechaAlta);
        db.close();

        return res;
    }

    public void goToInicio() {
        showToastMsg("Registro correcto");

        finish();
        /*
        Intent intent = new Intent(this, InicioActivity.class);
        intent.putExtra("nombre_usuario", usuario);*/

        startActivity(new Intent(this, InicioActivity.class));
    }

    public void goToWelcomeScreen() {
        showToastMsg("Registro incorrecto");

        finish();
        /*
        Intent intent = new Intent(this, InicioActivity.class);
        intent.putExtra("nombre_usuario", usuario);*/

        startActivity(new Intent(this, WelcomeUserActivity.class));
    }

    public void showToastMsg(String msg) {
        Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
        toast.show();
    }

    public String getDateIntoSpanishStringFormat() {
        Calendar cal = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(cal.getTime());
    }

    public boolean validateInputFields(String usuario, String pass, String nombre_real,
                                       String apellidos, String correo, String tlfno,
                                       String direccion, String localidad, int codigoPostal)
    {
        return validarUsuario(usuario) && validarPass(pass)  && validarNombreReal(nombre_real)
                && validarApellidos(apellidos) && validarCorreo(correo) && validarTlfno(tlfno)
                && validarDireccion(direccion) && validarLocalidad(localidad)
                && validarCodigoPostal(codigoPostal);
    }

    public boolean validarUsuario(String input) {
        Pattern p = Pattern.compile("^[a-zA-Z]{3,40}$");
        return p.matcher(input).matches();
    }

    public boolean validarPass(String input) {
        Pattern p = Pattern.compile("^[a-zA-Z0-9]{3,40}$");
        return p.matcher(input).matches();
    }

    public boolean validarNombreReal(String input) {
        Pattern p = Pattern.compile("^[a-zA-Z]{3,40}$");
        return p.matcher(input).matches();
    }

    public boolean validarApellidos(String input) {
        Pattern p = Pattern.compile("^[a-zA-Z]{2,60}$");
        return p.matcher(input).matches();
    }

    public boolean validarCorreo(String input) {
        Pattern p = Pattern.compile("^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$");
        return p.matcher(input).matches();
    }

    public boolean validarTlfno(String input) {
        Pattern p = Pattern.compile("^[0-9]{9}$");
        return p.matcher(input).matches();
    }

    public boolean validarDireccion(String input) {
        Pattern p = Pattern.compile("^[a-zA-Zº0-9,.\\s-]{4,60}$");
        return p.matcher(input).matches();
    }

    public boolean validarLocalidad(String input) {
        Pattern p = Pattern.compile("^[a-zA-Z\\s]{4,35}$");
        return p.matcher(input).matches();
    }

    public boolean validarCodigoPostal(int input) {
        Pattern p = Pattern.compile("^[0-9]{5}$");
        return p.matcher(Integer.toString(input)).matches();

    }

    public String hashearMD5(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");

            byte[] messageDigest = md.digest(password.getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            StringBuilder hashtext = new StringBuilder(no.toString(16));
            while (hashtext.length() < 32) {
                hashtext.insert(0, "0");
            }
            return hashtext.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

}