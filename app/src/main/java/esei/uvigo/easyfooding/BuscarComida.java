package esei.uvigo.easyfooding;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import esei.uvigo.easyfooding.database.DatabaseAccess;

public class BuscarComida extends AppCompatActivity {

    private ListView listViewItems;
    private TextView mensajeBusquedaVacia;
    private ArrayAdapter<String> listaItemsAdapter;
    private ArrayList<Integer> arrayItemsId; //Guarda los codigos de las comidas del listView
    private ArrayList<String>  arrayItems; //Guarda los nombres de las comidas del listView


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buscar_comida_activity);

        //Ocultar la barra con el titulo
        getSupportActionBar().hide();

        //Conectar a BD
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getApplicationContext());

        //Conectar ListView con el Adaptador y con el Array de resultados
        this.listViewItems = findViewById(R.id.listViewItems);
        this.arrayItemsId = new ArrayList<Integer>();
        this.arrayItems = new ArrayList<String>();
        //El adaptador une el ListView con el ArrayList
        this.listaItemsAdapter = new ArrayAdapter<String>(
                this.getApplicationContext(),
                android.R.layout.simple_selectable_list_item,
                this.arrayItems
        );
        listViewItems.setAdapter(this.listaItemsAdapter);

        String nombreCategoria;
        String nombreComida;
        Bundle parametros = getIntent().getExtras();
        try{
            if (parametros.get("nombre_categoria") != null) {
                nombreCategoria = parametros.getString("nombre_categoria");
                //Toast.makeText( this, nombreCategoria, Toast.LENGTH_SHORT ).show();
                obtenerComidasPorCategoria(nombreCategoria, databaseAccess);
            } else if (parametros.get("nombre_comida") != null) {
                nombreComida = parametros.getString("nombre_comida");
                Toast.makeText( this, nombreComida, Toast.LENGTH_SHORT ).show();
                obtenerComidasPorNombre(nombreComida, databaseAccess);
            } else { //No hay parametros
                this.mensajeBusquedaVacia = findViewById(R.id.mensajeBusquedaVacia);
                this.mensajeBusquedaVacia.setText("No se han encontrado resultados para tu búsqueda. Prueba otra vez.");
            }
        } catch(Exception ex){
        }

        //TODO ver detalles de la comida al clickar en ella
        listViewItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                //showOptionsDialog(pos);
            }
        });


        if(arrayItemsId.isEmpty()){
            this.mensajeBusquedaVacia = findViewById(R.id.mensajeBusquedaVacia);
            this.mensajeBusquedaVacia.setText("No se han encontrado resultados para tu búsqueda. Prueba otra vez.");
        }

        //Cambiar colores en el modo noche del dispositivo físico
        setColoresAndroidModoOscuro();

        //Cerrar conexion a BD
        databaseAccess.close();

        //Borrar parametros de la actividad
        parametros.clear();
    }

    private void setColoresAndroidModoOscuro(){
        //Colores del mensaje de busqueda sin resultados
        TextView textoAjustes = findViewById(R.id.mensajeBusquedaVacia);
        textoAjustes.setTextColor(Color.GRAY);
    }

    //Rellenar ListView con comida filtrada por categoria
    private void obtenerComidasPorCategoria(String nombreCategoria, DatabaseAccess databaseAccess) {
        databaseAccess.open();

        String codigoComida;
        this.arrayItemsId = databaseAccess.getCodigoComidaPorCategoria(nombreCategoria); //Obtener id de las comidas

        for(int i = 0; i < arrayItemsId.size(); i++){
            codigoComida = arrayItemsId.get(i).toString();
            arrayItems.add(databaseAccess.getNombreComidaPorId(codigoComida)); //Obtener nombres de las comidas
            //La comida ya se añade automaticamente a la ListView porque esta conectada con un Adaptador
        }
        databaseAccess.close();
    }

    //TODO rellenar ListView con comida filtrada por nombre
    private void obtenerComidasPorNombre(String nombreComida, DatabaseAccess databaseAccess) {


    }


}
