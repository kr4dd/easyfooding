package esei.uvigo.easyfooding.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import esei.uvigo.easyfooding.objetosCarrito.ListaPedidos;

public class DatabaseAccess {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase db;
    private static DatabaseAccess instance;

    //private constructor
    private DatabaseAccess(Context context) {
        this.openHelper = new DatabaseOpenHelper(context);
    }

    //to return the single instance of database
    public static DatabaseAccess getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseAccess(context);
        }
        return instance;
    }

    //open the database connection
    public void open() {
        this.db = openHelper.getWritableDatabase();
    }

    //close the database connection
    public void close() {
        if (db != null) {
            this.db.close();
        }
    }

    //metodo para hacer una consulta del nombre de una categoria teniendo su codigo_categoria
    public String getCategoriaPorId(String id){

        //Metemos el resultado de la consulta en el cursor
        Cursor cursor = db.rawQuery("SELECT nombre_categoria FROM categorias WHERE codigo_categoria = "
                + "'?'", new String[]{id});

        StringBuffer buffer = new StringBuffer();
        while(cursor.moveToNext()){
            String categoria = cursor.getString(0);
            buffer.append(""+categoria);
        }
        return buffer.toString();
    }

    //Obtener el numero de categorias actuales
    public int getNumCategorias(){

        int toret = 0;
        Cursor cursor = db.rawQuery("SELECT count(*) FROM categorias", null);
        StringBuffer buffer = new StringBuffer();
        while(cursor.moveToNext()){
            String numCategorias = cursor.getString(0);
            buffer.append(""+numCategorias);
        }
        toret = Integer.parseInt(buffer.toString());
        return toret;
    }

    //Obtener el nombre de todas las categorias
    public ArrayList<String> getNombresCategorias(){
        ArrayList<String> toret = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT DISTINCT(nombre_categoria) FROM categorias ORDER BY codigo_categoria", null);
        while(cursor.moveToNext()){
            String elemento = cursor.getString(0);
            toret.add(elemento);
        }
        return toret;
    }

    //Obtener el codigo de las comidas que pertenecen a una categoria
    public ArrayList<Integer> getCodigoComidaPorCategoria(String nombreCategoria){
        ArrayList<Integer> toret =  new ArrayList<>();
        Cursor cursor = db.rawQuery("select com.codigo_comida from comidas com, categorias cat where com.categoria = cat.nombre_categoria and cat.nombre_categoria = ?", new String[]{nombreCategoria});
        while(cursor.moveToNext()){
            int elemento = Integer.parseInt(cursor.getString(0));
            toret.add(elemento);
        }
        return toret;
    }

    //Obtener el nombre de la comida que tiene un codigo_comida dado
    public String getNombreComidaPorId(String codigoComida){
        String toret = "";
        Cursor cursor = db.rawQuery("select nombre from comidas where codigo_comida = ?", new String[]{codigoComida});
        if(cursor.moveToNext()){
            toret = cursor.getString(0);
        }
        return toret;
    }


    public  ArrayList<ListaPedidos> historial(String usuario) {
        Cursor c = db.rawQuery("SELECT * FROM pedidos WHERE nombre_usuario = ?", new String[]{usuario});
        StringBuffer buffer = new StringBuffer();
        ArrayList<ListaPedidos> courseModalArrayList = new ArrayList<>();

        while(c.moveToNext()){
            int num_ped = Integer.parseInt(c.getString(0));
            String user = c.getString(1);
            String fecha =  c.getString(2);
            String direccion = c.getString(3);
            String loc = c.getString(4);
            float codigo = Integer.parseInt(c.getString(5));
            double tot = Double.parseDouble(c.getString(6));
            String obs = c.getString(7);

            ListaPedidos l = new ListaPedidos(num_ped,user,fecha,direccion,loc,codigo,tot,obs);
            courseModalArrayList.add(l);
        }

        return courseModalArrayList;
    }


}
