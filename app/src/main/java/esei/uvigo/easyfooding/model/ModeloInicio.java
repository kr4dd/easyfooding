package esei.uvigo.easyfooding.model;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;

import java.util.ArrayList;

import esei.uvigo.easyfooding.database.DatabaseAccess;

public class ModeloInicio {
    public DatabaseAccess singletonInstance;

    public ModeloInicio(Context c) {
        singletonInstance = DatabaseAccess.getInstance(c);
    }

    // Obtener el nombre de 5 comidas aleatorias
    public ArrayList<String> getNombreComidasRandom() {
        singletonInstance.openR();

        ArrayList<String> toret = new ArrayList<>();

        String[] selectionArgs = new String[] {"%bebida%"};

        Cursor cursor = null;

        try{
            cursor =
                    singletonInstance
                            .getDb()
                            .rawQuery(
                                    "select nombre\n" + "from comidas \n" + "where categoria not like(?)",
                                    selectionArgs);
            while (cursor.moveToNext()) {
                String elemento = cursor.getString(0);
                toret.add(elemento);
            }

        }catch(SQLException e){
            Log.e( "DBManager.getNombreComidasRandom", e.getMessage() );
        }finally{
            if ( cursor != null ) {
                cursor.close();
            }
            singletonInstance.close();
        };

        return toret;

    }

    // Obtener los ids de las comidas dado un nombre de comida
    public ArrayList<Integer> getCodigoComidaPorNombre(String nombreComida) {
        singletonInstance.openR();

        ArrayList<Integer> toret = new ArrayList<>();

        // Añadir lso porcentajes antes de ejecutar la sentencia SQL con Like
        String[] selectionArgs = new String[] {"%" + nombreComida + "%"};

        Cursor cursor = null;

        try{
            cursor =
                    singletonInstance
                            .getDb()
                            .rawQuery("select codigo_comida from comidas where nombre like(?)", selectionArgs);
            while (cursor.moveToNext()) {
                int elemento = Integer.parseInt(cursor.getString(0));
                toret.add(elemento);
            }

        }catch(SQLException e){
            Log.e( "DBManager.getCodigoComidaPorNombre", e.getMessage() );
        }finally{
            if ( cursor != null ) {
                cursor.close();
            }
            singletonInstance.close();
        };

        return toret;
    }

    // Obtener el nombre de todas las categorias
    public ArrayList<String> getNombresCategorias() {
        singletonInstance.openR();

        ArrayList<String> toret = new ArrayList<>();

        Cursor cursor = null;

        try{
            cursor =
                    singletonInstance
                            .getDb()
                            .rawQuery(
                                    "SELECT DISTINCT(nombre_categoria) FROM categorias ORDER BY codigo_categoria",
                                    null);
            while (cursor.moveToNext()) {
                String elemento = cursor.getString(0);
                toret.add(elemento);
            }

        }catch(SQLException e){
            Log.e( "DBManager.getNombresCategorias", e.getMessage() );
        }finally{
            if ( cursor != null ) {
                cursor.close();
            }
            singletonInstance.close();
        };

        return toret;
    }

    // Obtener el numero de categorias actuales
    public int getNumCategorias() {

        singletonInstance.openR();

        int toret = 0;
        Cursor cursor = null;
        StringBuilder buffer = new StringBuilder();

        try{
            cursor = singletonInstance.getDb().rawQuery("SELECT count(*) FROM categorias", null);
            while (cursor.moveToNext()) {
                String numCategorias = cursor.getString(0);
                buffer.append("").append(numCategorias);
            }
            toret = Integer.parseInt(buffer.toString());

        }catch(SQLException e){
            Log.e( "DBManager.getNumCategorias", e.getMessage() );
        }finally{
            if ( cursor != null ) {
                cursor.close();
            }
            singletonInstance.close();
        };

        return toret;
    }

    // Obtener el nombre de la comida que tiene un codigo_comida dado
    public String getNombreComidaPorId(String codigoComida) {
        singletonInstance.openR();

        String toret = "";
        Cursor cursor = null;

        try{
            cursor =
                    singletonInstance
                            .getDb()
                            .rawQuery(
                                    "select nombre from comidas where codigo_comida = ?", new String[] {codigoComida});
            if (cursor.moveToNext()) {
                toret = cursor.getString(0);
            }

        }catch(SQLException e){
            Log.e( "DBManager.getNombreComidaPorId", e.getMessage() );
        }finally{
            if ( cursor != null ) {
                cursor.close();
            }
            singletonInstance.close();
        };

        return toret;
    }

    // Obtener el codigo de las comidas que pertenecen a una categoria
    public ArrayList<Integer> getCodigoComidaPorCategoria(String nombreCategoria) {
        singletonInstance.openR();

        ArrayList<Integer> toret = new ArrayList<>();

        Cursor cursor = null;

        try{
            cursor =
                    singletonInstance
                            .getDb()
                            .rawQuery(
                                    "select com.codigo_comida from comidas com, categorias cat where com.categoria = cat.nombre_categoria and cat.nombre_categoria = ?",
                                    new String[] {nombreCategoria});
            while (cursor.moveToNext()) {
                int elemento = Integer.parseInt(cursor.getString(0));
                toret.add(elemento);
            }

        }catch(SQLException e){
            Log.e( "DBManager.getCodigoComidaPorCategoria", e.getMessage() );
        }finally{
            if ( cursor != null ) {
                cursor.close();
            }
            singletonInstance.close();
        };

        return toret;
    }
}
