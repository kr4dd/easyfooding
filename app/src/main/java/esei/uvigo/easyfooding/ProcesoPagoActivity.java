package esei.uvigo.easyfooding;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

import esei.uvigo.easyfooding.objetosCarrito.Comida;

public class ProcesoPagoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proceso_pago);
        Bundle datos = getIntent().getExtras();
        Objects.requireNonNull(getSupportActionBar()).hide();
        ArrayList<Comida> datosComidas = (ArrayList<Comida>) datos.get("datosProductos");
        Toast.makeText(ProcesoPagoActivity.this, String.valueOf(datosComidas.toString()),Toast.LENGTH_LONG).show();
        //TODO, Ya tengo aqui importe total (para la insercion en pedidos) y los datos de cada comida (para la insercion en linea
        // primero insertar el pedido en si, luego insertar la linea)
    }
}