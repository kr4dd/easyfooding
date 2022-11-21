package esei.uvigo.easyfooding.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import java.util.ArrayList;
import java.util.Objects;

import esei.uvigo.easyfooding.R;
import esei.uvigo.easyfooding.core.OperationsUser;
import esei.uvigo.easyfooding.model.ModeloInicio;

public class InicioActivity extends AppCompatActivity {
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Set night mode if needed
        SharedPreferences sharedPreferences = this.getSharedPreferences("data", Context.MODE_PRIVATE);

        if(sharedPreferences.getString("theme", "").equals("dark"));
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        //Ocultar la barra con el titulo
        Objects.requireNonNull(getSupportActionBar()).hide();

        //Cargar saludo a usuario
        @SuppressLint("CutPasteId") TextView msgBienvenida = findViewById(R.id.saludoUsuario);
        msgBienvenida.setText("Hola, " + OperationsUser.getUserFromSession(this));

        //Cambiar de actividades
        OperationsUser.cambiarActividadPanelInterno(getWindow().getDecorView(), this, this);

        //Crear instancia de la BD
        ModeloInicio db = new ModeloInicio(this);

        //Generar dinamicamente ScrollViewHorizontal de categorias
        construirListaCategorias(db);

        //Generar dinamicamente ScrollViewHorizontal de recomendaciones
        construirListaRecomendaciones(db);

        funcionalidadesBarraBusqueda();

        //Colores personalizado del saludo al usuario
        @SuppressLint("CutPasteId") TextView saludoUsuario = findViewById(R.id.saludoUsuario);
        saludoUsuario.setTextColor(Color.parseColor("#ff3d00"));
    }

    private void construirListaCategorias(ModeloInicio db){
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
            button.setTextColor(getColor(R.color.app_text_color));
            button.setHeight(200);

            String nombre = nombresCategorias.get(i);
            button.setOnClickListener(view -> {
                //Irse a la actividad de buscar comida pasandole parametros de busqueda
                Intent i1 = new Intent(InicioActivity.this, BuscarComidaActivity.class);
                i1.putExtra("nombre_categoria", nombre); //Pasarle a la nueva actividad por parametro el nombre de la categoria a filtrar
                startActivity(i1);
            });

            //Añadimos el botón a la barra de Scroll Horizontal
            layoutHorizontalCategorias.addView(button);
        }
    }

    private void construirListaRecomendaciones(ModeloInicio db){
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
            button.setTextColor(getColor(R.color.app_text_color));
            button.setHeight(200);

            String nombre = nombresComidas.get(i);
            ArrayList<Integer> codigo_comida = db.getCodigoComidaPorNombre(nombre);

            button.setOnClickListener(view -> {
                //Irse a la actividad de buscar comida pasandole parametros de busqueda
                Intent i1 = new Intent(InicioActivity.this, DetalleComidaActivity.class);
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
            Intent i = new Intent(InicioActivity.this, BuscarComidaActivity.class);
            i.putExtra("nombre_comida", barraBusqueda.getText().toString()); //Pasarle a la nueva actividad por parametro el nombre de la comida a filtrar
            startActivity(i);
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

}


