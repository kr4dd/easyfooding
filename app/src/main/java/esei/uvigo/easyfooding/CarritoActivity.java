package esei.uvigo.easyfooding;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;

import esei.uvigo.easyfooding.objetosCarrito.Comida;

public class CarritoActivity extends AppCompatActivity {
  private RecyclerView listaProductos;
  private ArrayList<Comida> listaComida = new ArrayList<>();
  double precioImpuestos;
  double precioEnvio;

  @Override
  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    getSupportActionBar().hide();
    setContentView(R.layout.activity_carrito);

    listaProductos = findViewById(R.id.listaComida);
    precioImpuestos = 0.21;
    precioEnvio = 3.50;

    TextView gobierno = findViewById(R.id.impuesto);
    TextView gasofa = findViewById(R.id.envio);
    gobierno.setText(String.valueOf(precioImpuestos));
    gasofa.setText(String.valueOf(precioEnvio));

    // para la barra de movimientos
    cambiarActividad();
    // Recuperamos los datos de compra
    rellenarArrays();
    // insertamos los datos en la lista
    crearLista();

    // Accion para mandar al usuario a la actividad de pago
    ConstraintLayout pagar = findViewById(R.id.pagar);
    pagar.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            // TODO, hacer la funcion de pago mandando lo que tenga el TextView a otra pestaña
          }
        });
  }

  // metodo que nos permite rellenar el array de objetos comida para mostrarlo luego en la lista
  private void rellenarArrays() {

    listaComida.add(new Comida("Banana", R.drawable.banana, 5.50, 2, 8));
    TextView precio = findViewById(R.id.suma);

    // una vez rellenada seteamos los valores iniciales
    double total = 0;
    for (int i = 0; i < listaComida.size(); i++) {
      total += listaComida.get(i).getPrecio() * listaComida.get(i).getCantidad();
      Toast.makeText(CarritoActivity.this, String.valueOf(total), Toast.LENGTH_LONG).show();
    }
    double iva = total * precioImpuestos;
    total = total + iva + precioEnvio;
    DecimalFormat df = new DecimalFormat("###,###,###,##0.00");
    precio.setText(df.format(total));
  }




  private void crearLista() {
    LinearLayoutManager linearLayoutManager =
        new LinearLayoutManager(CarritoActivity.this, LinearLayoutManager.VERTICAL, false);
    listaProductos.setLayoutManager(linearLayoutManager);
    listaProductos.setAdapter(new AdaptadorPedido());
  }

  // metodos para la lista de productos

  private class AdaptadorPedido
      extends RecyclerView.Adapter<AdaptadorPedido.AdaptadorPedidoHolder> {
    @NonNull
    @Override
    public AdaptadorPedidoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      return new AdaptadorPedidoHolder(
          getLayoutInflater().inflate(R.layout.lista_carrito, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorPedidoHolder holder, int position) {
      holder.imprimir(position);
    }

    @Override
    public int getItemCount() {
      return listaComida.size();
    }

    public class AdaptadorPedidoHolder extends RecyclerView.ViewHolder {
      ImageView comida;
      TextView nombre;
      TextView precioUnitario;
      TextView precio_total;
      ImageView resta;
      ImageView add;
      TextView cantidadTotal;

      public AdaptadorPedidoHolder(@NonNull View itemView) {
        super(itemView);
        comida = itemView.findViewById(R.id.imagenComida);
        nombre = itemView.findViewById(R.id.nombre_comida);
        precioUnitario = itemView.findViewById(R.id.precioUnitario);
        precio_total = itemView.findViewById(R.id.precio_total);
        resta = itemView.findViewById(R.id.resta);
        add = itemView.findViewById(R.id.add);
        cantidadTotal = itemView.findViewById(R.id.total);

        // para añadir uno mas
        add.setOnClickListener(
            new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                int actual = Integer.parseInt(cantidadTotal.getText().toString());
                masProducto(actual);
              }
            });

        // para retirar un producto
        resta.setOnClickListener(
            new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                int actual = Integer.parseInt(cantidadTotal.getText().toString());
                menosProducto(actual);
              }
            });
      }

      public void masProducto(int actual) {
        int add = actual + 1;
        double unidad = Double.parseDouble(precioUnitario.getText().toString());
        cantidadTotal.setText(String.valueOf(add));
        double precio = add * unidad;
        precio_total.setText(String.valueOf(precio));
        calculoComida(); // llamamos a la funcion que actualiza el precio total
      }

      public void menosProducto(int actual) {
        if (actual > 0) {
          int subs = actual - 1;
          double unidad = Double.parseDouble(precioUnitario.getText().toString());
          cantidadTotal.setText(String.valueOf(add));
          double precio = subs * unidad;
          cantidadTotal.setText(String.valueOf(subs));
          precio_total.setText(String.valueOf(precio));
          calculoComida();
        }
      }

      // por cada elemento de la lista imprimimos asignando al layout los elementos
      public void imprimir(int position) {
        comida.setImageResource(listaComida.get(position).getImagen());
        nombre.setText(listaComida.get(position).getNombre());
        cantidadTotal.setText(String.valueOf(listaComida.get(position).getCantidad()));
        // precio de cada uno de los productos
        precioUnitario.setText(String.valueOf(listaComida.get(position).getPrecio()));
        int totalActual = Integer.parseInt(cantidadTotal.getText().toString());
        // en funcion del numero de productos calculamos el total de precio actual
        double precio = totalActual * listaComida.get(position).getPrecio();
        precio_total.setText(String.valueOf(precio));
      }

      // calcula el precio total y lo asigna
      public void calculoComida() {
        double precioTotal = 0;
        for (int x = listaProductos.getChildCount(), i = 0; i < x; ++i) {
          RecyclerView.ViewHolder holder =
              listaProductos.getChildViewHolder(listaProductos.getChildAt(i));
          TextView t = holder.itemView.findViewById(R.id.precio_total);
          double actual = Double.parseDouble(t.getText().toString());
          precioTotal += actual;
        }
        TextView precioComida = findViewById(R.id.precioTotal);
        precioComida.setText(String.valueOf(precioTotal));
        double iva = precioTotal * precioImpuestos;
        precioTotal = precioTotal + iva + precioEnvio;
        TextView total = findViewById(R.id.suma);
        DecimalFormat df = new DecimalFormat("###,###,###,##0.00");
        total.setText(df.format(precioTotal));
      }
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
            startActivity(new Intent(CarritoActivity.this, InicioActivity.class));
          }
        });

    // Cambiar a la actividad Perfil
    LinearLayout perfil = findViewById(R.id.perfil);
    perfil.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            finish();
            startActivity(new Intent(CarritoActivity.this, PerfilActivity.class));
          }
        });

    // Cambiar a la actividad Carrito
    LinearLayout carrito = findViewById(R.id.carrito);
    carrito.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            finish();
            startActivity(new Intent(CarritoActivity.this, CarritoActivity.class));
          }
        });

    // Cambiar a la actividad Pedidos
    LinearLayout pedidos = findViewById(R.id.pedidos);
    pedidos.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            finish();
            startActivity(new Intent(CarritoActivity.this, PedidosActivity.class));
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
