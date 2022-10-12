package esei.uvigo.easyfooding;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import esei.uvigo.easyfooding.database.DatabaseAccess;

public class RegisterUserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);

        Button btnRegistrarse = findViewById(R.id.btnLogearse);
        btnRegistrarse.setOnClickListener(
                view -> {
                    if(registrarUsuario(databaseAccess)) {
                        //TODO llevarse el nombre de usuario a la sesion
                        goToInicio();
                    } else {
                        goToWelcomeScreen();
                    }

                    //TODO Validar inputs
                    //TODO Hashear contrasenas empleado hashMD5 al registrarse y deshashear al logear
                });

    }

    public boolean registrarUsuario(DatabaseAccess db) {
        EditText editTextUsuario = findViewById(R.id.editTextUsuario);
        String usuario = editTextUsuario.getText().toString();

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
        int cp = Integer.parseInt(editTextCp.getText().toString());

        String fechaAlta = getDateIntoSpanishStringFormat(); // Sample "02-08-22";

        db.open();
        boolean res = db.insertarUsuario(usuario, pass, nombre_real, apellidos, correo, tlfno,
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
        showToastMsg("Login incorrecto");

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
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        return sdf.format(cal.getTime());
    }
}