package esei.uvigo.easyfooding;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Objects;

import esei.uvigo.easyfooding.database.DatabaseAccess;

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
        msgBienvenida.setText("Hola, " + OperationsUserActivity.getUserFromSession(this));

        //Cambiar de actividades
        cambiarActividad();

        //Crear instancia de la BD
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getApplicationContext());

        //Generar dinamicamente ScrollViewHorizontal de categorias
        construirListaCategorias(databaseAccess);

        //Generar dinamicamente ScrollViewHorizontal de recomendaciones
        construirListaRecomendaciones(databaseAccess);

        funcionalidadesBarraBusqueda();

        databaseAccess.close();
    }

    private void construirListaCategorias(DatabaseAccess databaseAccess){
        //Sacar info de la BD
        databaseAccess.open();
        int numCategorias = databaseAccess.getNumCategorias();
        ArrayList<String> nombresCategorias = databaseAccess.getNombresCategorias();
        databaseAccess.close();

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

    private void construirListaRecomendaciones(DatabaseAccess databaseAccess){
        databaseAccess.open();
        ArrayList <String> nombresComidas = databaseAccess.getNombreComidasRandom();
        databaseAccess.close();

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


            databaseAccess.open();
            String nombre = nombresComidas.get(i);
            ArrayList<Integer> codigo_comida = databaseAccess.getCodigoComidaPorNombre(nombre);
            databaseAccess.close();

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Irse a la actividad de buscar comida pasandole parametros de busqueda
                    Intent i = new Intent(InicioActivity.this, DetalleComida.class);
                    i.putExtra("codigo_comida", codigo_comida.get(0)); //Pasarle a la nueva actividad por parametro el nombre de la comida a filtrar
                    startActivity(i);
                }
            });

            //Añadimos el botón a la barra de Scroll Horizontal
            layoutHorizontalRecomendaciones.addView(button);
        }

    }

    private void funcionalidadesBarraBusqueda(){
        EditText barraBusqueda = findViewById(R.id.barraBusqueda);

        ImageButton botonBusqueda = findViewById(R.id.botonBusqueda);
        botonBusqueda.setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View view) {
                //Irse a la actividad de buscar comida pasandole parametros de busqueda
                Intent i = new Intent(InicioActivity.this, BuscarComida.class);
                i.putExtra("nombre_comida", barraBusqueda.getText().toString()); //Pasarle a la nueva actividad por parametro el nombre de la comida a filtrar
                startActivity(i);
            }
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

}


