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
                                    new String[]{toQuery, cantidadActual})
                            > 0;
            singletonInstance.getDb().setTransactionSuccessful();
        } catch (SQLException e) {
            Log.e("DBManager.deleteUnProductoCarrito", e.getMessage());
        } finally {
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
        boolean toret = false;
        try {
            singletonInstance.getDb().beginTransaction();
            toret =
                    singletonInstance
                            .getDb()
                            .update(
                                    "carrito_temp",
                                    cv,
                                    "id_linea_carrito_temp=? and cantidad=?",
                                    new String[]{toQuery, cantidadActual})
                            > 0;
            singletonInstance.getDb().setTransactionSuccessful();

        } catch (SQLException e) {
            Log.e("DBManager.addUnProductoCarrito", e.getMessage());
        } finally {
            singletonInstance.getDb().endTransaction();
            singletonInstance.close();
        }
        return toret;
    }

    // Cogemos los objetos del carrito
    public ArrayList<Carrito> getObjetosEnCarro(String usuario) {
        singletonInstance.openR();

        Cursor cursor = null;
        ArrayList<Carrito> toret = new ArrayList<Carrito>();

        try{
            cursor = singletonInstance
                            .getDb()
                            .rawQuery(
                                    "SELECT * FROM carrito_temp WHERE nombre_usuario = ?", new String[]{usuario});
            while (cursor.moveToNext()) {
                int codigo_comida = cursor.getInt(1);
                int cantidad = cursor.getInt(2);
                String usr = cursor.getString(0);
                toret.add(new Carrito(codigo_comida, cantidad, usr));
            }

        }catch(SQLException e){
            Log.e( "DBManager.getObjetosEnCarro", e.getMessage() );
        }finally{
            if ( cursor != null ) {
                cursor.close();
            }
            singletonInstance.close();
        };

        return toret;
    }

    // con el id buscamos las caracteristicas del producto
    public Comida getDatosComida(int idComida, int cantidad) {
        singletonInstance.openR();

        String toQuery = String.valueOf(idComida);
        Comida toret = new Comida();
        Cursor c = null;

        try{
            c = singletonInstance
                            .getDb()
                            .rawQuery("SELECT * FROM comidas WHERE codigo_comida = ?", new String[]{toQuery});
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

        }catch(SQLException e){
            Log.e( "DBManager.getDatosComida", e.getMessage() );
        }finally{
            if ( c != null ) {
                c.close();
            }
            singletonInstance.close();
        };

        return toret;
    }

    public int getIdLineaConCantidad(int codigoComida, String usuario, int cantidad) {
        singletonInstance.openR();

        String toQuery = String.valueOf(codigoComida);
        String cantidadStr = String.valueOf(cantidad);

        int toret = 0;
        Cursor c = null;

        try{
            c = singletonInstance
                            .getDb()
                            .rawQuery(
                                    "SELECT * FROM carrito_temp WHERE codigo_comida = ? AND nombre_usuario = ? AND cantidad = ?",
                                    new String[]{toQuery, usuario, cantidadStr});
            while (c.moveToNext()) {
                int codigoLinea = c.getInt(3);
                toret = codigoLinea;
            }

        }catch(SQLException e){
            Log.e( "DBManager.getIdLineaConCantidad", e.getMessage() );
        }finally{
            if ( c != null ) {
                c.close();
            }
            singletonInstance.close();
        };

        return toret;
    }

    // Obtener los ids de las comidas dado un nombre
    public ArrayList<String> getDatosComidaPorId(String codigo_comida) {
        singletonInstance.openR();
        ArrayList<String> toret = new ArrayList<>();
        Cursor cursor = null;

        try{
            cursor = singletonInstance
                            .getDb()
                            .rawQuery(
                                    "select nombre, descripcion, precio, valoracion, calorias, tiempo_preparacion\n"
                                            + "from comidas \n"
                                            + "where codigo_comida = ?",
                                    new String[]{codigo_comida});

            while (cursor.moveToNext()) {
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    String elemento = cursor.getString(i);
                    toret.add(elemento);
                }
            }

        }catch(SQLException e){
            Log.e( "DBManager.getDatosComidaPorId", e.getMessage() );
        }finally{
            if ( cursor != null ) {
                cursor.close();
            }
            singletonInstance.close();
        };


        return toret;
    }

    public int getIdComida(String nombre) {
        singletonInstance.openR();
        int toret = 0;
        Cursor cursor = null;

        try{
            cursor = singletonInstance
                            .getDb()
                            .rawQuery("select codigo_comida from comidas where nombre = ?", new String[]{nombre});
            if (cursor.moveToNext()) {
                toret = cursor.getInt(0);
            }

        }catch(SQLException e){
            Log.e( "DBManager.getIdComida", e.getMessage() );
        }finally{
            if ( cursor != null ) {
                cursor.close();
            }
            singletonInstance.close();
        };

        return toret;
    }

    public boolean eliminarProductorCompradosConCodigo(int codigoLinea, String usuario) {
        singletonInstance.openW();
        String toQuery = String.valueOf(codigoLinea);
        boolean toret = false;
        try {
            singletonInstance.getDb().beginTransaction();
            toret =
                    singletonInstance
                            .getDb()
                            .delete(
                                    "carrito_temp",
                                    "id_linea_carrito_temp=? and nombre_usuario=?",
                                    new String[]{toQuery, usuario})
                            > 0;
            singletonInstance.getDb().setTransactionSuccessful();
        } catch (SQLException e) {
            Log.e("DBManager.eliminarProductorCompradosConCodigo", e.getMessage());
        } finally {
            singletonInstance.getDb().endTransaction();
            singletonInstance.close();
        }
        return toret;
    }


    public int getIdLineaConCodigoComida(int codigoComida, String usuario) {
        singletonInstance.openR();
        String toQuery = String.valueOf(codigoComida);
        int toret = 0;
        Cursor cursor = null;
        try{
            cursor = singletonInstance
                            .getDb()
                            .rawQuery(
                                    "SELECT * FROM carrito_temp WHERE codigo_comida = ? AND nombre_usuario = ?",
                                    new String[]{toQuery, usuario});
            while (cursor.moveToNext()) {
                int codigoLinea = cursor.getInt(3);
                toret = codigoLinea;
            }

        }catch(SQLException e){
            Log.e( "DBManager.getIdLineaConCodigoComida", e.getMessage() );
        }finally{
            if ( cursor != null ) {
                cursor.close();
            }
            singletonInstance.close();
        };

        return toret;
    }
}
