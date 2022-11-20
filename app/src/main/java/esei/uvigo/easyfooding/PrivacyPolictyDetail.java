package esei.uvigo.easyfooding;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import java.util.Objects;

public class PrivacyPolictyDetail extends AppCompatActivity {

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
        Button goBack = findViewById(R.id.privacy_button);
        goBack.setOnClickListener( view -> {
            finish();
            startActivity(new Intent(PrivacyPolictyDetail.this, OptionsActivity.class));
        });
    }
}
