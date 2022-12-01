package esei.uvigo.easyfooding.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import java.util.Objects;

import esei.uvigo.easyfooding.R;
import esei.uvigo.easyfooding.core.OperationsUser;

public class PrivacyPolicityDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();

        setContentView(R.layout.privacy_policy);
        setGoBackEvent();
    }

    private void setGoBackEvent()
    {
        // Usuario NO registrado se mantiene en el panel interior al aceptar los terminos
        if(OperationsUser.getUserFromSession(PrivacyPolicityDetail.this).isEmpty()){
            Button goBack = findViewById(R.id.privacy_button);
            goBack.setOnClickListener( view -> {
                finish();
                startActivity(new Intent(PrivacyPolicityDetail.this, WelcomeUserActivity.class));
            });

        } else {
            // Usuario registrado se mantiene en el panel interior al aceptar los terminos
            Button goBack = findViewById(R.id.privacy_button);
            goBack.setOnClickListener( view -> {
                finish();
                startActivity(new Intent(PrivacyPolicityDetail.this, OptionsActivity.class));
            });
        }


    }
}
