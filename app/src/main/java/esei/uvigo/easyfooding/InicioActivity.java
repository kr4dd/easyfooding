package esei.uvigo.easyfooding;

import esei.uvigo.easyfooding.database.DatabaseAccess;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class InicioActivity extends AppCompatActivity {

    private RecyclerView.Adapter adapterCategorias, adapterRecomendaciones;
    private RecyclerView recyclerViewCategorias, recyclerViewRecomendaciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inicio_activity);

        //Ocultar la barra con el titulo
        getSupportActionBar().hide();

        //Cambiar colores en el modo noche del dispositivo físico
        setColoresAndroidModoOscuro();

        //Cambiar de actividades
        cambiarActividad();

        //Crear instancia de la BD
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getApplicationContext());

        //Generar dinamicamente ScrollViewHorizontal de categorias
        construirListaCategorias(databaseAccess);

        //TODO Generar dinamicamente ScrollViewHorizontal de recomendaciones
        //construirListaRecomendaciones(databaseAccess);

        databaseAccess.close();
    }

    private void setColoresAndroidModoOscuro(){

        //Colores del saludo al usuario
        TextView saludoUsuario = findViewById(R.id.saludoUsuario);
        saludoUsuario.setTextColor(Color.parseColor("#ff3d00"));

        //Colores de la barra de busqueda
        EditText barraBusqueda = findViewById(R.id.barraBusqueda);
        barraBusqueda.setHintTextColor(Color.GRAY);
        barraBusqueda.setTextColor(Color.BLACK);

        //Colores de los textos de la navbar inferior
        TextView textoInicio = findViewById(R.id.textoInicio);
        textoInicio.setTextColor(Color.GRAY);
        TextView textoPerfil = findViewById(R.id.textoPerfil);
        textoPerfil.setTextColor(Color.GRAY);
        TextView textoCarrito = findViewById(R.id.textoCarrito);
        textoCarrito.setTextColor(Color.GRAY);
        TextView textoPedidos = findViewById(R.id.textoPedidos);
        textoPedidos.setTextColor(Color.GRAY);
        TextView textoAjustes = findViewById(R.id.textoAjustes);
        textoAjustes.setTextColor(Color.GRAY);
    }

    private void cambiarActividad(){

        //Cambiar a la actividad de Inicio
        LinearLayout inicio = findViewById(R.id.inicio);
        inicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(InicioActivity.this, InicioActivity.class));
            }
        });

        //Cambiar a la actividad Perfil
        LinearLayout perfil = findViewById(R.id.perfil);
        perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(InicioActivity.this, PerfilActivity.class));
            }
        });

        //Cambiar a la actividad Carrito
        LinearLayout carrito = findViewById(R.id.carrito);
        carrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                //startActivity(new Intent(InicioActivity.this, CarritoActivity.class));
            }
        });

        //Cambiar a la actividad Pedidos
        LinearLayout pedidos = findViewById(R.id.pedidos);
        pedidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                //startActivity(new Intent(InicioActivity.this, PedidosActivity.class));
            }
        });

        //Cambiar a la actividad Ajustes
        LinearLayout ajustes = findViewById(R.id.ajustes);
        ajustes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                //startActivity(new Intent(InicioActivity.this, AjustesActivity.class));
            }
        });
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

    //TODO
    private void construirListaRecomendaciones(DatabaseAccess databaseAccess){


    }

}


