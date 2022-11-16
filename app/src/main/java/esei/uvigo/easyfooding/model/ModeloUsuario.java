package esei.uvigo.easyfooding.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import esei.uvigo.easyfooding.core.OperationsUser;
import esei.uvigo.easyfooding.core.UsuarioRegistro;
import esei.uvigo.easyfooding.database.DatabaseAccess;

public class ModeloUsuario {

    public DatabaseAccess singletonInstance;

    public ModeloUsuario(Context c) {
        singletonInstance = DatabaseAccess.getInstance(c);
    }

    public boolean checkLogin(String usuario, String pass) {
        singletonInstance.open();

        Cursor cursor = singletonInstance
                        .getDb()
                        .rawQuery(
                                "select count(nombre_usuario) from usuarios "
                                        + "where nombre_usuario = ? and pass = ?",
                                new String[] {usuario, pass});

        int result = 0;
        while (cursor.moveToNext()) {
            result = cursor.getInt(0);
        }

        singletonInstance.close();

        return result == 1;
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
}
