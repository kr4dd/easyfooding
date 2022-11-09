package esei.uvigo.easyfooding;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Objects;

import esei.uvigo.easyfooding.database.DatabaseAccess;
import esei.uvigo.easyfooding.model.AccesoModelo;

public class InicioActivity extends AppCompatActivity {
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inicio_activity);

        //Ocultar la barra con el titulo
        Objects.requireNonNull(getSupportActionBar()).hide();

        //Cargar saludo a usuario
        TextView msgBienvenida = findViewById(R.id.saludoUsuario);
        msgBienvenida.setText("Hola, " + OperationsUser.getUserFromSession(this));

        //Cambiar de actividades
        cambiarActividad();

        //Crear instancia de la BD
        AccesoModelo db = new AccesoModelo(this);

        //Generar dinamicamente ScrollViewHorizontal de categorias
        construirListaCategorias(db);

        //Generar dinamicamente ScrollViewHorizontal de recomendaciones
        construirListaRecomendaciones(db);

        funcionalidadesBarraBusqueda();

        //Colores personalizado del saludo al usuario
        TextView saludoUsuario = findViewById(R.id.saludoUsuario);
        saludoUsuario.setTextColor(Color.parseColor("#ff3d00"));
    }

    private void construirListaCategorias(AccesoModelo db){
        //Sacar info de la BD
        int numCategorias = db.getNumCategorias();
        ArrayList<String> nombresCategorias = db.getNombresCategorias();

        LinearLayout layoutHorizontalCategorias = findViewById(R.id.layoutHorizontalCategorias);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT );

        for (int i=0; i<numCategorias; i++){
            Button button = new Button(this);

            //Asignamos propiedades de layout al boton
            lp.setMargins(10, 10, 10, 10);
            button.setLayoutParams(lp);
            button.setBackgroundResource(R.drawable.categoria_background);

            //Asignamos Texto al botón
            button.setText(nombresCategorias.get(i));
            button.setTextColor(Color.BLACK);
            button.setHeight(200);

            String nombre = nombresCategorias.get(i);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Irse a la actividad de buscar comida pasandole parametros de busqueda
                    Intent i = new Intent(InicioActivity.this, BuscarComida.class);
                    i.putExtra("nombre_categoria", nombre); //Pasarle a la nueva actividad por parametro el nombre de la categoria a filtrar
                    startActivity(i);
                }
            });

            //Añadimos el botón a la barra de Scroll Horizontal
            layoutHorizontalCategorias.addView(button);
        }
    }

    private void construirListaRecomendaciones(AccesoModelo db){
        ArrayList <String> nombresComidas = db.getNombreComidasRandom();

        LinearLayout layoutHorizontalRecomendaciones = findViewById(R.id.layoutHorizontalRecomendaciones);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT );


        for (int i=0; i<4; i++){
            Button button = new Button(this);

            //Asignamos propiedades de layout al boton
            lp.setMargins(10, 10, 10, 10);
            button.setLayoutParams(lp);
            button.setBackgroundResource(R.drawable.categoria_background);

            //Asignamos Texto al botón
            button.setText(nombresComidas.get(i));
            button.setTextColor(Color.BLACK);
            button.setHeight(200);

            String nombre = nombresComidas.get(i);
            ArrayList<Integer> codigo_comida = db.getCodigoComidaPorNombre(nombre);

            button.setOnClickListener(view -> {
                //Irse a la actividad de buscar comida pasandole parametros de busqueda
                Intent i1 = new Intent(InicioActivity.this, DetalleComida.class);
                i1.putExtra("codigo_comida", codigo_comida.get(0)); //Pasarle a la nueva actividad por parametro el nombre de la comida a filtrar
                startActivity(i1);
            });

            //Añadimos el botón a la barra de Scroll Horizontal
            layoutHorizontalRecomendaciones.addView(button);
        }

    }

    private void funcionalidadesBarraBusqueda(){
        EditText barraBusqueda = findViewById(R.id.barraBusqueda);

        ImageButton botonBusqueda = findViewById(R.id.botonBusqueda);
        botonBusqueda.setOnClickListener(view -> {
            //Irse a la actividad de buscar comida pasandole parametros de busqueda
            Intent i = new Intent(InicioActivity.this, BuscarComida.class);
            i.putExtra("nombre_comida", barraBusqueda.getText().toString()); //Pasarle a la nueva actividad por parametro el nombre de la comida a filtrar
            startActivity(i);
        });
    }

    private void cambiarActividad() {
        // Cambiar a la actividad de Inicio
        LinearLayout inicio = findViewById(R.id.inicio);
        inicio.setOnClickListener(
                view -> {
                    finish();
                    startActivity(new Intent(InicioActivity.this, InicioActivity.class));
                });

        // Cambiar a la actividad Perfil
        LinearLayout perfil = findViewById(R.id.perfil);
        perfil.setOnClickListener(
                view -> {
                    finish();
                    startActivity(new Intent(InicioActivity.this, PerfilActivity.class));
                });

        // Cambiar a la actividad Carrito
        LinearLayout carrito = findViewById(R.id.carrito);
        carrito.setOnClickListener(
                view -> {
                    finish();
                    startActivity(new Intent(InicioActivity.this, CarritoActivity.class));
                });

        // Cambiar a la actividad Pedidos
        LinearLayout pedidos = findViewById(R.id.pedidos);
        pedidos.setOnClickListener(
                view -> {
                    finish();
                    startActivity(new Intent(InicioActivity.this, PedidosActivity.class));
                });

        // Cambiar a la actividad Ajustes
        LinearLayout ajustes = findViewById(R.id.ajustes);
        ajustes.setOnClickListener(
                view -> {
                    finish();
                    // startActivity(new Intent(InicioActivity.this, AjustesActivity.class));
                });
    }

    @Override
    protected void onPause(){
        super.onPause();
        /*
        SharedPreferences prefs = this.getSharedPreferences("datosOnPause", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        EditText barraBusqueda = findViewById(R.id.barraBusqueda);
        editor.putString("barraBusquedaComida", barraBusqueda.getText().toString());
        editor.apply();
         */
    }

    @Override
    protected void onResume(){
        super.onResume();
        /*
        SharedPreferences prefs = this.getSharedPreferences("datosOnPause", MODE_PRIVATE);
        String nombreComidaBuscar = prefs.getString("barraBusquedaComida", "");
        EditText barraBusqueda = findViewById(R.id.barraBusqueda);
        barraBusqueda.setText(nombreComidaBuscar);
         */
    }

    //Método para controlar que pulsamos la tecla de back en el dispositivo movil
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == event.KEYCODE_BACK){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.cerrarSesionDialog);
            builder.setPositiveButton(R.string.aceptar, (dialog, id) -> finish());
            builder.setNegativeButton(R.string.cancelar, (dialog, id) -> {
                //nothing
            });
            builder.create().show();
        }
        return super.onKeyDown(keyCode, event);
    }
}


