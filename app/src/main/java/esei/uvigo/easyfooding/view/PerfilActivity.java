package esei.uvigo.easyfooding.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import java.util.Locale;
import java.util.Objects;

import esei.uvigo.easyfooding.database.DatabaseAccess;
import esei.uvigo.easyfooding.entities.User;
import esei.uvigo.easyfooding.entities.Validators.UserValidator;

public class PerfilActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();

        setContentView(R.layout.perfil_activity);
        cambiarActividad();

        String username = OperationsUserActivity.getUserFromSession(this);

        DatabaseAccess dbAccess = DatabaseAccess.getInstance(getApplicationContext());

        dbAccess.open();

        User currentUser = dbAccess.getCurrentUser(username);
        setUserParams(currentUser);
        setLayoutEventListeners();
        setSaveChangesEvent(dbAccess, currentUser);
        setDeleteUserEvent(dbAccess, currentUser.getNombre_usuario());

        dbAccess.close();
    }

    private void setDeleteUserEvent(DatabaseAccess dbAccess, String nombre_usuario)
    {
        Button deleteUserBtn = findViewById(R.id.profile_delete_user_button);
        deleteUserBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                onDeleteUserPressed(dbAccess, nombre_usuario);
            }
        });
    }

    private void onDeleteUserPressed(DatabaseAccess dbAccess, String nombre_usuario)
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Are you sure to do this action?");

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int whichButton)
            {
                dbAccess.open();

                int result = dbAccess.DeleteUser(nombre_usuario);

                dbAccess.close();

                if (result > 0)
                {
                    finish();
                    startActivity(new Intent(PerfilActivity.this, WelcomeUserActivity.class));
                }
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int whichButton)
            {
                dialog.cancel();
            }
        });

        alert.show();
    }

    private void setSaveChangesEvent (DatabaseAccess dbAccess, User currentUser)
    {
        Button saveChangesBtn = findViewById(R.id.profile_save_changes);
        saveChangesBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                onSaveChangesPressed(dbAccess, currentUser);
            }
        });
    }

    private void onSaveChangesPressed (DatabaseAccess dbAccess, User currentUser)
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Are you sure to do this action?");

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int whichButton)
            {
                updateUserWithViewValues(currentUser);

                dbAccess.open();

                int result = dbAccess.UpdateUser(currentUser);

                if (result > 0)
                {
                    String username = OperationsUserActivity.getUserFromSession(PerfilActivity.this);
                    User updatedUser = dbAccess.getCurrentUser(username);

                    setUserParams(updatedUser);
                }

                dbAccess.close();
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int whichButton)
            {
                dialog.cancel();
            }
        });

        alert.show();
    }

    private void setUserParams (User currentUser)
    {
        TextView info_username = findViewById(R.id.profile_info_username);
        info_username.setText(currentUser.getNombre_usuario().toUpperCase());

        TextView info_email = findViewById(R.id.profile_info_email);
        info_email.setText(currentUser.getMail());

        TextView nombre = findViewById(R.id.profile_name);
        nombre.setText(currentUser.getNombre_real());

        TextView apellidos = findViewById(R.id.profile_surname);
        apellidos.setText(currentUser.getApellidos());

        TextView mail = findViewById(R.id.profile_email);
        mail.setText(currentUser.getMail());

        TextView telefono = findViewById(R.id.profile_telefono);
        telefono.setText(currentUser.getTelefono());

        TextView localidad = findViewById(R.id.profile_localidad);
        localidad.setText(currentUser.getLocalidad());

        TextView direccion = findViewById(R.id.profile_direccion);
        direccion.setText(currentUser.getDireccion());

        TextView codigo_postal = findViewById(R.id.profile_codigoPostal);
        codigo_postal.setText(String.valueOf(currentUser.getCodigo_postal()));
    }

    private void updateUserWithViewValues (User currentUser)
    {
        TextView nombre = findViewById(R.id.profile_name);
        currentUser.setNombre_real(nombre.getText().toString());

        TextView apellidos = findViewById(R.id.profile_surname);
        currentUser.setApellidos(apellidos.getText().toString());

        TextView mail = findViewById(R.id.profile_email);
        currentUser.setMail(mail.getText().toString());

        TextView telefono = findViewById(R.id.profile_telefono);
        currentUser.setTelefono(telefono.getText().toString());

        TextView localidad = findViewById(R.id.profile_localidad);
        currentUser.setLocalidad(localidad.getText().toString());

        TextView direccion = findViewById(R.id.profile_direccion);
        currentUser.setDireccion(direccion.getText().toString());

        TextView codigo_postal = findViewById(R.id.profile_codigoPostal);
        currentUser.setCodigo_postal(Integer.parseInt(codigo_postal.getText().toString()));
    }

    private void setLayoutEventListeners ()
    {
        LinearLayout nombreRealLayout = (LinearLayout) findViewById(R.id.layout_nombre_real);
        LinearLayout apellidosLayout = (LinearLayout) findViewById(R.id.layout_apellidos);
        LinearLayout mailLayout = (LinearLayout) findViewById(R.id.layout_mail);
        LinearLayout telefonoLayout = (LinearLayout) findViewById(R.id.layout_telefono);
        LinearLayout localidadLayout = (LinearLayout) findViewById(R.id.layout_localidad);
        LinearLayout direccionLayout = (LinearLayout) findViewById(R.id.layout_direccion);
        LinearLayout codigoPostalLayout = (LinearLayout) findViewById(R.id.layout_codigo_postal);

        nombreRealLayout.setOnClickListener(this);
        apellidosLayout.setOnClickListener(this);
        mailLayout.setOnClickListener(this);
        telefonoLayout.setOnClickListener(this);
        localidadLayout.setOnClickListener(this);
        direccionLayout.setOnClickListener(this);
        codigoPostalLayout.setOnClickListener(this);
    }

    public void onLayoutPressed (TextView textView, TextView label)
    {
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alert_attribute_editor, null);

        AlertDialog.Builder alert = new AlertDialog.Builder(this)
                .setView(dialogView)
                .setTitle(String.format("Introduce el nuevo valor para %s", label.getText().toString().toLowerCase(Locale.ROOT)));

        alert.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
            }
        });

        alert.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();
            }
        });

        final AlertDialog dialog = alert.create();

        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText input = dialogView.findViewById(R.id.alert_input_field);
                String errorMsg = validateInputField(input.getText().toString(), textView);
                TextView errorMsgInput = dialogView.findViewById(R.id.alert_input_error_message);

                if (errorMsg.isEmpty())
                {
                    textView.setText(input.getText().toString());
                    errorMsgInput.setVisibility(View.GONE);

                    dialog.dismiss();
                }
                else
                {
                    errorMsgInput.setText(errorMsg);
                    errorMsgInput.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private String validateInputField(String input, TextView textView)
    {
        String result = "";

        switch (textView.getId())
        {
            case R.id.profile_name:
                if(UserValidator.validarNombreReal(input)) result = getString(R.string.nombreRealErr);
                break;
            case R.id.profile_surname:
                if(UserValidator.validarApellidos(input)) result = getString(R.string.apellidosErr);
                break;
            case R.id.profile_email:
                if(UserValidator.validarCorreo(input)) result = getString(R.string.mailErr);
                break;
            case R.id.profile_telefono:
                if(UserValidator.validarTlfno(input)) result = getString(R.string.telefonoErr);
                break;
            case R.id.profile_localidad:
                if(UserValidator.validarLocalidad(input)) result = getString(R.string.localidadErr);
                break;
            case R.id.profile_direccion:
                if(UserValidator.validarDireccion(input)) result = getString(R.string.direccionErr);
                break;
            case R.id.profile_codigoPostal:
                if(!isInteger(input) || UserValidator.validarCodigoPostal(Integer.parseInt(input))) result = getString(R.string.codigoPostalErr);
                break;
        }

        return result;
    }

    @Override
    public void onClick (View view)
    {
        TextView textView = new TextView(this);
        TextView label = new TextView(this);

        switch (view.getId())
        {
            case R.id.layout_nombre_real:
                textView = findViewById(R.id.profile_name);
                label = findViewById(R.id.profile_name_label);
                break;
            case R.id.layout_apellidos:
                textView = findViewById(R.id.profile_surname);
                label = findViewById(R.id.profile_surname_label);
                break;
            case R.id.layout_mail:
                textView = findViewById(R.id.profile_email);
                label = findViewById(R.id.profile_email_label);
                break;
            case R.id.layout_telefono:
                textView = findViewById(R.id.profile_telefono);
                label = findViewById(R.id.profile_telefono_label);
                break;
            case R.id.layout_localidad:
                textView = findViewById(R.id.profile_localidad);
                label = findViewById(R.id.profile_localidad_label);
                break;
            case R.id.layout_direccion:
                textView = findViewById(R.id.profile_direccion);
                label = findViewById(R.id.profile_direccion_label);
                break;
            case R.id.layout_codigo_postal:
                textView = findViewById(R.id.profile_codigoPostal);
                label = findViewById(R.id.profile_codigoPostal_label);
                break;
        }

        onLayoutPressed(textView, label);
    }

    private void cambiarActividad ()
    {
        // Cambiar a la actividad de Inicio
        LinearLayout inicio = findViewById(R.id.inicio);
        inicio.setOnClickListener(
                view -> {
                    finish();
                    startActivity(new Intent(PerfilActivity.this, InicioActivity.class));
                });

        // Cambiar a la actividad Perfil
        LinearLayout perfil = findViewById(R.id.perfil);
        perfil.setOnClickListener(
                view -> {
                    finish();
                    startActivity(new Intent(PerfilActivity.this, PerfilActivity.class));
                });

        // Cambiar a la actividad Carrito
        LinearLayout carrito = findViewById(R.id.carrito);
        carrito.setOnClickListener(
                view -> {
                    finish();
                    startActivity(new Intent(PerfilActivity.this, CarritoActivity.class));
                });

        // Cambiar a la actividad Pedidos
        LinearLayout pedidos = findViewById(R.id.pedidos);
        pedidos.setOnClickListener(
                view -> {
                    finish();
                    startActivity(new Intent(PerfilActivity.this, PedidosActivity.class));
                });

        // Cambiar a la actividad Ajustes
        LinearLayout ajustes = findViewById(R.id.ajustes);
        ajustes.setOnClickListener(
                view -> {
                    finish();
                    startActivity(new Intent(PerfilActivity.this, OptionsActivity.class));
                });
    }

    private boolean isInteger(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            int d = Integer.parseInt(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}