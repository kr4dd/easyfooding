package esei.uvigo.easyfooding.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import java.util.Objects;

import esei.uvigo.easyfooding.R;

public class WelcomeUserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_user);

        //Ocultar la barra con el titulo
        Objects.requireNonNull(getSupportActionBar()).hide();


        //Ir a pantalla de registro
        Button btnGoToRegistro = findViewById(R.id.btnGoRegistro);
        btnGoToRegistro.setOnClickListener(
                view -> startActivity(new Intent(this, RegisterUserActivity.class)));


        //Ir a pantalla de login
        Button btnGoToLogin = findViewById(R.id.btnGoLogin);;
        btnGoToLogin.setOnClickListener(
                view -> startActivity(new Intent(this, LoginUserActivity.class)));

    }
}