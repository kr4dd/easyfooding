package esei.uvigo.easyfooding.view;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.Objects;

import esei.uvigo.easyfooding.R;

public class WelcomeUserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_user);

        //Ir a pantalla de registro
        Button btnGoToRegistro = findViewById(R.id.btnGoRegistro);
        btnGoToRegistro.setOnClickListener(
                view -> startActivity(new Intent(this, RegisterUserActivity.class)));


        //Ir a pantalla de login
        Button btnGoToLogin = findViewById(R.id.btnGoLogin);;
        btnGoToLogin.setOnClickListener(
                view -> startActivity(new Intent(this, LoginUserActivity.class)));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu( menu );

        this.getMenuInflater().inflate( R.menu.actions_menu, menu );
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem)
    {
        boolean toret = false;

        switch( menuItem.getItemId() ) {
            case R.id.privacyTitlePanel:

                // Cargar politicade privacidad
                startActivity(new Intent(this, PrivacyPolicityDetailWelcome.class));

                toret = true;
                break;
        }

        return toret;
    }
}