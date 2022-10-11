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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import esei.uvigo.easyfooding.database.DatabaseAccess;
import esei.uvigo.easyfooding.objetosCarrito.ListaPedidos;

public class PedidosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos);
        getSupportActionBar().hide();
        cambiarActividad();
        getHistorial();
    }
    private void getHistorial() {
        ArrayList<ListaPedidos> res =  new ArrayList<>();
        DatabaseAccess dataBaseAccess = DatabaseAccess.getInstance(getApplicationContext());
        dataBaseAccess.open();

        //ejecutamos query
        res = dataBaseAccess.historial("pepe");//aqui hay que meter el usuario logeado

        if(res.size() > 0){
            ListView list = findViewById(R.id.ticket);
            TextView vacio = findViewById(R.id.sinPedidos);
            list.setVisibility(View.VISIBLE);
            vacio.setVisibility(View.INVISIBLE);
            //rellenamos la lista llamando a la clase adaptadora
            ArrayAdapter<ListaPedidos> adapter = new listaAdapter(PedidosActivity.this,0,res);
            ListView historial = findViewById(R.id.ticket);
            historial.setAdapter(adapter);
            ArrayList<ListaPedidos> finalRes = res;
            historial.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                    Intent intent = new Intent(PedidosActivity.this,TicketActivity.class);
                    intent.putExtra("numero_pedido", finalRes.get(pos).getNum_pedido());
                    startActivity(intent);
                }
            });
        }else{
            //anunciamos que no hay nada
            ListView list = findViewById(R.id.ticket);
            TextView vacio = findViewById(R.id.sinPedidos);
            list.setVisibility(View.INVISIBLE);
            vacio.setVisibility(View.VISIBLE);
        }
        dataBaseAccess.close();
    }


    //ArrayAdapder para la lista
    public class listaAdapter extends ArrayAdapter<ListaPedidos>{
        private Context context;
        private List<ListaPedidos> listaHistorial;

        public listaAdapter(@NonNull Context context, int resource, ArrayList<ListaPedidos> objetos) {
            super(context, resource, objetos);

            this.context = context;
            this.listaHistorial = objetos;
        }
        //renderizamos la lista con los objetos que obtuvimos

        public View getView(int position, View convertView, ViewGroup parent){
            ListaPedidos objActual = listaHistorial.get(position);

            //inflamos le layout personalizado
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(PedidosActivity.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.historial_compras,null);

            TextView nombre_usuario = view.findViewById(R.id.nombre_usuario);
            TextView fecha_pedido = view.findViewById(R.id.fecha_pedido);
            TextView direccion = view.findViewById(R.id.direccion);
            TextView codigo_postal = view.findViewById(R.id.codigo_postal);
            TextView localidad = view.findViewById(R.id.localidad);
            TextView pTotal = view.findViewById(R.id.pTotal);


            //añadimos los datos a la vista

            nombre_usuario.setText(objActual.getNombreUsuario());
            fecha_pedido.setText(objActual.getFecha());
            direccion.setText(objActual.getDireccion());
            codigo_postal.setText(Float.toString(objActual.getCodioPostal()));
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
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                        startActivity(new Intent(PedidosActivity.this, InicioActivity.class));
                    }
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
}