package esei.uvigo.easyfooding;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Objects;

import esei.uvigo.easyfooding.database.DatabaseAccess;
import esei.uvigo.easyfooding.objetosCarrito.Carrito;
import esei.uvigo.easyfooding.objetosCarrito.Comida;

public class CarritoActivity extends AppCompatActivity {
  private RecyclerView listaProductos;
  static ArrayList<Comida> listaComida = new ArrayList<>();
  double precioImpuestos;
  double precioEnvio;
  static AdaptadorPedido ap;
  ArrayList<Comida> pago =
      new ArrayList<>(); // se manda a la pestaña de pago para informar de la comida y las
  // cantidades

  @Override
  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    Objects.requireNonNull(getSupportActionBar()).hide();
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
    if(listaComida.size()>0){
      listaComida.clear();
      ap.notifyDataSetChanged();
    }
    recuperarComidasEnCarro();
    if (listaComida.size() < 1) {
      ConstraintLayout vacio = findViewById(R.id.vacio);
      vacio.setVisibility(View.VISIBLE);
    } else {
      ConstraintLayout vacio = findViewById(R.id.vacio);
      vacio.setVisibility(View.INVISIBLE);
      // insertamos los datos en la lista
      crearLista();
    }

    // Accion para mandar al usuario a la actividad de pago
    activarPago();
    setColoresAndroidModoOscuro();
  }

  protected void onResume() {
    super.onResume();
    if(ap!=null){
      ap.notifyDataSetChanged();
    }
  }


  /*---------------------funciones de inicializacion de la lista------------------------------------*/

  private void crearLista() {
    LinearLayoutManager linearLayoutManager =
            new LinearLayoutManager(CarritoActivity.this, LinearLayoutManager.VERTICAL, false);
    listaProductos.setLayoutManager(linearLayoutManager);
    ap = new AdaptadorPedido();
    listaProductos.setAdapter(ap);
  }


  private void recuperarComidasEnCarro() {
    // primero obtenemos todos los elementos del carrito de este usuario:

    DatabaseAccess dataBaseAccess = DatabaseAccess.getInstance(getApplicationContext());
    dataBaseAccess.open();
    ArrayList<Carrito> carro;

    // ejecutamos query
    carro =
            dataBaseAccess.getObjetosEnCarro(
                    OperationsUserActivity.getUserFromSession(
                            this)); // aqui hay que meter el usuario logeado
    dataBaseAccess.close();

    // recogemos los datos de cada comida en el carrito

    for (int i = 0; i < carro.size(); i++) {
      int idComida = carro.get(i).getCodigoComida();
      int cantidad = carro.get(i).getCantidad();
      dataBaseAccess.open();
      Comida c = dataBaseAccess.getDatosComida(idComida, cantidad);
      dataBaseAccess.close();
      listaComida.add(c);
    }

    TextView precio = findViewById(R.id.suma);
    TextView precioComida = findViewById(R.id.precioTotal);

    // una vez rellenada seteamos los valores iniciales si no esta vacia

    if (listaComida.size() > 0) {
      double total = 0;
      for (int i = 0; i < listaComida.size(); i++) {
        total += listaComida.get(i).getPrecio() * listaComida.get(i).getCantidad();
      }
      precioComida.setText(String.valueOf(total));
      double iva = total * precioImpuestos;
      total = total + iva + precioEnvio;
      DecimalFormat df = new DecimalFormat("###,###,###,##0.00");
      precio.setText(df.format(total));
    }
  }

  public void activarPago() {
    ConstraintLayout pagar = findViewById(R.id.pagar);
    if (listaComida.size() > 0) {
      pagar.setOnClickListener(
              new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                  actualizaPago();
                  if (listaComida.size() > 0) {
                    Intent intent = new Intent(CarritoActivity.this, ProcesoPagoActivity.class);
                    TextView total = findViewById(R.id.suma);
                    String suma = total.getText().toString();
                    intent.putExtra("importe", suma);
                    intent.putExtra("datosProductos", listaComida);
                    startActivity(intent);
                    // limpiamos el carrito
                    //int size = listaComida.size();
                    //listaComida.clear();
                    //ap.notifyDataSetChanged();
                  }
                }
              });
    }
  }

  /*---------------------funciones de edicion de la lista------------------------------------*/


  private void eliminar(String nombre, int cantidad) {

    TextView precioTotal = findViewById(R.id.suma);
    TextView precioComida = findViewById(R.id.precioTotal);

    Double vprecioTotal = Double.parseDouble(precioTotal.getText().toString());
    Double vprecioComida = Double.parseDouble(precioComida.getText().toString());
    boolean seguir = true;
    for (int i = 0; i < listaComida.size(); i++) {
      if (listaComida.get(i).getNombre().equals(nombre) && seguir == true) {
        vprecioComida = vprecioComida - listaComida.get(i).getPrecio() * cantidad;
        vprecioTotal = vprecioTotal - listaComida.get(i).getPrecio() * cantidad;
        int codigoComida = listaComida.get(i).getCodigo();
        DecimalFormat df = new DecimalFormat("###,###,###,##0.00");
        precioTotal.setText(df.format(vprecioTotal));
        precioComida.setText(df.format(vprecioComida));
        listaComida.remove(i);
        ap.notifyDataSetChanged();
        //lo eliminamos de la tabla
        DatabaseAccess dataBaseAccess = DatabaseAccess.getInstance(getApplicationContext());
        dataBaseAccess.open();
        int codigoLineaEliminada = dataBaseAccess.getIdLineaConCodigoComida(codigoComida,OperationsUserActivity.getUserFromSession(this));
        boolean a = dataBaseAccess.eliminarProductorCompradosConCodigo(codigoLineaEliminada,OperationsUserActivity.getUserFromSession(this));
        dataBaseAccess.close();
        seguir = false;
      }
    }
  }

  // calcula el precio total y lo asigna
  public void calculoComida() {
    Double total = 0.0;
    TextView suma = findViewById(R.id.suma);
    if (listaComida.size() < 1) {
      suma.setText("0");
    } else {
      for (int i = 0; i < listaComida.size(); i++) {
        total = total + listaComida.get(i).getCantidad() * listaComida.get(i).getPrecio();
      }

      TextView precio_comida = findViewById(R.id.precioTotal);
      precio_comida.setText(String.valueOf(total));

      double impuestos = total * precioImpuestos;
      double sumaImpuestos = total + impuestos;
      double sumaEnvio = sumaImpuestos + precioEnvio;
      DecimalFormat df = new DecimalFormat("###,###,###,##0.00");
      suma.setText(df.format(sumaEnvio));
    }
  }

  public void actualizaPago() {
    pago.clear();
    for (int x = listaProductos.getChildCount(), i = 0; i < x; ++i) {
      RecyclerView.ViewHolder holder =
              listaProductos.getChildViewHolder(listaProductos.getChildAt(i));

      // actualizacion del array
      TextView t = holder.itemView.findViewById(R.id.precio_total); // precio del producto
      TextView nombreComida = holder.itemView.findViewById((R.id.nombre_comida));
      TextView precioUno = holder.itemView.findViewById(R.id.precioUnitario);
      TextView cantidad = holder.itemView.findViewById(R.id.total);
      DatabaseAccess dataBaseAccess = DatabaseAccess.getInstance(getApplicationContext());
      dataBaseAccess.open();
      int codigo = dataBaseAccess.getIdComida(nombreComida.getText().toString());
      dataBaseAccess.close();
      Comida add =
              new Comida(
                      nombreComida.getText().toString(),
                      Double.parseDouble(precioUno.getText().toString()),
                      Integer.parseInt(cantidad.getText().toString()),
                      codigo,
                      Double.parseDouble(t.getText().toString()));
      pago.add(add);
    }
  }

  /*--------------------Adaptador del recycleview-------------------------*/

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
      ImageView borrar;

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
        itemView.setOnLongClickListener(
            new View.OnLongClickListener() {
              @Override
              public boolean onLongClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CarritoActivity.this);
                builder.setTitle(R.string.borrar);
                builder.setMessage(R.string.confirma);
                builder.setPositiveButton(
                    "Confirmar",
                    new DialogInterface.OnClickListener() {
                      @Override
                      public void onClick(DialogInterface dialogInterface, int i) {
                        String eliminar = nombre.getText().toString();
                        int cantidad = Integer.parseInt(cantidadTotal.getText().toString());
                        eliminar(eliminar, cantidad);
                        ap.notifyItemRemoved(getAdapterPosition());
                        calculoComida();
                      }
                    });
                builder.setNegativeButton(
                    "Cancelar",
                    new DialogInterface.OnClickListener() {
                      @Override
                      public void onClick(DialogInterface dialogInterface, int i) {
                        return;
                      }
                    });
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
              }
            });
      }

      public void masProducto(int actual) {
        if (actual < 10) {
          int add = actual + 1;
          double unidad = Double.parseDouble(precioUnitario.getText().toString());
          cantidadTotal.setText(String.valueOf(add));
          double precio = add * unidad;
          precio_total.setText(String.valueOf(precio));
          listaComida.get(getAdapterPosition()).setCantidad(add);
          cantidadTotal.setTextColor(Color.GRAY);
          calculoComida(); // llamamos a la funcion que actualiza el precio total
          DatabaseAccess dataBaseAccess = DatabaseAccess.getInstance(getApplicationContext());
          dataBaseAccess.open();
          //actualizamos cantidad en base de datos
          int idLineaTemp = dataBaseAccess.getIdLineaConCantidad(listaComida.get(getAdapterPosition()).getCodigo(),OperationsUserActivity.getUserFromSession(CarritoActivity.this),actual);
          dataBaseAccess.addUnProductoCarrito(idLineaTemp,add);
          dataBaseAccess.close();
        }
      }

      public void menosProducto(int actual) {
        if (actual > 1) {
          int subs = actual - 1;
          double unidad = Double.parseDouble(precioUnitario.getText().toString());
          cantidadTotal.setText(String.valueOf(add));
          double precio = subs * unidad;
          cantidadTotal.setText(String.valueOf(subs));
          precio_total.setText(String.valueOf(precio));
          listaComida.get(getAdapterPosition()).setCantidad(subs);
          cantidadTotal.setTextColor(Color.GRAY);
          calculoComida(); // llamamos a la funcion que actualiza el precio total
          DatabaseAccess dataBaseAccess = DatabaseAccess.getInstance(getApplicationContext());
          dataBaseAccess.open();
          int idLineaTemp = dataBaseAccess.getIdLineaConCantidad(listaComida.get(getAdapterPosition()).getCodigo(),OperationsUserActivity.getUserFromSession(CarritoActivity.this),actual);
          //actualizamos cantidad en base de datos
          dataBaseAccess.deleteUnProductoCarrito(idLineaTemp,subs);
          dataBaseAccess.close();
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
        cantidadTotal.setTextColor(Color.GRAY);
      }
    }
  }

  /*Funciones auxiliares*/

  // barra navegacion
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

    /*private void rellenarArrays() {

    // listaComida.add(new Comida("Burger simple", 5.25, 2, 1));
    // listaComida.add(new Comida("Patacon", 4.0, 2, 3));
    TextView precio = findViewById(R.id.suma);
    TextView precioComida = findViewById(R.id.precioTotal);

    // una vez rellenada seteamos los valores iniciales si no esta vacia
    if (listaComida.size() > 0) {
      double total = 0;
      for (int i = 0; i < listaComida.size(); i++) {
        total += listaComida.get(i).getPrecio() * listaComida.get(i).getCantidad();
      }
      precioComida.setText(String.valueOf(total));
      double iva = total * precioImpuestos;
      total = total + iva + precioEnvio;
      DecimalFormat df = new DecimalFormat("###,###,###,##0.00");
      precio.setText(df.format(total));
    }
  }*/
}
