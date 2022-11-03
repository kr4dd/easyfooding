package esei.uvigo.easyfooding;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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
import esei.uvigo.easyfooding.objetosCarrito.Comida;

public class ProcesoPagoActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_proceso_pago);
    Bundle datos = getIntent().getExtras();
    setColoresAndroidModoOscuro();
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

  private void setColoresAndroidModoOscuro() {
    // Colores de los textos de la navbar inferior
    EditText direccion = findViewById(R.id.direc);
    direccion.setTextColor(Color.GRAY);
    EditText ciudad = findViewById(R.id.ciudad);
    ciudad.setTextColor(Color.GRAY);
    EditText codigoPost = findViewById(R.id.codigoPost);
    codigoPost.setTextColor(Color.GRAY);
    EditText obs = findViewById(R.id.obs);
    obs.setTextColor(Color.GRAY);
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
      if(!TextUtils.isEmpty(varObs) && !validarObs(varObs)){
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

  private void insertarPedido(
      ArrayList<Comida> comidas,
      String importe,
      String varDireccion,
      String varCiudad,
      String varCodigo,
      String varObs) {

    String nombreUsuario =
        OperationsUserActivity.getUserFromSession(this); // aqui iria el user que se autentico
    String fecha =
        getDateIntoSpanishStringFormat(); // llamamos a la funcion que nos devuelve la fecha actual
    String direccion = varDireccion;
    String localidad = varCiudad;
    int cp = Integer.parseInt(varCodigo);
    double precio = Double.parseDouble(importe);
    String observaciones = varObs;
    DatabaseAccess dataBaseAccess = DatabaseAccess.getInstance(getApplicationContext());
    dataBaseAccess.open();
    int maxId = dataBaseAccess.getMaxIdPedido();
    int idNuevoPedido = maxId + 1;
    dataBaseAccess.insertarPedido(
        idNuevoPedido, nombreUsuario, fecha, direccion, localidad, cp, precio, observaciones);
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
    DatabaseAccess dataBaseAccess = DatabaseAccess.getInstance(getApplicationContext());
    dataBaseAccess.open();
    dataBaseAccess.eliminarProductorComprados(OperationsUserActivity.getUserFromSession(this));
  }

  private void insertarLineasPedido(int idPedido, Comida comida) {
    DatabaseAccess dataBaseAccess = DatabaseAccess.getInstance(getApplicationContext());
    dataBaseAccess.open();
    int maxIDLinea = dataBaseAccess.getMaxIdLineaPedido();
    dataBaseAccess.close();
    int idActual = maxIDLinea + 1;
    int codigoComida = comida.getCodigo();
    int cantidad = comida.getCantidad();
    double precio = comida.getPrecio();
    double precioTotal = precio * cantidad;
    dataBaseAccess = DatabaseAccess.getInstance(getApplicationContext());
    dataBaseAccess.open();
    dataBaseAccess.insertarLineaPedido(idActual, idPedido, codigoComida, cantidad, precioTotal);
    dataBaseAccess.close();
  }

  public String getDateIntoSpanishStringFormat() {
    Calendar cal = Calendar.getInstance();
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    return sdf.format(cal.getTime());
  }

  public boolean validarDireccion(String input) {
    Pattern p = Pattern.compile("^[a-zA-Zº0-9,.\\s-]{4,60}$");

    boolean correcto = showErrMessagesForRegisterTxtViews(p, input, R.id.errDirec);
    if (correcto) {
      return true;
    } else {
      return false;
    }
  }

  public boolean validarLocalidad(String input) {
    Pattern p = Pattern.compile("^[a-zA-Z\\s]{4,35}$");

    boolean correcto = showErrMessagesForRegisterTxtViews(p, input, R.id.errLocal);
    if (correcto) {
      return true;
    } else {
      return false;
    }
  }
  public boolean validarObs(String input){
    Pattern p = Pattern.compile("^[A-Za-z0-9\\s\\(\\)\\,\\.\\¿\\?\\=\\-\\+\\áéíóúñÁÉÍÓÚÑ]{1,80}$");
    boolean correcto = showErrMessagesForRegisterTxtViews(p, input, R.id.errObs);
    if (correcto) {
      return true;
    } else {
      return false;
    }
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

    boolean correcto =
        showErrMessagesForRegisterTxtViews(p, Integer.toString(input), R.id.errPostal);
    if (correcto) {
      return true;
    } else {
      return false;
    }
  }
}
