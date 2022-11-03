package esei.uvigo.easyfooding;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import esei.uvigo.easyfooding.database.DatabaseAccess;
import esei.uvigo.easyfooding.objetosCarrito.ListaPedidos;

public class PedidosActivity extends AppCompatActivity {
  private ArrayList<ListaPedidos> res;
  private ArrayAdapter<ListaPedidos> adapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_pedidos);
    getSupportActionBar().hide();
    cambiarActividad();
    setColoresAndroidModoOscuro();
    this.res = new ArrayList<ListaPedidos>();

    DatabaseAccess dataBaseAccess = DatabaseAccess.getInstance(getApplicationContext());
    dataBaseAccess.open();
    // ejecutamos query
    res =  dataBaseAccess.historial(OperationsUserActivity.getUserFromSession(this)); // aqui hay que meter el usuario logeado
    dataBaseAccess.close();

    this.adapter = new listaAdapter(PedidosActivity.this, 0, res);
    ListView ticket = (ListView) this.findViewById(R.id.ticket);
    ticket.setAdapter(this.adapter);
    ticket.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
        Intent intent = new Intent(PedidosActivity.this, TicketActivity.class);
        intent.putExtra("numero_pedido", res.get(pos).getNum_pedido());
        startActivity(intent);
      }
    });
    if(res.size()<1){
      // anunciamos que no hay nada
      ListView list = findViewById(R.id.ticket);
      TextView vacio = findViewById(R.id.sinPedidos);
      list.setVisibility(View.INVISIBLE);
      vacio.setVisibility(View.VISIBLE);
    }else{
      ListView list = findViewById(R.id.ticket);
      TextView vacio = findViewById(R.id.sinPedidos);
      list.setVisibility(View.VISIBLE);
      vacio.setVisibility(View.INVISIBLE);
    }
  }


  //refresco de la lista con los nuevos datos de la BD
  @Override
  protected void onResume() {
    super.onResume();
    ListView ticket = (ListView) this.findViewById(R.id.ticket);
    DatabaseAccess dataBaseAccess = DatabaseAccess.getInstance(getApplicationContext());
    dataBaseAccess.open();
    ArrayList<ListaPedidos> nuevaLista = dataBaseAccess.historial(OperationsUserActivity.getUserFromSession(this));
    adapter.clear();
    adapter.addAll(nuevaLista);
    adapter.notifyDataSetChanged();
    dataBaseAccess.close();
  }

  // ArrayAdapder para la lista
  public static class listaAdapter extends ArrayAdapter<ListaPedidos> {
    private Context context;
    private List<ListaPedidos> listaHistorial;

    public listaAdapter(@NonNull Context context, int resource, ArrayList<ListaPedidos> objetos) {
      super(context, resource, objetos);

      this.context = context;
      this.listaHistorial = objetos;
    }
    // renderizamos la lista con los objetos que obtuvimos

    public View getView(int position, View convertView, ViewGroup parent) {
      ListaPedidos objActual = listaHistorial.get(position);

      // inflamos le layout personalizado
      LayoutInflater inflater =
          (LayoutInflater) context.getSystemService(PedidosActivity.LAYOUT_INFLATER_SERVICE);
      View view = inflater.inflate(R.layout.historial_compras, null);

      TextView fecha_pedido = view.findViewById(R.id.fecha_pedido);
      TextView direccion = view.findViewById(R.id.direccion);
      TextView codigo_postal = view.findViewById(R.id.codigo_postal);
      TextView localidad = view.findViewById(R.id.localidad);
      TextView pTotal = view.findViewById(R.id.pTotal);

      // añadimos los datos a la vista

      fecha_pedido.setText(objActual.getFecha());
      direccion.setText(objActual.getDireccion());
      codigo_postal.setText(Integer.toString(objActual.getCodioPostal()));
      localidad.setText(objActual.getLocalidad());

      double total = objActual.getImporte();
      DecimalFormat df = new DecimalFormat("###,###,###,##0.0");
      String mostar = df.format(total) + "€";
      pTotal.setText(mostar);

      return view;
    }
  }

  private void cambiarActividad() {

    // Cambiar a la actividad de Inicio
    LinearLayout inicio = findViewById(R.id.inicio);
    inicio.setOnClickListener(
        view -> {
          finish();
          startActivity(new Intent(PedidosActivity.this, InicioActivity.class));
        });

    // Cambiar a la actividad Perfil
    LinearLayout perfil = findViewById(R.id.perfil);
    perfil.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            finish();
            startActivity(new Intent(PedidosActivity.this, PerfilActivity.class));
          }
        });

    // Cambiar a la actividad Carrito
    LinearLayout carrito = findViewById(R.id.carrito);
    carrito.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            finish();
            startActivity(new Intent(PedidosActivity.this, CarritoActivity.class));
          }
        });

    // Cambiar a la actividad Pedidos
    LinearLayout pedidos = findViewById(R.id.pedidos);
    pedidos.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            finish();
            startActivity(new Intent(PedidosActivity.this, PedidosActivity.class));
          }
        });

    // Cambiar a la actividad Ajustes
    LinearLayout ajustes = findViewById(R.id.ajustes);
    ajustes.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            finish();
            // startActivity(new Intent(InicioActivity.this, AjustesActivity.class));
          }
        });
  }

  private void setColoresAndroidModoOscuro() {
    // Colores de los textos de la navbar inferior
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
}
