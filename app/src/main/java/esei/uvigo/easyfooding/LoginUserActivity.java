package esei.uvigo.easyfooding;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import esei.uvigo.easyfooding.database.DatabaseAccess;

public class LoginUserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_user);

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);

        Button btnRegistrarse = findViewById(R.id.btnLogearse);
        btnRegistrarse.setOnClickListener(
                view -> {
                    if(checkearLogin(databaseAccess)) {
                        goToInicio();
                    } else {
                        goToWelcomeScreen();
                    }

                });

    }

    public boolean checkearLogin(DatabaseAccess db) {
        EditText editTextUsuario = findViewById(R.id.editTextUsuario);
        String usuario = editTextUsuario.getText().toString();

        EditText editTextPass = findViewById(R.id.editTextPass);
        String pass = editTextPass.getText().toString();

        boolean isAllow = false;
        db.open();
        isAllow = db.checkLogin(usuario, pass);
        db.close();

        return isAllow;
    }

    public void goToInicio() {
        showToastMsg("Login correcto");

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
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        toast.show();
    }

}