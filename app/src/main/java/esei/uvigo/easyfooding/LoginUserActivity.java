package esei.uvigo.easyfooding;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import esei.uvigo.easyfooding.database.DatabaseAccess;

public class LoginUserActivity extends AppCompatActivity {
    String usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_user);

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);

        Button btnRegistrarse = findViewById(R.id.btnLogearse);
        btnRegistrarse.setOnClickListener(
                view -> {
                    if(checkearLogin(databaseAccess)) {
                        //Crear sesion de usuario
                        OperationsUserActivity.setSession(this, usuario);

                        //Mandar a inicio
                        goToInicio();
                    } else {
                        goToWelcomeScreen();
                    }

                    //TODO anadir mensajes de error en campos de validacion
                });

    }

    public boolean checkearLogin(DatabaseAccess db) {
        EditText editTextUsuario = findViewById(R.id.editTextUsuario);
        usuario = editTextUsuario.getText().toString();

        EditText editTextPass = findViewById(R.id.editTextPass);
        String pass = editTextPass.getText().toString();

        boolean isAllow;
        db.open();
        isAllow = db.checkLogin(usuario, OperationsUserActivity.hashearMD5(pass));
        db.close();

        return isAllow;
    }

    public void goToInicio() {
        OperationsUserActivity.showToastMsg(this, "Login correcto");

        finish();

        startActivity(new Intent(this, InicioActivity.class));
    }

    public void goToWelcomeScreen() {
        OperationsUserActivity.showToastMsg(this, "Login incorrecto");

        finish();

        startActivity(new Intent(this, WelcomeUserActivity.class));
    }

}