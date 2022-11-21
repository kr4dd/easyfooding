package esei.uvigo.easyfooding.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;

import esei.uvigo.easyfooding.database.DatabaseAccess;

public class ModeloPedido {
    public DatabaseAccess singletonInstance;

    public ModeloPedido(Context c) {
        singletonInstance = DatabaseAccess.getInstance(c);
    }

    public Cursor getHistorialCursor(String usuario) {
        singletonInstance.openR();

        Cursor c =
                singletonInstance
                        .getDb()
                        .rawQuery(
                                "SELECT num_pedido as _id, nombre_usuario,fecha_pedido,direccion_envio,localidad_envio,codigo_postal_envio,importe_total,observaciones FROM pedidos WHERE nombre_usuario = ?",
                                new String[]{usuario});
        return c;
    }
}
