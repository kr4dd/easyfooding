package esei.uvigo.easyfooding.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;
import java.util.Objects;

import esei.uvigo.easyfooding.R;
import esei.uvigo.easyfooding.model.ModeloInicio;
import esei.uvigo.easyfooding.model.ModeloTicket;

public class TicketActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);
        Objects.requireNonNull(getSupportActionBar()).hide();
    }
    protected void onStart() {

        super.onStart();
        Bundle datos = getIntent().getExtras();
        int numPedido = datos.getInt("numero_pedido");
        Cursor listaPedidos = getCursorListaPedidos(numPedido);
        if(listaPedidos.moveToNext()){
            ListView list = findViewById(R.id.ticket);
            TextView vacio = findViewById(R.id.sinEntradas);
            list.setVisibility(View.VISIBLE);
            vacio.setVisibility(View.INVISIBLE);
            ListView lineas = findViewById(R.id.ticket);
            ticketCursorAdapter adapter = new ticketCursorAdapter(this,listaPedidos,0);
            lineas.setAdapter(adapter);
        }
    }

    private Cursor getCursorListaPedidos(int numPedidos){
        ModeloTicket db = new ModeloTicket(this);
        return db.getCursorLineaPedidos(numPedidos);

    }

    public class ticketCursorAdapter extends CursorAdapter{
        private LayoutInflater cursorInflater;
        public ticketCursorAdapter(Context context, Cursor cursor, int flags){
            super(context,cursor, flags);
            cursorInflater = (LayoutInflater) context.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return cursorInflater.inflate(R.layout.linea_pedidos_layout,parent,false);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            ModeloInicio db = new ModeloInicio(getApplicationContext());
            @SuppressLint("Range") String idComida = cursor.getString(cursor.getColumnIndex("codigo_comida"));
            String nombreComida = db.getNombreComidaPorId(idComida);

            TextView nombre = view.findViewById(R.id.nombre);
            TextView cantidad = view.findViewById(R.id.cantidad);
            TextView precio = view.findViewById(R.id.precio);
            nombre.setText(nombreComida);
            @SuppressLint("Range") String toShow = "Cantidad: " + cursor.getString(cursor.getColumnIndex("cantidad"));
            cantidad.setText(toShow);

            @SuppressLint("Range") double total = cursor.getDouble(cursor.getColumnIndex("precio_linea"));
            DecimalFormat df = new DecimalFormat("###,###,###,##0.0");

            precio.setText(df.format(total) + "€");
        }
    }
}