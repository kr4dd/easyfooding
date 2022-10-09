package esei.uvigo.easyfooding;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import esei.uvigo.easyfooding.database.DatabaseAccess;
import esei.uvigo.easyfooding.objetosCarrito.LineaPedidos;

public class TicketActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);
        Bundle datos = getIntent().getExtras();
        getSupportActionBar().hide();
        int num_pedido = datos.getInt("numero_pedido");

        ConstraintLayout back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(TicketActivity.this, PedidosActivity.class));
            }
        });


        ArrayList<LineaPedidos> lineasPedido =  getListaPedidos(num_pedido);
        if(lineasPedido.size() > 0){
            ListView list = findViewById(R.id.ticket);
            TextView vacio = findViewById(R.id.sinEntradas);
            list.setVisibility(View.VISIBLE);
            vacio.setVisibility(View.INVISIBLE);
            ArrayAdapter<LineaPedidos> adapter = new ticketAdapter(TicketActivity.this,0,lineasPedido);
            ListView lineas = findViewById(R.id.ticket);
            lineas.setAdapter(adapter);
        }else{
           ListView list = findViewById(R.id.ticket);
           TextView vacio = findViewById(R.id.sinEntradas);
           list.setVisibility(View.INVISIBLE);
           vacio.setVisibility(View.VISIBLE);
        }
    }

    private ArrayList<LineaPedidos> getListaPedidos(int num_pedido) {
        DatabaseAccess dataBaseAccess = DatabaseAccess.getInstance(getApplicationContext());

        ArrayList<LineaPedidos> l;
        dataBaseAccess.open();
        l = dataBaseAccess.getLineaPedidos(num_pedido);
        dataBaseAccess.close();
        return l;
    }




    public class ticketAdapter extends ArrayAdapter<LineaPedidos>{
        private Context context;
        private List <LineaPedidos> linea;

        public ticketAdapter(@NonNull Context context, int resource, @NonNull ArrayList<LineaPedidos> objects) {
            super(context, resource, objects);
            this.context = context;
            this.linea = objects;
        }
        public View getView(int position, View convertView, ViewGroup parent){
            DatabaseAccess dataBaseAccess = DatabaseAccess.getInstance(getApplicationContext());
            LineaPedidos objetoActual = linea.get(position);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(TicketActivity.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.linea_pedidos_layout,null);

            //antes de setear el contenido tenemos que buscar cual es el nombre del alimento
            dataBaseAccess.open();
            String nombreComida =  dataBaseAccess.getNombreComidaPorId(String.valueOf(objetoActual.getCodigo_comida()));
            dataBaseAccess.close();

            TextView nombre = view.findViewById(R.id.nombre);
            TextView cantidad = view.findViewById(R.id.cantidad);
            TextView precio = view.findViewById(R.id.precio);

            nombre.setText(nombreComida);
            String toShow = "Cantidad: "+ String.valueOf(objetoActual.getCantidad());
            cantidad.setText(toShow);

            double total = objetoActual.getPrecio();
            DecimalFormat df = new DecimalFormat("###,###,###,##0.0");

            String mostar = df.format(total) + "€";

            precio.setText(mostar);

            return  view;
        }
    }
}