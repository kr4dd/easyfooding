package esei.uvigo.easyfooding.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.util.Objects;

import esei.uvigo.easyfooding.R;

public class AjustesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajustes);

        Objects.requireNonNull(getSupportActionBar()).hide();

    }
}