package esei.uvigo.easyfooding.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

import esei.uvigo.easyfooding.core.OperationsUser;
import esei.uvigo.easyfooding.R;
import esei.uvigo.easyfooding.model.ModeloUsuario;

public class LoginUserActivity extends AppCompatActivity {
    String usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_user);

        //Ocultar la barra con el titulo
        Objects.requireNonNull(getSupportActionBar()).hide();

        ModeloUsuario db = new ModeloUsuario(this);

        Button btnRegistrarse = findViewById(R.id.btnLogearse);
        btnRegistrarse.setOnClickListener(
                view -> {
                    if(checkearLogin(db)) {
                        //Crear sesion de usuario
                        OperationsUser.setSession(this, usuario);

                        //Mandar a inicio
                        goToInicio();
                    }

                });

    }

    public boolean checkearLogin(ModeloUsuario db) {
        EditText editTextUsuario = findViewById(R.id.editTextUsuario);
        usuario = editTextUsuario.getText().toString();

        EditText editTextPass = findViewById(R.id.editTextPass);
        String pass = editTextPass.getText().toString();

        //Validar contra la base de datos
        boolean isAllow;
        isAllow = db.checkLogin(usuario, OperationsUser.hashearMD5(pass));

        //Mensaje en caso de login no autorizado
        TextView errMsg = findViewById(R.id.errLogin);
        if(!isAllow) {
            errMsg.setVisibility(View.VISIBLE); //Muestra el error
        } else {
            errMsg.setVisibility(View.GONE); //Hace que el error desaparezca
        }

        return isAllow;
    }

    public void goToInicio() {
        finish();

        startActivity(new Intent(this, InicioActivity.class));
    }


}