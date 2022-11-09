package esei.uvigo.easyfooding;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;
import java.util.regex.Pattern;

import esei.uvigo.easyfooding.database.DatabaseAccess;
import esei.uvigo.easyfooding.core.Comida;
import esei.uvigo.easyfooding.model.AccesoModelo;

public class ProcesoPagoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proceso_pago);
        Bundle datos = getIntent().getExtras();
        Objects.requireNonNull(getSupportActionBar()).hide();
        ArrayList<Comida> datosComidas = (ArrayList<Comida>) datos.get("datosProductos");
        String importe = (String) datos.get("importe");
        ConstraintLayout pago = findViewById(R.id.pagar);
        pago.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getValues(datosComidas, importe);
                    }
                });
    }

    public void getValues(ArrayList<Comida> comidas, String importe) {
        EditText direccion = findViewById(R.id.direc);
        EditText ciudad = findViewById(R.id.ciudad);
        EditText codigoPost = findViewById(R.id.codigoPost);
        EditText obs = findViewById(R.id.obs);

        String varDireccion = direccion.getText().toString();
        String varCiudad = ciudad.getText().toString();
        String varCodigo = codigoPost.getText().toString();
        String varObs = obs.getText().toString();
        if (TextUtils.isEmpty(varDireccion)
                || TextUtils.isEmpty(varCiudad)
                || TextUtils.isEmpty(varCodigo)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(ProcesoPagoActivity.this);
            builder.setMessage("Debes introducir todos los campos");
            builder.setPositiveButton(
                    R.string.aceptar,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            return;
                        }
                    });
            builder.create().show();
            // validaciones
        } else {
            boolean isvalid = true;
            if (!validarDireccion(varDireccion)) {
                isvalid = false;
            }
            if (!validarLocalidad(varCiudad)) {
                isvalid = false;
            }
            if (!validarCodigoPostal(Integer.parseInt(varCodigo))) {
                isvalid = false;
            }
            if (!TextUtils.isEmpty(varObs) && !validarObs(varObs)) {
                isvalid = false;
            }
            if (isvalid) {
                if (TextUtils.isEmpty(varObs)) {
                    insertarPedido(comidas, importe, varDireccion, varCiudad, varCodigo, "");
                } else {
                    insertarPedido(comidas, importe, varDireccion, varCiudad, varCodigo, varObs);
                }
            }
        }
    }

    private void insertarPedido(ArrayList<Comida> comidas, String importe, String direccion,
                                String localidad, String varCodigo, String observaciones)
    {

        String nombreUsuario = OperationsUser.getUserFromSession(this); // aqui iria el user que se autentico
        String fecha = OperationsUser.getActualDateSpanishStrFormat(); // llamamos a la funcion que nos devuelve la fecha actual
        int cp = Integer.parseInt(varCodigo);
        double precio = Double.parseDouble(importe);

        //Acceso a la base de datos
        AccesoModelo db = new AccesoModelo(this);
        int maxId = db.getMaxIdPedido();
        int idNuevoPedido = maxId + 1;
        db.insertarPedido(idNuevoPedido, nombreUsuario, fecha, direccion, localidad,
                cp, precio, observaciones);

        for (int i = 0; i < comidas.size(); i++) {
            insertarLineasPedido(idNuevoPedido, comidas.get(i));
        }

        // vaciamos la tabla del carrito
        limpiarTablaCarrito();

        // limpiamos la lista y el adaptador del recycleview
        CarritoActivity.listaComida.clear();
        Intent intent = new Intent(ProcesoPagoActivity.this, InicioActivity.class);
        finish();
        startActivity(intent);
    }

    private void limpiarTablaCarrito() {
        AccesoModelo db = new AccesoModelo(this);
        db.eliminarProductorComprados(OperationsUser.getUserFromSession(this));
    }

    private void insertarLineasPedido(int idPedido, Comida comida) {
        AccesoModelo db = new AccesoModelo(this);
        int maxIDLinea = db.getMaxIdLineaPedido();
        int idActual = maxIDLinea + 1;
        int codigoComida = comida.getCodigo();
        int cantidad = comida.getCantidad();
        double precio = comida.getPrecio();
        double precioTotal = precio * cantidad;

        db.insertarLineaPedido(idActual, idPedido, codigoComida, cantidad, precioTotal);
    }

    public boolean validarDireccion(String input) {
        Pattern p = Pattern.compile("^[a-zA-Zº0-9,.\\s-]{4,60}$");

        return showErrMessagesForRegisterTxtViews(p, input, R.id.errDirec);
    }

    public boolean validarLocalidad(String input) {
        Pattern p = Pattern.compile("^[a-zA-Z\\s]{4,35}$");

        return showErrMessagesForRegisterTxtViews(p, input, R.id.errLocal);
    }

    public boolean validarObs(String input) {
        Pattern p = Pattern.compile("^[A-Za-z0-9\\s(),.¿?=\\-+áéíóúñÁÉÍÓÚÑ]{1,80}$");
        return showErrMessagesForRegisterTxtViews(p, input, R.id.errObs);
    }

    public boolean showErrMessagesForRegisterTxtViews(Pattern p, String input, int view) {
        TextView errMsg = findViewById(view);

        if (!p.matcher(input).matches()) {
            errMsg.setVisibility(View.VISIBLE); // Muestra el error
            return false;

        } else {
            errMsg.setVisibility(View.GONE); // Hace que el error desaparezca
            return true;
        }
    }

    public boolean validarCodigoPostal(int input) {
        Pattern p = Pattern.compile("^[0-9]{5}$");

        return showErrMessagesForRegisterTxtViews(p, Integer.toString(input), R.id.errPostal);
    }
}
