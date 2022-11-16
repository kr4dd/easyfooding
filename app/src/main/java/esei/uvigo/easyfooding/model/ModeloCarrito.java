package esei.uvigo.easyfooding.model;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;

import java.util.ArrayList;

import esei.uvigo.easyfooding.R;
import esei.uvigo.easyfooding.core.Carrito;
import esei.uvigo.easyfooding.core.Comida;
import esei.uvigo.easyfooding.database.DatabaseAccess;

public class ModeloCarrito {

  public DatabaseAccess singletonInstance;

  public ModeloCarrito(Context c) {
    singletonInstance = DatabaseAccess.getInstance(c);
  }

  public boolean insertarLineaCarrito(String username, String codigoComida, int cantidad) {
    singletonInstance.openW();

    ContentValues contentValues = new ContentValues();
    contentValues.put("nombre_usuario", username);
    contentValues.put("codigo_comida", codigoComida);
    contentValues.put("cantidad", cantidad);

    long res = -1;
    try {
      singletonInstance.getDb().beginTransaction();
      res = singletonInstance.getDb().insertOrThrow("carrito_temp", null, contentValues);
      singletonInstance.getDb().setTransactionSuccessful();
    } catch (SQLException e) {
      Log.e("DBManager.InsertarLineaCarrito", e.getMessage());
    } finally {
      singletonInstance.getDb().endTransaction();
      singletonInstance.close();
    }

    return res != -1;
  }

  public boolean deleteUnProductoCarrito(int idProducto, int numeroActual) {
    singletonInstance.openW();

    ContentValues cv = new ContentValues();
    String toQuery = String.valueOf(idProducto);
    String actualizacion = String.valueOf(numeroActual);
    String cantidadActual = String.valueOf(numeroActual + 1);
    cv.put("cantidad", actualizacion);

    boolean res = false;
    try {
      singletonInstance.getDb().beginTransaction();
      res =
          singletonInstance
                  .getDb()
                  .update(
                      "carrito_temp",
                      cv,
                      "id_linea_carrito_temp=? and cantidad=?",
                      new String[] {toQuery, cantidadActual})
              > 0;
      singletonInstance.getDb().setTransactionSuccessful();
    } catch (SQLException e) {
      Log.e("DBManager.delete1producto", e.getMessage());
    }finally{
      singletonInstance.getDb().endTransaction();
      singletonInstance.close();
    }

    return res;
  }

  public boolean addUnProductoCarrito(int idProducto, int numeroActual) {
    singletonInstance.openW();

    ContentValues cv = new ContentValues();
    String toQuery = String.valueOf(idProducto);
    String actualizacion = String.valueOf(numeroActual);
    String cantidadActual = String.valueOf(numeroActual - 1);
    cv.put("cantidad", actualizacion);
    boolean toret=false;
    try{
      singletonInstance.getDb().beginTransaction();
      toret =
              singletonInstance
                      .getDb()
                      .update(
                              "carrito_temp",
                              cv,
                              "id_linea_carrito_temp=? and cantidad=?",
                              new String[] {toQuery, cantidadActual})
                      > 0;
      singletonInstance.getDb().setTransactionSuccessful();

    }catch (SQLException e){
      Log.e("DBManager.add1producto", e.getMessage());
    }finally{
      singletonInstance.getDb().endTransaction();
      singletonInstance.close();
    }
    return toret;
  }

  // Cogemos los objetos del carrito
  public ArrayList<Carrito> getObjetosEnCarro(String usuario) {
    singletonInstance.openR();

    Cursor c =
        singletonInstance
            .getDb()
            .rawQuery(
                "SELECT * FROM carrito_temp WHERE nombre_usuario = ?", new String[] {usuario});
    ArrayList<Carrito> toret = new ArrayList<Carrito>();
    while (c.moveToNext()) {
      int codigo_comida = c.getInt(1);
      int cantidad = c.getInt(2);
      String usr = c.getString(0);
      toret.add(new Carrito(codigo_comida, cantidad, usr));
    }

    singletonInstance.close();

    c.close();

    return toret;
  }

  // con el id buscamos las caracteristicas del producto
  public Comida getDatosComida(int idComida, int cantidad) {
    singletonInstance.openR();

    String toQuery = String.valueOf(idComida);
    Cursor c =
        singletonInstance
            .getDb()
            .rawQuery("SELECT * FROM comidas WHERE codigo_comida = ?", new String[] {toQuery});
    Comida toret = new Comida();
    while (c.moveToNext()) {
      int codigo = Integer.parseInt(c.getString(0));
      String nombre = c.getString(1);
      Double precio = Double.parseDouble(c.getString(3));
      toret.setCantidad(cantidad);
      toret.setPrecio(precio);
      toret.setNombre(nombre);
      toret.setCodigo(codigo);
      toret.setImagen(R.mipmap.logo);
    }

    singletonInstance.close();

    c.close();

    return toret;
  }

  public int getIdLineaConCantidad(int codigoComida, String usuario, int cantidad) {
    singletonInstance.openR();

    String toQuery = String.valueOf(codigoComida);
    String cantidadStr = String.valueOf(cantidad);
    Cursor c =
        singletonInstance
            .getDb()
            .rawQuery(
                "SELECT * FROM carrito_temp WHERE codigo_comida = ? AND nombre_usuario = ? AND cantidad = ?",
                new String[] {toQuery, usuario, cantidadStr});
    int toret = 0;
    while (c.moveToNext()) {
      int codigoLinea = c.getInt(3);
      toret = codigoLinea;
    }

    singletonInstance.close();

    c.close();

    return toret;
  }

  // Obtener los ids de las comidas dado un nombre
  public ArrayList<String> getDatosComidaPorId(String codigo_comida) {
    singletonInstance.openR();

    ArrayList<String> toret = new ArrayList<>();
    @SuppressLint("Recycle")
    Cursor cursor =
        singletonInstance
            .getDb()
            .rawQuery(
                "select nombre, descripcion, precio, valoracion, calorias, tiempo_preparacion\n"
                    + "from comidas \n"
                    + "where codigo_comida = ?",
                new String[] {codigo_comida});

    while (cursor.moveToNext()) {
      for (int i = 0; i < cursor.getColumnCount(); i++) {
        String elemento = cursor.getString(i);
        toret.add(elemento);
      }
    }

    singletonInstance.close();

    cursor.close();

    return toret;
  }

  public int getIdComida(String nombre) {
    singletonInstance.openR();

    Cursor cursor =
        singletonInstance
            .getDb()
            .rawQuery("select codigo_comida from comidas where nombre = ?", new String[] {nombre});
    int toret = 0;
    if (cursor.moveToNext()) {
      toret = cursor.getInt(0);
    }

    singletonInstance.close();

    cursor.close();

    return toret;
  }

  public boolean eliminarProductorCompradosConCodigo(int codigoLinea, String usuario) {
    singletonInstance.openW();
    String toQuery = String.valueOf(codigoLinea);
    boolean toret = false;
    try{
      singletonInstance.getDb().beginTransaction();
      toret =
              singletonInstance
              .getDb()
              .delete(
                      "carrito_temp",
                      "id_linea_carrito_temp=? and nombre_usuario=?",
                      new String[] {toQuery, usuario})
              > 0;
      singletonInstance.getDb().setTransactionSuccessful();
    }catch (SQLException e){
      Log.e("DBManager.add1producto", e.getMessage());
    }finally{
      singletonInstance.getDb().endTransaction();
      singletonInstance.close();
    }
    return toret;
  }



  public int getIdLineaConCodigoComida(int codigoComida, String usuario) {
    singletonInstance.openR();

    String toQuery = String.valueOf(codigoComida);
    Cursor c =
        singletonInstance
            .getDb()
            .rawQuery(
                "SELECT * FROM carrito_temp WHERE codigo_comida = ? AND nombre_usuario = ?",
                new String[] {toQuery, usuario});
    int toret = 0;
    while (c.moveToNext()) {
      int codigoLinea = c.getInt(3);
      toret = codigoLinea;
    }

    singletonInstance.close();

    c.close();

    return toret;
  }
}
