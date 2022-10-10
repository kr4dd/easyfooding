package esei.uvigo.easyfooding.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import esei.uvigo.easyfooding.objetosCarrito.LineaPedidos;
import esei.uvigo.easyfooding.objetosCarrito.ListaPedidos;

public class DatabaseAccess {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase db;
    private static DatabaseAccess instance;

    // private constructor
    private DatabaseAccess(Context context) {
        this.openHelper = new DatabaseOpenHelper(context);
    }

    // to return the single instance of database
    public static DatabaseAccess getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseAccess(context);
        }
        return instance;
    }

    // open the database connection
    public void open() {
        this.db = openHelper.getWritableDatabase();
    }

    // close the database connection
    public void close() {
        if (db != null) {
            this.db.close();
        }
    }

    // metodo para hacer una consulta del nombre de una categoria teniendo su
    // codigo_categoria
    public String getCategoriaPorId(String id) {

        // Metemos el resultado de la consulta en el cursor
        Cursor cursor = db.rawQuery("SELECT nombre_categoria FROM categorias WHERE codigo_categoria = "
                + "'?'", new String[] { id });

        StringBuffer buffer = new StringBuffer();
        while (cursor.moveToNext()) {
            String categoria = cursor.getString(0);
            buffer.append("" + categoria);
        }
        return buffer.toString();
    }

    // Obtener el numero de categorias actuales
    public int getNumCategorias() {

        int toret = 0;
        Cursor cursor = db.rawQuery("SELECT count(*) FROM categorias", null);
        StringBuffer buffer = new StringBuffer();
        while (cursor.moveToNext()) {
            String numCategorias = cursor.getString(0);
            buffer.append("" + numCategorias);
        }
        toret = Integer.parseInt(buffer.toString());
        return toret;
    }

    // Obtener el nombre de todas las categorias
    public ArrayList<String> getNombresCategorias() {
        ArrayList<String> toret = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT DISTINCT(nombre_categoria) FROM categorias ORDER BY codigo_categoria",
                null);
        while (cursor.moveToNext()) {
            String elemento = cursor.getString(0);
            toret.add(elemento);
        }
        return toret;
    }

    // Obtener el codigo de las comidas que pertenecen a una categoria
    public ArrayList<Integer> getCodigoComidaPorCategoria(String nombreCategoria) {
        ArrayList<Integer> toret = new ArrayList<>();
        Cursor cursor = db.rawQuery(
                "select com.codigo_comida from comidas com, categorias cat where com.categoria = cat.nombre_categoria and cat.nombre_categoria = ?",
                new String[] { nombreCategoria });
        while (cursor.moveToNext()) {
            int elemento = Integer.parseInt(cursor.getString(0));
            toret.add(elemento);
        }
        return toret;
    }

    // Obtener el nombre de la comida que tiene un codigo_comida dado
    public String getNombreComidaPorId(String codigoComida) {
        String toret = "";
        Cursor cursor = db.rawQuery("select nombre from comidas where codigo_comida = ?",
                new String[] { codigoComida });
        if (cursor.moveToNext()) {
            toret = cursor.getString(0);
        }
        return toret;
    }

    // Obtener los ids de las comidas dado un nombre
    public ArrayList<String> getDatosComidaPorId(String codigo_comida){
        ArrayList<String> toret = new ArrayList<>();
        Cursor cursor = db.rawQuery("select nombre, descripcion, precio, valoracion, calorias, tiempo_preparacion\n" +
                                        "from comidas \n" +
                                        "where codigo_comida = ?", new String[]{codigo_comida});

        while(cursor.moveToNext()){
            for (int i = 0; i < cursor.getColumnCount(); i++) {
                String elemento = cursor.getString(i);
                toret.add(elemento);
            }
        }
        return toret;
    }

    // Obtener los ids de las comidas dado un nombre de comida
    public ArrayList<Integer> getCodigoComidaPorNombre(String nombreComida) {
        ArrayList<Integer> toret = new ArrayList<>();

        // Añadir lso porcentajes antes de ejecutar la sentencia SQL con Like
        String[] selectionArgs = new String[] { "%" + nombreComida + "%" };

        Cursor cursor = db.rawQuery("select codigo_comida from comidas where nombre like(?)", selectionArgs);
        while (cursor.moveToNext()) {
            int elemento = Integer.parseInt(cursor.getString(0));
            toret.add(elemento);
        }
        return toret;
    }

    // Obtener el nombre de 5 comidas aleatorias
    public ArrayList<String> getNombreComidasRandom() {
        ArrayList<String> toret = new ArrayList<>();

        String[] selectionArgs = new String[] { "%bebida%" };

        Cursor cursor = db.rawQuery("select nombre\n" +
                "from comidas \n" +
                "where categoria not like(?)", selectionArgs);
        while (cursor.moveToNext()) {
            String elemento = cursor.getString(0);
            toret.add(elemento);
            System.out.println(elemento);
        }
        return toret;
    }

    /*
     * //Obtener el nombre de 5 comidas aleatorias
     * public Map<String,String> getNombreComidasRandom(){
     * Map<String,String> toret = new HashMap<String, String>();
     * 
     * String [] selectionArgs = new String[] { "%bebida%" };
     * 
     * Cursor cursor = db.rawQuery("select codigo_comida, nombre\n" +
     * "from comidas \n" +
     * "where categoria not like(?)", selectionArgs);
     * while(cursor.moveToNext()){
     * String clave = cursor.getString(0);
     * String valor = cursor.getString(1);
     * toret.put(clave, valor);
     * System.out.println(toret.get(clave));
     * }
     * return toret;
     * }
     */






























    //querys de Carrito y de historial pedidos
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

    public  ArrayList<LineaPedidos> getLineaPedidos(int numPedido){
        String toQuery = String.valueOf(numPedido);
        Cursor c = db.rawQuery("SELECT * FROM linea_pedidos WHERE num_pedido = ?", new String[]{toQuery});
        ArrayList<LineaPedidos> toret = new ArrayList<>();
        while(c.moveToNext()){
            int num_linea = Integer.parseInt(c.getString(0));
            int num_pedidio = Integer.parseInt(c.getString(1));
            int codigo_comida = Integer.parseInt(c.getString(2));
            int cantidad = Integer.parseInt(c.getString(3));
            double precio_linea = Double.parseDouble(c.getString(4));

            LineaPedidos l = new LineaPedidos(num_linea,num_pedidio,codigo_comida,cantidad,precio_linea);
            toret.add(l);
        }
        return toret;
    }
    public void setPedido (String nombreUsr, String fecha,String dir, String localidad, int cp, double importe, String obs){
        ContentValues contentValues = new ContentValues();

        contentValues.put("nombre_usuario",nombreUsr);
        contentValues.put("fecha_pedido",fecha);
        contentValues.put("direccion_envio",dir);
        contentValues.put("localidad_envio",localidad);
        contentValues.put("codigo_postal_envio",cp);
        contentValues.put("importe_total",importe);
        contentValues.put("observaciones",obs);

        db.insert("pedido", null, contentValues);
    }
    public int getIdComida(String nombre){
        Cursor cursor = db.rawQuery("select codigo_comida from comidas where nombre = ?", new String[]{nombre});
        int toret = 0;
        if (cursor.moveToNext()) {
            toret = cursor.getInt(0);
        }
        return toret;
    }
}
