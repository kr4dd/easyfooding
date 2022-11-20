package esei.uvigo.easyfooding;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import esei.uvigo.easyfooding.database.DatabaseAccess;

public class BuscarComida extends AppCompatActivity {

    private ArrayList<Integer> arrayItemsId; //Guarda los codigos de las comidas del listView
    private ArrayList<String> arrayItems; //Guarda los nombres de las comidas del listView

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buscar_comida_activity);

        //Ocultar la barra con el titulo
        getSupportActionBar().hide();

        //Conectar a BD
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getApplicationContext());

        //Conectar ListView con el Adaptador y con el Array de resultados
        ListView listViewItems = findViewById(R.id.listViewItems);
        this.arrayItemsId = new ArrayList<>();
        this.arrayItems = new ArrayList<>();
        //El adaptador une el ListView con el ArrayList
        ArrayAdapter<String> listaItemsAdapter = new ArrayAdapter<>(
                this.getApplicationContext(),
                android.R.layout.simple_selectable_list_item,
                this.arrayItems
        );
        listViewItems.setAdapter(listaItemsAdapter);

        String nombreCategoria, nombreComida;
        Bundle parametros = getIntent().getExtras();
        TextView titulo_busqueda = findViewById(R.id.textView_titulo_busqueda);
        TextView mensajeBusquedaFallida;
        try {
            if (parametros.get("nombre_categoria") != null) { //Si hemos buscado por categoria
                nombreCategoria = parametros.getString("nombre_categoria");
                titulo_busqueda.setText("Categoría " + nombreCategoria);
                obtenerComidasPorCategoria(nombreCategoria, databaseAccess);
            } else if (parametros.get("nombre_comida") != null) { //Si hemos buscado por nombre de comida
                nombreComida = parametros.getString("nombre_comida");
                titulo_busqueda.setText("Resultados para: " + "'" + nombreComida + "'");
                obtenerComidasPorNombre(nombreComida, databaseAccess);
            } else { //No hay parametros
                titulo_busqueda.setText("");
                mensajeBusquedaFallida = findViewById(R.id.mensajeBusquedaFallida);
                mensajeBusquedaFallida.setText(R.string.busquedafallida);
            }
        } catch (Exception ex) {
            throw new RuntimeException();
        }

        listViewItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                Intent i = new Intent(BuscarComida.this, DetalleComida.class);
                i.putExtra("codigo_comida", arrayItemsId.get(pos).toString()); //Pasarle a la nueva actividad por parametro el id de la comida en la que se ha clickado
                startActivity(i);
            }
        });

        if (arrayItemsId.isEmpty()) {
            mensajeBusquedaFallida = findViewById(R.id.mensajeBusquedaFallida);
            mensajeBusquedaFallida.setText(R.string.busquedafallida);
        }

        //Cambiar colores en el modo noche del dispositivo físico
        setColoresAndroidModoOscuro();

        //Cerrar conexion a BD
        databaseAccess.close();

        //Borrar parametros de la actividad
        parametros.clear();
    }

    private void setColoresAndroidModoOscuro() {
        //Colores del mensaje de busqueda sin resultados
        TextView textoAjustes = findViewById(R.id.mensajeBusquedaFallida);
        textoAjustes.setTextColor(getColor(R.color.app_text_color));
    }

    //Rellenar ListView con comida filtrada por categoria
    private void obtenerComidasPorCategoria(String nombreCategoria, DatabaseAccess databaseAccess) {
        databaseAccess.open();
        String codigoComida;
        this.arrayItemsId = databaseAccess.getCodigoComidaPorCategoria(nombreCategoria); //Obtener id de las comidas
        for (int i = 0; i < arrayItemsId.size(); i++) {
            codigoComida = arrayItemsId.get(i).toString();
            arrayItems.add(databaseAccess.getNombreComidaPorId(codigoComida)); //Obtener nombres de las comidas
            //La comida ya se añade automaticamente a la ListView porque esta conectada con un Adaptador
        }
        databaseAccess.close();
    }

    private void obtenerComidasPorNombre(String nombreComida, DatabaseAccess databaseAccess) {
        databaseAccess.open();
        String codigoComida;
        this.arrayItemsId = databaseAccess.getCodigoComidaPorNombre(nombreComida); //Obtener id de las comidas filtradas por su nombre
        for (int i = 0; i < arrayItemsId.size(); i++) {
            codigoComida = arrayItemsId.get(i).toString();
            arrayItems.add(databaseAccess.getNombreComidaPorId(codigoComida)); //Obtener nombres de las comidas
            //La comida ya se añade automaticamente a la ListView porque esta conectada con un Adaptador
        }
        databaseAccess.close();
    }
}
