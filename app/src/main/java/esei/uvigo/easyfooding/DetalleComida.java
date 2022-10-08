package esei.uvigo.easyfooding;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import esei.uvigo.easyfooding.database.DatabaseAccess;

public class DetalleComida extends AppCompatActivity {

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
        getSupportActionBar().hide();

        //Conectar a BD
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getApplicationContext());

        Bundle parametros = getIntent().getExtras();
        String codigo_comida = parametros.get("codigo_comida").toString();

        simboloPostivo = findViewById(R.id.simboloPositivo);
        simboloNegativo = findViewById(R.id.simboloNegativo);
        precioTotal = findViewById(R.id.textView_precio_total);

        rellenarDatosActividad(codigo_comida, databaseAccess);

        //Por defecto, la cantidad de esa comida a añadir al carrito es 1
        cantidadComida = findViewById(R.id.cantidad_comida);
        cantidadComida.setText("1");

        //Añadir Listeners a los botones de mas o menos comida
        botonesCantidadComida();

        //TODO añadir el listener de añadir la comida al carrito
        botonCarrito();

        //Cerrar conexion a BD
        databaseAccess.close();

        //Borrar parametros de la actividad
        parametros.clear();
    }


    //Función para  extraer datos de la BD y mostrarlos en la actividad
    private void rellenarDatosActividad(String codigo_comida, DatabaseAccess databaseAccess) {
        databaseAccess.open();
        ArrayList<String> datosComida = databaseAccess.getDatosComidaPorId(codigo_comida);

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

        databaseAccess.close();
    }

    //Calcular el precio a añadir al carrito según la cantidad de comida que se pida
    private void botonesCantidadComida() {

        //Añadir listener a los botones de mas y de menos
        simboloPostivo.setOnClickListener(new View.OnClickListener() {
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

    private void calcularNuevoImporte(int cantidad){
        float importe = precio_comida * cantidad;
        precioTotal.setText(importe +"€");
    }

    private void botonCarrito(){
        TextView carrito = findViewById(R.id.textView_add_carrito);
        carrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Mostrar un snackbar con notificacion de comida añadida al carrito??
            }
        });
    }
}