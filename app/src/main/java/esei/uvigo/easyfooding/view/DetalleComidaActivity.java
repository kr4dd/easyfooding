package esei.uvigo.easyfooding.view;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Objects;

import esei.uvigo.easyfooding.core.OperationsUser;
import esei.uvigo.easyfooding.R;
import esei.uvigo.easyfooding.model.ModeloCarrito;

public class DetalleComidaActivity extends AppCompatActivity {
    TextView cantidadComida;
    TextView precioTotal;
    ImageView simboloPostivo;
    ImageView simboloNegativo;
    Float precio_comida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_comida);

        //Ocultar la barra con el titulo
        Objects.requireNonNull(getSupportActionBar()).hide();

        //Conectar a BD
        ModeloCarrito db = new ModeloCarrito(this);

        Bundle parametros = getIntent().getExtras();
        String codigoComida = parametros.get("codigo_comida").toString();

        simboloPostivo = findViewById(R.id.simboloPositivo);
        simboloNegativo = findViewById(R.id.simboloNegativo);
        precioTotal = findViewById(R.id.textView_precio_total);

        rellenarDatosActividad(codigoComida, db);

        //Por defecto, la cantidad de esa comida a añadir al carrito es 1
        cantidadComida = findViewById(R.id.cantidad_comida);
        cantidadComida.setText("1");

        //Añadir Listeners a los botones de mas o menos comida
        botonesCantidadComida();

        //Añadir funcionalidad al botón de añadir al carrito
        botonCarrito(codigoComida, db);

        //Borrar parametros de la actividad
        parametros.clear();
    }


    //Función para  extraer datos de la BD y mostrarlos en la actividad
    @SuppressLint("SetTextI18n")
    private void rellenarDatosActividad(String codigoComida, ModeloCarrito db) {
        ArrayList<String> datosComida = db.getDatosComidaPorId(codigoComida);

        //nombre
        TextView titulo = findViewById(R.id.textView_nombre_comida);
        titulo.setText(datosComida.get(0));

        //descripcion
        TextView descripcion = findViewById(R.id.textView_descripcion);
        descripcion.setText(datosComida.get(1));

        //precio
        TextView precio = findViewById(R.id.textView_precio_unidad);
        this.precio_comida = Float.parseFloat(datosComida.get(2));
        precio.setText(datosComida.get(2) + "€");
        this.precioTotal.setText(datosComida.get(2) + "€"); //El precio total por defecto es el precio de una unidad

        //valoracion
        TextView valoracion = findViewById(R.id.textView_valoracion);
        valoracion.setText(datosComida.get(3));

        //calorias
        TextView calorias = findViewById(R.id.textView_calorias);
        calorias.setText(datosComida.get(4) + " Kcal");

        //tiempo_preparacion
        TextView tiempo_preparacion = findViewById(R.id.textView_tiempo_preparacion);
        tiempo_preparacion.setText(datosComida.get(5) + " min");

    }

    //Calcular el precio a añadir al carrito según la cantidad de comida que se pida
    private void botonesCantidadComida() {

        //Añadir listener a los botones de mas y de menos
        simboloPostivo.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                int cantidad = Integer.parseInt(cantidadComida.getText().toString());
                if (cantidad <= 9) {
                    cantidad++;
                }
                cantidadComida.setText(cantidad + "");
                calcularNuevoImporte(cantidad);
            }
        });

        simboloNegativo.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                int cantidad = Integer.parseInt(cantidadComida.getText().toString());
                if (cantidad >= 2) {
                    cantidad--;
                }
                cantidadComida.setText(cantidad + "");
                calcularNuevoImporte(cantidad);
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void calcularNuevoImporte(int cantidad){
        float importe = precio_comida * cantidad;
        precioTotal.setText(importe +"€");
    }

    private void botonCarrito(String codigoComida, ModeloCarrito db){
        TextView carrito = findViewById(R.id.textView_add_carrito);
        carrito.setOnClickListener(view -> {

            //insertar en la tabla carrito_temp el codigo del usuario, el codigo de la  comida y la cantidad.
            db.insertarLineaCarrito(OperationsUser.getUserFromSession(getApplicationContext()), codigoComida, Integer.parseInt(cantidadComida.getText().toString()));

            //Resetear la cantidad
            cantidadComida.setText("1");

            //Redirigir al usuario al inicio
            finish();
            startActivity(new Intent(DetalleComidaActivity.this, InicioActivity.class));
        });
    }

    @Override
    protected void onPause(){
        super.onPause();
        SharedPreferences prefs = this.getSharedPreferences("datosOnPause", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("cantidadDetalleComida", this.cantidadComida.getText().toString());
        editor.apply();

    }

    @Override
    protected void onResume(){
        super.onResume();
        SharedPreferences prefs = this.getSharedPreferences("datosOnPause", MODE_PRIVATE);
        String cantidadDetalleComida = prefs.getString("cantidadDetalleComida", "1");
        this.cantidadComida.setText(cantidadDetalleComida);

    }

    //Método para controlar que pulsamos la tecla de back en el dispositivo movil
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == event.KEYCODE_BACK){
            cantidadComida.setText("1");
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}