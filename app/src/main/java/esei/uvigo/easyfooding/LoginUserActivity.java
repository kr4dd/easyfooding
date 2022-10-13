package esei.uvigo.easyfooding;

import androidx.appcompat.app.AppCompatActivity;

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
                    //TODO dejar login esteticamente chulo
                });

    }

    public boolean checkearLogin(DatabaseAccess db) {
        EditText editTextUsuario = findViewById(R.id.editTextUsuario);
        usuario = editTextUsuario.getText().toString();

        EditText editTextPass = findViewById(R.id.editTextPass);
        String pass = editTextPass.getText().toString();

        boolean isAllow;
        db.open();
        isAllow = db.checkLogin(usuario, hashearMD5(pass));
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