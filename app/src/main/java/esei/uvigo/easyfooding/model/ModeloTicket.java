package esei.uvigo.easyfooding.model;

import android.content.Context;
import android.database.Cursor;

import esei.uvigo.easyfooding.database.DatabaseAccess;

public class ModeloTicket {
    public DatabaseAccess singletonInstance;

    public ModeloTicket(Context c) {
        singletonInstance = DatabaseAccess.getInstance(c);
    }

    public Cursor getCursorLineaPedidos(int numPedido) {
        singletonInstance.openR();

        String toQuery = String.valueOf(numPedido);
        Cursor c =
                singletonInstance
                        .getDb()
                        .rawQuery(
                                "SELECT num_linea as _id, num_pedido, codigo_comida, cantidad, precio_linea  FROM linea_pedidos WHERE num_pedido = ?",
                                new String[] {toQuery});
        return c;
    }

}
