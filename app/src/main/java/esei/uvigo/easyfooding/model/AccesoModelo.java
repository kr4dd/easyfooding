package esei.uvigo.easyfooding.model;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

import esei.uvigo.easyfooding.OperationsUser;
import esei.uvigo.easyfooding.R;
import esei.uvigo.easyfooding.UsuarioRegistro;
import esei.uvigo.easyfooding.core.Carrito;
import esei.uvigo.easyfooding.core.Comida;
import esei.uvigo.easyfooding.core.LineaPedidos;
import esei.uvigo.easyfooding.core.ListaPedidos;
import esei.uvigo.easyfooding.database.DatabaseAccess;

public class AccesoModelo {
    public DatabaseAccess singletonInstance;

    public AccesoModelo(Context c) {
        singletonInstance = DatabaseAccess.getInstance(c);
    }

    // Obtener el numero de categorias actuales
    public int getNumCategorias() {

        singletonInstance.open();

        int toret = 0;
        Cursor cursor = singletonInstance.getDb().rawQuery("SELECT count(*) FROM categorias", null);
        StringBuilder buffer = new StringBuilder();
        while (cursor.moveToNext()) {
            String numCategorias = cursor.getString(0);
            buffer.append("").append(numCategorias);
        }
        toret = Integer.parseInt(buffer.toString());

        singletonInstance.close();

        return toret;
    }

    // Obtener el nombre de todas las categorias
    public ArrayList<String> getNombresCategorias() {
        singletonInstance.open();

        ArrayList<String> toret = new ArrayList<>();
        @SuppressLint("Recycle")
        Cursor cursor = singletonInstance.getDb().rawQuery("SELECT DISTINCT(nombre_categoria) FROM categorias ORDER BY codigo_categoria",
                null);
        while (cursor.moveToNext()) {
            String elemento = cursor.getString(0);
            toret.add(elemento);
        }

        singletonInstance.close();

        return toret;
    }

    // Obtener el codigo de las comidas que pertenecen a una categoria
    public ArrayList<Integer> getCodigoComidaPorCategoria(String nombreCategoria) {
        singletonInstance.open();

        ArrayList<Integer> toret = new ArrayList<>();
        @SuppressLint("Recycle")
        Cursor cursor = singletonInstance.getDb().rawQuery(
                "select com.codigo_comida from comidas com, categorias cat where com.categoria = cat.nombre_categoria and cat.nombre_categoria = ?",
                new String[] { nombreCategoria });
        while (cursor.moveToNext()) {
            int elemento = Integer.parseInt(cursor.getString(0));
            toret.add(elemento);
        }

        singletonInstance.close();

        return toret;
    }

    // Obtener el nombre de la comida que tiene un codigo_comida dado
    public String getNombreComidaPorId(String codigoComida) {
        singletonInstance.open();

        String toret = "";
        Cursor cursor = singletonInstance.getDb().rawQuery("select nombre from comidas where codigo_comida = ?",
                new String[] { codigoComida });
        if (cursor.moveToNext()) {
            toret = cursor.getString(0);
        }

        singletonInstance.close();

        return toret;
    }

    // Obtener los ids de las comidas dado un nombre
    public ArrayList<String> getDatosComidaPorId(String codigo_comida) {
        singletonInstance.open();

        ArrayList<String> toret = new ArrayList<>();
        @SuppressLint("Recycle")
        Cursor cursor = singletonInstance.getDb().rawQuery("select nombre, descripcion, precio, valoracion, calorias, tiempo_preparacion\n" +
                "from comidas \n" +
                "where codigo_comida = ?", new String[] { codigo_comida });

        while (cursor.moveToNext()) {
            for (int i = 0; i < cursor.getColumnCount(); i++) {
                String elemento = cursor.getString(i);
                toret.add(elemento);
            }
        }

        singletonInstance.close();

        return toret;
    }

    // Obtener los ids de las comidas dado un nombre de comida
    public ArrayList<Integer> getCodigoComidaPorNombre(String nombreComida) {
        singletonInstance.open();

        ArrayList<Integer> toret = new ArrayList<>();

        // Añadir lso porcentajes antes de ejecutar la sentencia SQL con Like
        String[] selectionArgs = new String[] { "%" + nombreComida + "%" };

        @SuppressLint("Recycle")
        Cursor cursor = singletonInstance.getDb().rawQuery("select codigo_comida from comidas where nombre like(?)", selectionArgs);
        while (cursor.moveToNext()) {
            int elemento = Integer.parseInt(cursor.getString(0));
            toret.add(elemento);
        }

        singletonInstance.close();

        return toret;
    }

    // Obtener el nombre de 5 comidas aleatorias
    public ArrayList<String> getNombreComidasRandom() {
        singletonInstance.open();

        ArrayList<String> toret = new ArrayList<>();

        String[] selectionArgs = new String[] { "%bebida%" };

        @SuppressLint("Recycle")
        Cursor cursor = singletonInstance.getDb().rawQuery("select nombre\n" +
                "from comidas \n" +
                "where categoria not like(?)", selectionArgs);
        while (cursor.moveToNext()) {
            String elemento = cursor.getString(0);
            toret.add(elemento);
        }

        singletonInstance.close();

        return toret;
    }

    // querys de Carrito y de historial pedidos
    public ArrayList<ListaPedidos> historial(String usuario) {
        singletonInstance.open();

        @SuppressLint("Recycle")
        Cursor c = singletonInstance.getDb().rawQuery("SELECT * FROM pedidos WHERE nombre_usuario = ?", new String[] { usuario });
        ArrayList<ListaPedidos> courseModalArrayList = new ArrayList<>();

        while (c.moveToNext()) {
            int num_ped = Integer.parseInt(c.getString(0));
            String user = c.getString(1);
            String fecha = c.getString(2);
            String direccion = c.getString(3);
            String loc = c.getString(4);
            int codigo = Integer.parseInt(c.getString(5));
            double tot = Double.parseDouble(c.getString(6));
            String obs = c.getString(7);

            ListaPedidos l = new ListaPedidos(num_ped, user, fecha, direccion, loc, codigo, tot, obs);
            courseModalArrayList.add(l);
        }

        singletonInstance.close();

        return courseModalArrayList;
    }

    public ArrayList<LineaPedidos> getLineaPedidos(int numPedido) {
        singletonInstance.open();

        String toQuery = String.valueOf(numPedido);
        Cursor c = singletonInstance.getDb().rawQuery("SELECT * FROM linea_pedidos WHERE num_pedido = ?", new String[] { toQuery });
        ArrayList<LineaPedidos> toret = new ArrayList<>();
        while (c.moveToNext()) {
            int num_linea = Integer.parseInt(c.getString(0));
            int num_pedidio = Integer.parseInt(c.getString(1));
            int codigo_comida = Integer.parseInt(c.getString(2));
            int cantidad = Integer.parseInt(c.getString(3));
            double precio_linea = Double.parseDouble(c.getString(4));

            LineaPedidos l = new LineaPedidos(num_linea, num_pedidio, codigo_comida, cantidad,
                    precio_linea);
            toret.add(l);
        }

        singletonInstance.close();

        return toret;
    }

    public boolean insertarPedido(int id,String nombreUsr, String fecha,
                                  String dir, String localidad, int cp, double importe,
                                  String obs) {
        singletonInstance.open();

        ContentValues contentValues = new ContentValues();
        contentValues.put("num_pedido", id);
        contentValues.put("nombre_usuario", nombreUsr);
        contentValues.put("fecha_pedido", fecha);
        contentValues.put("direccion_envio", dir);
        contentValues.put("localidad_envio", localidad);
        contentValues.put("codigo_postal_envio", cp);
        contentValues.put("importe_total", importe);
        contentValues.put("observaciones", obs);

        long res = singletonInstance.getDb().insertOrThrow("pedidos", null, contentValues);

        singletonInstance.close();

        return res != 1;
    }

    //con el id buscamos las caracteristicas del producto
    public Comida getDatosComida(int idComida, int cantidad){
        singletonInstance.open();

        String toQuery = String.valueOf(idComida);
        Cursor c = singletonInstance.getDb().rawQuery("SELECT * FROM comidas WHERE codigo_comida = ?", new String[] { toQuery });
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

        return toret;
    }

    //Cogemos los objetos del carrito
    public ArrayList<Carrito> getObjetosEnCarro (String usuario){
        singletonInstance.open();

        Cursor c = singletonInstance.getDb().rawQuery("SELECT * FROM carrito_temp WHERE nombre_usuario = ?", new String[] { usuario });
        ArrayList<Carrito> toret = new ArrayList<Carrito>();
        while(c.moveToNext()){
            int codigo_comida = c.getInt(1);
            int cantidad = c.getInt(2);
            String usr = c.getString(0);
            toret.add(new Carrito(codigo_comida,cantidad,usr));
        }

        singletonInstance.close();

        return toret;
    }

    public int getIdLineaConCodigoComida(int codigoComida,String usuario){
        singletonInstance.open();

        String toQuery = String.valueOf(codigoComida);
        Cursor c = singletonInstance.getDb().rawQuery("SELECT * FROM carrito_temp WHERE codigo_comida = ? AND nombre_usuario = ?", new String[] { toQuery,usuario });
        int toret = 0;
        while (c.moveToNext()){
            int codigoLinea = c.getInt(3);
            toret = codigoLinea;
        }

        singletonInstance.close();

        return toret;
    }

    public int getIdLineaConCantidad(int codigoComida,String usuario, int cantidad){
        singletonInstance.open();

        String toQuery = String.valueOf(codigoComida);
        String cantidadStr = String.valueOf(cantidad);
        Cursor c = singletonInstance.getDb().rawQuery("SELECT * FROM carrito_temp WHERE codigo_comida = ? AND nombre_usuario = ? AND cantidad = ?", new String[] { toQuery,usuario,cantidadStr });
        int toret = 0;
        while (c.moveToNext()){
            int codigoLinea = c.getInt(3);
            toret = codigoLinea;
        }

        singletonInstance.close();

        return toret;
    }

    //eliminamos la comida del carrito cuando ya la hemos comprado
    public boolean eliminarProductorComprados(String usuario){
        singletonInstance.open();
        boolean toret = singletonInstance.getDb().delete("carrito_temp","nombre_usuario=?",new String[]{usuario})>0;
        singletonInstance.close();
        return toret;
    }

    public boolean eliminarProductorCompradosConCodigo(int codigoLinea, String usuario){
        singletonInstance.open();
        String toQuery = String.valueOf(codigoLinea);
        boolean toret = singletonInstance.getDb().delete("carrito_temp","id_linea_carrito_temp=? and nombre_usuario=?", new String[]{toQuery,usuario})>0;
        singletonInstance.close();

        return toret;
    }

    public boolean addUnProductoCarrito(int idProducto, int numeroActual){
        singletonInstance.open();

        ContentValues cv = new ContentValues();
        String toQuery = String.valueOf(idProducto);
        String actualizacion = String.valueOf(numeroActual);
        String cantidadActual = String.valueOf(numeroActual-1);
        cv.put("cantidad", actualizacion);

        boolean toret = singletonInstance.getDb().update("carrito_temp",cv,"id_linea_carrito_temp=? and cantidad=?",new String[]{toQuery,cantidadActual})>0;
        singletonInstance.close();

        return toret;
    }

    public boolean deleteUnProductoCarrito(int idProducto,int numeroActual){
        singletonInstance.open();

        ContentValues cv = new ContentValues();
        String toQuery = String.valueOf(idProducto);
        String actualizacion = String.valueOf(numeroActual);
        String cantidadActual = String.valueOf(numeroActual+1);
        cv.put("cantidad", actualizacion);

        boolean toret = singletonInstance.getDb().update("carrito_temp",cv,"id_linea_carrito_temp=? and cantidad=?",new String[]{toQuery,cantidadActual})>0;

        singletonInstance.close();

        return toret;
    }

    public boolean insertarLineaPedido(int idLinea,int numPedido, int codigoComida, int cantidad, double precio){
        singletonInstance.open();
        ContentValues contentValues = new ContentValues();
        contentValues.put("num_linea",idLinea);
        contentValues.put("num_pedido",numPedido);
        contentValues.put("codigo_comida",codigoComida);
        contentValues.put("cantidad",cantidad);
        contentValues.put("precio_linea",precio);
        long res = singletonInstance.getDb().insertOrThrow("linea_pedidos", null, contentValues);

        singletonInstance.close();
        return res != 1;
    }

    public int getMaxIdPedido(){
        singletonInstance.open();

        Cursor c = singletonInstance.getDb().rawQuery("SELECT MAX(num_pedido)  AS max_id FROM pedidos", null);
        String toret = "";
        if (c.moveToNext()) {
            toret = c.getString(0);
        }

        singletonInstance.close();

        return Integer.parseInt(toret);
    }

    public int getMaxIdLineaPedido() {
        singletonInstance.open();

        Cursor c = singletonInstance.getDb().rawQuery("SELECT MAX(num_linea)  AS max_id FROM linea_pedidos", null);
        String toret = "";
        if (c.moveToNext()) {
            toret = c.getString(0);
        }

        singletonInstance.close();

        return Integer.parseInt(toret);
    }

    public int getIdComida(String nombre) {
        singletonInstance.open();

        Cursor cursor = singletonInstance.getDb().rawQuery("select codigo_comida from comidas where nombre = ?",
                new String[] { nombre });
        int toret = 0;
        if (cursor.moveToNext()) {
            toret = cursor.getInt(0);
        }

        singletonInstance.close();

        return toret;
    }

    public boolean insertarUsuario(UsuarioRegistro ur) {
        singletonInstance.open();

        ContentValues cv = new ContentValues();
        cv.put("nombre_usuario", ur.getUsuario());
        cv.put("pass", OperationsUser.hashearMD5(ur.getPass()));
        cv.put("nombre_real", ur.getNombreReal());
        cv.put("apellidos", ur.getApellidos());
        cv.put("mail", ur.getCorreo());
        cv.put("telefono", ur.getTlfno());
        cv.put("direccion", ur.getDireccion());
        cv.put("localidad", ur.getLocalidad());
        cv.put("codigo_postal", ur.getCp());
        cv.put("fecha_alta", ur.getFechaAlta());

        long res = singletonInstance.getDb().insertOrThrow("usuarios", null, cv);

        singletonInstance.close();

        return res != -1;
    }

    public boolean checkLogin(String usuario, String pass) {
        singletonInstance.open();

        Cursor cursor = singletonInstance.getDb().rawQuery("select count(nombre_usuario) from usuarios " +
                        "where nombre_usuario = ? and pass = ?",
                new String[] { usuario, pass });

        int result = 0;
        while (cursor.moveToNext()) {
            result = cursor.getInt(0);
        }

        singletonInstance.close();

        return result == 1;

    }

    public boolean insertarLineaCarrito(String username, String codigoComida, int cantidad){
        singletonInstance.open();

        ContentValues contentValues = new ContentValues();
        contentValues.put("nombre_usuario",username);
        contentValues.put("codigo_comida",codigoComida);
        contentValues.put("cantidad",cantidad);
        long res = singletonInstance.getDb().insertOrThrow("carrito_temp", null, contentValues);

        singletonInstance.close();

        return res != 1;
    }
}
