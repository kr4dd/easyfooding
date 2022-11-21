package esei.uvigo.easyfooding.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import java.util.Objects;

import esei.uvigo.easyfooding.R;
import esei.uvigo.easyfooding.core.OperationsUser;

public class OptionsActivity extends AppCompatActivity {
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.options_activity);

        //Cambiar de actividades
        OperationsUser.cambiarActividadPanelInterno(getWindow().getDecorView(), this, this);

        setChangeThemeEvent(this);
        setLogoutEvent(this);
        setGoToPrivacyPolicy();
    }

    private void setGoToPrivacyPolicy()
    {
        // Cambiar a la actividad de Inicio
        LinearLayout privacyLayout = findViewById(R.id.privacy_policy_layout);
        privacyLayout.setOnClickListener( view -> {
            finish();
            startActivity(new Intent(OptionsActivity.this, PrivacyPolictyDetail.class));
        });
    }

    private void setLogoutEvent(Context context)
    {
        LinearLayout languageLayout = findViewById(R.id.logout_layout);
        languageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { onLogoutPressed(context); }
        });
    }

    private void onLogoutPressed(Context context)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you sure to logout?");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences.Editor editor = context.getSharedPreferences("data", Context.MODE_PRIVATE).edit();
                editor.remove("nombre_usuario");

                finish();
                startActivity(new Intent(OptionsActivity.this, WelcomeUserActivity.class));
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /*** CHANGE APP LANGUAGE ***
    private void setChangeLanguageEvent(Context context)
    {
        LinearLayout languageLayout = findViewById(R.id.language_layout);
        languageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { onChangeLanguagePressed(context); }
        });
    }

    private void onChangeLanguagePressed(Context context)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        String language = sharedPreferences.getString("language", "");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Change app language:");

        String[] languages = {"Español", "Ingles", "Gallego"};
        List<String> languagesList = new ArrayList<>(Arrays.asList(languages));

        int checkedItem = languagesList.contains(language) ? languagesList.indexOf(language) : 0;

        builder.setSingleChoiceItems(languages, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                SharedPreferences.Editor ed = sharedPreferences.edit();
                ed.putString("language", languages[which]);
                boolean result = ed.commit();
            }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // user clicked OK
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
     */

    private void setChangeThemeEvent(Context context)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        String theme = sharedPreferences.getString("theme", "");

        Switch switchTheme = findViewById(R.id.change_theme_switch);
        switchTheme.setChecked(theme.equals("dark"));

        switchTheme.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedPreferences.Editor ed = sharedPreferences.edit();

                if(b)
                {
                    ed.putString("theme", "dark");
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }
                else
                {
                    ed.putString("theme", "light");
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }

                boolean result = ed.commit();
            }
        });
    }
}