package esei.uvigo.easyfooding;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

import esei.uvigo.easyfooding.database.DatabaseAccess;
import esei.uvigo.easyfooding.objetosCarrito.Comida;

public class ProcesoPagoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proceso_pago);
        Bundle datos = getIntent().getExtras();
        Objects.requireNonNull(getSupportActionBar()).hide();
        ArrayList<Comida> datosComidas = (ArrayList<Comida>) datos.get("datosProductos");
        double precioTotal = Double.parseDouble((String) datos.get("importe"));
        Toast.makeText(ProcesoPagoActivity.this, String.valueOf(datosComidas.toString()),Toast.LENGTH_LONG).show();
        //TODO, Ya tengo aqui importe total (para la insercion en pedidos) y los datos de cada comida (para la insercion en linea
        // primero insertar el pedido en si, luego insertar la linea)
        insertarPedido();
    }

    private void insertarPedido() {
        DatabaseAccess dataBaseAccess = DatabaseAccess.getInstance(getApplicationContext());
        dataBaseAccess.open();
        Toast.makeText(ProcesoPagoActivity.this, String.valueOf(dataBaseAccess.getMaxidPedido()),Toast.LENGTH_LONG).show();
        Toast.makeText(ProcesoPagoActivity.this, String.valueOf(dataBaseAccess.getMaxidLineaPedidos()),Toast.LENGTH_LONG).show();

    }
}