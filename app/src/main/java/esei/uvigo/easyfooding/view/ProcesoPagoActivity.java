package esei.uvigo.easyfooding.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;
import java.util.Objects;
import java.util.regex.Pattern;

import esei.uvigo.easyfooding.R;
import esei.uvigo.easyfooding.core.Comida;
import esei.uvigo.easyfooding.core.OperationsUser;
import esei.uvigo.easyfooding.model.ModeloPago;

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
        pago.setOnClickListener(view -> getValues(datosComidas, importe));
    }

    public void getValues(ArrayList<Comida> comidas, String importe) {
        EditText direccion = findViewById(R.id.direc);
        EditText ciudad = findViewById(R.id.ciudad);
        EditText codigoPost = findViewById(R.id.codigoPost);
        EditText obs = findViewById(R.id.obs);

        String varDireccion = direccion.getText().toString();
        String varCiudad = ciudad.getText().toString();
        String varCodigoPostal = codigoPost.getText().toString();
        String varObs = obs.getText().toString();

        //Validar datos
        if(validateInputFields(varDireccion, varCiudad, varCodigoPostal, varObs))
        {
            insertarPedido(comidas, importe, varDireccion, varCiudad, varCodigoPostal, varObs);
        }

    }

    public boolean validateInputFields(String varDireccion, String varCiudad, String varCodigoPostal,
                                       String varObs)
    {
        //Validar datos input formulario del proceso de pago
        validarDireccion(varDireccion);
        validarLocalidad(varCiudad);

        int cp;
        try {
            cp = Integer.parseInt(varCodigoPostal);
        } catch (NumberFormatException e) {
            cp = 000000;
        }
        validarCodigoPostal(cp);

        validarObs(varObs);

        TextView errLocal = findViewById(R.id.errLocal);
        TextView errDirec = findViewById(R.id.errDirec);
        TextView errObs = findViewById(R.id.errObs);
        TextView errPostal = findViewById(R.id.errPostal);

        return errLocal.getVisibility() == View.GONE && errDirec.getVisibility() == View.GONE
                && errObs.getVisibility() == View.GONE && errPostal.getVisibility() == View.GONE;
    }

    private void insertarPedido(ArrayList<Comida> comidas, String importe, String direccion,
                                String localidad, String varCodigo, String observaciones)
    {

        String nombreUsuario = OperationsUser.getUserFromSession(this);
        String fechaActual = OperationsUser.getActualDateSpanishStrFormat();
        int cp = Integer.parseInt(varCodigo);
        double precio = Double.parseDouble(importe.replaceFirst(",","."));

        //Acceso a la base de datos
        ModeloPago db = new ModeloPago(this);
        int maxId = db.getMaxIdPedido();
        int idNuevoPedido = maxId + 1;
        db.insertarPedido(idNuevoPedido, nombreUsuario, fechaActual, direccion, localidad,
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
        ModeloPago db = new ModeloPago(this);
        db.eliminarProductorComprados(OperationsUser.getUserFromSession(this));
    }

    private void insertarLineasPedido(int idPedido, Comida comida) {
        ModeloPago db = new ModeloPago(this);
        int maxIDLinea = db.getMaxIdLineaPedido();
        int idActual = maxIDLinea + 1;
        int codigoComida = comida.getCodigo();
        int cantidad = comida.getCantidad();
        double precio = comida.getPrecio();
        double precioTotal = precio * cantidad;

        db.insertarLineaPedido(idActual, idPedido, codigoComida, cantidad, precioTotal);
    }

    public void validarDireccion(String input) {
        Pattern p = Pattern.compile("^[a-zA-Zº0-9áéíóúÁÉÍÓÚ,.\\s-]{4,60}$");

        showErrMessagesForRegisterTxtViews(p, input, R.id.errDirec);
    }

    public void validarLocalidad(String input) {
        Pattern p = Pattern.compile("^[a-zA-ZáéíóúÁÉÍÓÚ\\s]{4,35}$");

        showErrMessagesForRegisterTxtViews(p, input, R.id.errLocal);
    }

    public void validarObs(String input) {
        Pattern p = Pattern.compile("(^$|^[A-Za-z0-9\\s(),.¿?=\\-+áéíóúñÁÉÍÓÚÑ]{1,80}$)");

        showErrMessagesForRegisterTxtViews(p, input, R.id.errObs);
    }

    public void validarCodigoPostal(int input) {
        Pattern p = Pattern.compile("^[0-9]{5}$");

        showErrMessagesForRegisterTxtViews(p, Integer.toString(input), R.id.errPostal);
    }

    public void showErrMessagesForRegisterTxtViews(Pattern p, String input, int view) {
        TextView errMsg = findViewById(view);

        if(!p.matcher(input).matches()) {
            errMsg.setVisibility(View.VISIBLE); //Muestra el error

        } else {
            errMsg.setVisibility(View.GONE); //Hace que el error desaparezca
        }
    }

}
