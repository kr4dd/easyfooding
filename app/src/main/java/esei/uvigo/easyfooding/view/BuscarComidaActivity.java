package esei.uvigo.easyfooding.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Objects;

import esei.uvigo.easyfooding.R;
import esei.uvigo.easyfooding.model.ModeloInicio;

public class BuscarComidaActivity extends AppCompatActivity {

    private ArrayList<Integer> arrayItemsId; //Guarda los codigos de las comidas del listView
    private ArrayList<String> arrayItems; //Guarda los nombres de las comidas del listView

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buscar_comida_activity);

        //Ocultar la barra con el titulo
        Objects.requireNonNull(getSupportActionBar()).hide();

        //Conectar a BD
        ModeloInicio db = new ModeloInicio(this);

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
                obtenerComidasPorCategoria(nombreCategoria, db);
            } else if (parametros.get("nombre_comida") != null) { //Si hemos buscado por nombre de comida
                nombreComida = parametros.getString("nombre_comida");
                titulo_busqueda.setText("Resultados para: " + "'" + nombreComida + "'");
                obtenerComidasPorNombre(nombreComida, db);
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

        //Borrar parametros de la actividad
        parametros.clear();
    }

    private void setColoresAndroidModoOscuro() {
        //Colores del mensaje de busqueda sin resultados
        TextView textoAjustes = findViewById(R.id.mensajeBusquedaFallida);
        textoAjustes.setTextColor(Color.GRAY);
    }

    //Rellenar ListView con comida filtrada por categoria
    private void obtenerComidasPorCategoria(String nombreCategoria, ModeloInicio db) {
        String codigoComida;
        this.arrayItemsId = db.getCodigoComidaPorCategoria(nombreCategoria); //Obtener id de las comidas
        for (int i = 0; i < arrayItemsId.size(); i++) {
            codigoComida = arrayItemsId.get(i).toString();
            arrayItems.add(db.getNombreComidaPorId(codigoComida)); //Obtener nombres de las comidas
            //La comida ya se añade automaticamente a la ListView porque esta conectada con un Adaptador
        }
    }

    private void obtenerComidasPorNombre(String nombreComida, ModeloInicio db) {
        String codigoComida;
        this.arrayItemsId = db.getCodigoComidaPorNombre(nombreComida); //Obtener id de las comidas filtradas por su nombre
        for (int i = 0; i < arrayItemsId.size(); i++) {
            codigoComida = arrayItemsId.get(i).toString();
            arrayItems.add(db.getNombreComidaPorId(codigoComida)); //Obtener nombres de las comidas
            //La comida ya se añade automaticamente a la ListView porque esta conectada con un Adaptador
        }
    }

    //Método para controlar que pulsamos la tecla de back en el dispositivo movil
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == event.KEYCODE_BACK){
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
