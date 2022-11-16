package esei.uvigo.easyfooding.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;

import esei.uvigo.easyfooding.database.DatabaseAccess;

public class ModeloPago {

    public DatabaseAccess singletonInstance;

    public ModeloPago(Context c) {
        singletonInstance = DatabaseAccess.getInstance(c);
    }

    public boolean insertarPedido(
            int id,
            String nombreUsr,
            String fecha,
            String dir,
            String localidad,
            int cp,
            double importe,
            String obs) {
        singletonInstance.openW();
        long res = -1;

        ContentValues contentValues = new ContentValues();
        contentValues.put("num_pedido", id);
        contentValues.put("nombre_usuario", nombreUsr);
        contentValues.put("fecha_pedido", fecha);
        contentValues.put("direccion_envio", dir);
        contentValues.put("localidad_envio", localidad);
        contentValues.put("codigo_postal_envio", cp);
        contentValues.put("importe_total", importe);
        contentValues.put("observaciones", obs);
        try {
            singletonInstance.getDb().beginTransaction();
            res = singletonInstance.getDb().insertOrThrow("pedidos", null, contentValues);
            singletonInstance.getDb().setTransactionSuccessful();

        } catch (SQLException e) {
            Log.e("DBManager.insertarPedido", e.getMessage());
        } finally {
            singletonInstance.getDb().endTransaction();
            singletonInstance.close();
        }

        return res != 1;
    }

    public int getMaxIdPedido() {
        singletonInstance.openR();

        Cursor cursor = null;
        String toret = "";

        try{
            cursor = singletonInstance
                            .getDb()
                            .rawQuery("SELECT MAX(num_pedido)  " + "AS max_id FROM pedidos", null);
            if (cursor.moveToNext()) {
                toret = cursor.getString(0);
            }

        }catch(SQLException e){
            Log.e( "DBManager.getMaxIdPedido", e.getMessage() );
        }finally{
            if ( cursor != null ) {
                cursor.close();
            }
            singletonInstance.close();
        };

        return Integer.parseInt(toret);
    }

    public int getMaxIdLineaPedido() {
        singletonInstance.openR();

        Cursor cursor = null;
        String toret = "";

        try{
            cursor = singletonInstance
                            .getDb()
                            .rawQuery("SELECT MAX(num_linea)  AS max_id FROM linea_pedidos", null);
            if (cursor.moveToNext()) {
                toret = cursor.getString(0);
            }

        }catch(SQLException e){
            Log.e( "DBManager.getMaxIdPedido", e.getMessage() );
        }finally{
            if ( cursor != null ) {
                cursor.close();
            }
            singletonInstance.close();
        };

        cursor.close();

        return Integer.parseInt(toret);
    }

    public boolean insertarLineaPedido(
            int idLinea, int numPedido, int codigoComida, int cantidad, double precio) {
        singletonInstance.openW();
        ContentValues contentValues = new ContentValues();
        contentValues.put("num_linea", idLinea);
        contentValues.put("num_pedido", numPedido);
        contentValues.put("codigo_comida", codigoComida);
        contentValues.put("cantidad", cantidad);
        contentValues.put("precio_linea", precio);
        long res = -1;
        try {
            singletonInstance.getDb().beginTransaction();
            res = singletonInstance.getDb().insertOrThrow("linea_pedidos", null, contentValues);
            singletonInstance.getDb().setTransactionSuccessful();

        } catch (SQLException e) {
            Log.e("DBManager.InsertarLineaCarrito", e.getMessage());
        } finally {
            singletonInstance.getDb().endTransaction();
            singletonInstance.close();
        }
        return res != 1;
    }

    // eliminamos la comida del carrito cuando ya la hemos comprado
    public boolean eliminarProductorComprados(String usuario) {
        singletonInstance.openW();
        boolean toret = false;
        try {
            singletonInstance.getDb().beginTransaction();
            toret =
                    singletonInstance.getDb().delete("carrito_temp", "nombre_usuario=?", new String[]{usuario})
                            > 0;
            singletonInstance.getDb().setTransactionSuccessful();

        } catch (SQLException e) {
            Log.e("DBManager.InsertarLineaCarrito", e.getMessage());
        } finally {
            singletonInstance.getDb().endTransaction();
            singletonInstance.close();
        }
        return toret;
    }
}
