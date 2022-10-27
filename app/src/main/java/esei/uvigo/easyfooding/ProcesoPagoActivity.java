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
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

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
        || TextUtils.isEmpty(varCodigo)
        || TextUtils.isEmpty(varObs)) {
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
    } else {
      insertarPedido(comidas, importe, varDireccion, varCiudad, varCodigo, varObs);
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
    boolean insert =
        dataBaseAccess.insertarPedido(
            idNuevoPedido, nombreUsuario, fecha, direccion, localidad, cp, precio, observaciones);
    for(int i = 0; i<comidas.size();i++){
      insertarLineasPedido(idNuevoPedido,comidas.get(i));
    }
    Intent intent = new Intent(ProcesoPagoActivity.this, InicioActivity.class);
    startActivity(intent);
    /*todo implementar la insercion de linea pedido (en el pedido hay que cojer la fecha actual con la funcion de diego) y de la linea
     *   -para la linea -> Cojer el Maxid
     *   -->recorrer el array de comidas y añadir cada una a una linea distinta
     *   --> por cada linea buscar el precio de esa comida en la base de datos y multiplicar por la cantidad
     *   -->Para el numPedido tenemos que cojer el de esta funcion, idNuevoPedido
     *   -->Implementar una redireccion a la actividad inicio despues del pago*/

  }

  private void insertarLineasPedido(int idPedido,Comida comida) {
    Toast.makeText(ProcesoPagoActivity.this,"Estoy aqui",Toast.LENGTH_LONG).show();
    DatabaseAccess dataBaseAccess = DatabaseAccess.getInstance(getApplicationContext());
    dataBaseAccess.open();
    int maxIDLinea = dataBaseAccess.getMaxIdLineaPedido();
    dataBaseAccess.close();
    int idActual = maxIDLinea + 1;
    int codigoComida = comida.getCodigo();
    int cantidad = comida.getCantidad();
    double precio = comida.getPrecio();
    dataBaseAccess = DatabaseAccess.getInstance(getApplicationContext());
    dataBaseAccess.open();
    boolean res = dataBaseAccess.insertarLineaPedido(idActual, idPedido, codigoComida, cantidad, precio);
    Toast.makeText(ProcesoPagoActivity.this, String.valueOf(res), Toast.LENGTH_LONG).show();
    dataBaseAccess.close();
  }

  public String getDateIntoSpanishStringFormat() {
    Calendar cal = Calendar.getInstance();
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    return sdf.format(cal.getTime());
  }
}
