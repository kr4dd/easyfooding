package esei.uvigo.easyfooding.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
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
import java.util.Objects;

import esei.uvigo.easyfooding.core.OperationsUser;
import esei.uvigo.easyfooding.R;
import esei.uvigo.easyfooding.core.ListaPedidos;
import esei.uvigo.easyfooding.model.AccesoModelo;

public class PedidosActivity extends AppCompatActivity {
    private ArrayList<ListaPedidos> res;
    private ArrayAdapter<ListaPedidos> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos);
        Objects.requireNonNull(getSupportActionBar()).hide();

        //Cambiar de actividades
        OperationsUser.cambiarActividadPanelInterno(getWindow().getDecorView(), this, this);

        this.res = new ArrayList<>();

        AccesoModelo db = new AccesoModelo(this);
        // ejecutamos query
        res = db.historial(OperationsUser.getUserFromSession(this)); // aqui hay que meter el usuario logeado

        this.adapter = new listaAdapter(PedidosActivity.this, 0, res);
        ListView ticket = this.findViewById(R.id.ticket);
        ticket.setAdapter(this.adapter);
        ticket.setOnItemClickListener((adapterView, view, pos, l) -> {
            Intent intent = new Intent(PedidosActivity.this, TicketActivity.class);
            intent.putExtra("numero_pedido", res.get(pos).getNum_pedido());
            startActivity(intent);
        });

        if (res.size() < 1) {
            // anunciamos que no hay nada
            ListView list = findViewById(R.id.ticket);
            TextView vacio = findViewById(R.id.sinPedidos);
            list.setVisibility(View.INVISIBLE);
            vacio.setVisibility(View.VISIBLE);
        } else {
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
        AccesoModelo db = new AccesoModelo(this);
        ArrayList<ListaPedidos> nuevaLista = db.historial(OperationsUser.getUserFromSession(this));
        adapter.clear();
        adapter.addAll(nuevaLista);
        adapter.notifyDataSetChanged();
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

        @SuppressLint("SetTextI18n")
        public View getView(int position, View convertView, ViewGroup parent) {
            ListaPedidos objActual = listaHistorial.get(position);

            // inflamos le layout personalizado
            LayoutInflater inflater =
                    (LayoutInflater) context.getSystemService(PedidosActivity.LAYOUT_INFLATER_SERVICE);
            @SuppressLint("ViewHolder") View view = inflater.inflate(R.layout.historial_compras, null);

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
            pTotal.setText(df.format(total) + "€");

            return view;
        }
    }

}
