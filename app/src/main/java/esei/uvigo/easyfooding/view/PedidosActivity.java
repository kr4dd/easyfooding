package esei.uvigo.easyfooding.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import esei.uvigo.easyfooding.core.OperationsUser;
import esei.uvigo.easyfooding.R;
import esei.uvigo.easyfooding.core.ListaPedidos;
import esei.uvigo.easyfooding.model.AccesoModelo;

public class PedidosActivity extends AppCompatActivity {
    private Cursor cursor;
    private ArrayList<ListaPedidos> res;
    private ArrayAdapter<ListaPedidos> adapter;
    private pedidoCursorAdapter cursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos);
        Objects.requireNonNull(getSupportActionBar()).hide();

        //Cambiar de actividades
        OperationsUser.cambiarActividadPanelInterno(getWindow().getDecorView(), this, this);
        
    }

    @Override
    protected void onStart() {
        super.onStart();
        AccesoModelo db = new AccesoModelo(this);
        cursor = db.getHistorialCursor(OperationsUser.getUserFromSession(this));
        ListView ticket = this.findViewById(R.id.ticket);

        cursorAdapter = new pedidoCursorAdapter(this,cursor,0);
        ticket.setAdapter(adapter);
    ticket.setOnItemClickListener(
        new AdapterView.OnItemClickListener() {
          @SuppressLint("Range")
          @Override
          public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(PedidosActivity.this, TicketActivity.class);
            intent.putExtra("numero_pedido", Integer.parseInt(cursor.getString(cursor.getColumnIndex("_id"))));
            startActivity(intent);
          }
        });
    }

    //refresco de la lista con los nuevos datos de la BD
    @Override
    protected void onResume() {
        super.onResume();
        AccesoModelo db = new AccesoModelo(this);
        ListView ticket = this.findViewById(R.id.ticket);
        cursor = db.getHistorialCursor(OperationsUser.getUserFromSession(this));
        pedidoCursorAdapter cursorAdapterNuevo = new pedidoCursorAdapter(this,cursor,0);
        ticket.setAdapter(null);
        ticket.setAdapter(cursorAdapterNuevo);
    }

    public class pedidoCursorAdapter extends CursorAdapter{
        private LayoutInflater cursorInflater;
        public pedidoCursorAdapter(Context context, Cursor cursor, int flags){
            super(context,cursor, flags);
            cursorInflater = (LayoutInflater) context.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
        }
        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return cursorInflater.inflate(R.layout.historial_compras,parent,false);
        }

        @SuppressLint("Range")
        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            TextView fecha_pedido = view.findViewById(R.id.fecha_pedido);
            TextView direccion = view.findViewById(R.id.direccion);
            TextView codigo_postal = view.findViewById(R.id.codigo_postal);
            TextView localidad = view.findViewById(R.id.localidad);
            TextView pTotal = view.findViewById(R.id.pTotal);

            fecha_pedido.setText(cursor.getString(cursor.getColumnIndex("fecha_pedido")));
            direccion.setText(cursor.getString(cursor.getColumnIndex("direccion_envio")));
            codigo_postal.setText(cursor.getString(cursor.getColumnIndex("codigo_postal_envio")));
            localidad.setText(cursor.getString(cursor.getColumnIndex("localidad_envio")));

            double total =Double.parseDouble(cursor.getString(cursor.getColumnIndex("importe_total")));
            DecimalFormat df = new DecimalFormat("###,###,###,##0.0");
            pTotal.setText(df.format(total) + "€");
        }
    }
}
