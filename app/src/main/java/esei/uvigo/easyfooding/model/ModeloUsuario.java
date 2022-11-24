package esei.uvigo.easyfooding.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;

import esei.uvigo.easyfooding.core.OperationsUser;
import esei.uvigo.easyfooding.core.UsuarioRegistro;
import esei.uvigo.easyfooding.database.DatabaseAccess;
import esei.uvigo.easyfooding.entities.User;

public class ModeloUsuario {

    public DatabaseAccess singletonInstance;

    public ModeloUsuario(Context c) {
        singletonInstance = DatabaseAccess.getInstance(c);
    }

    public boolean checkLogin(String usuario, String pass) {
        singletonInstance.openR();

        Cursor cursor = null;
        int result = 0;
        try {

            cursor = singletonInstance
                    .getDb()
                    .rawQuery(
                            "select count(nombre_usuario) from usuarios "
                                    + "where nombre_usuario = ? and pass = ?",
                            new String[]{usuario, pass});

            while (cursor.moveToNext()) {
                result = cursor.getInt(0);
            }


        } catch (SQLException e) {
            Log.e("DBManager.checkLogin", e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }

            singletonInstance.close();
        }

        return result == 1;
    }

    public boolean insertarUsuario(UsuarioRegistro ur) {
        singletonInstance.openW();

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

        long res = -1;
        try {
            singletonInstance.getDb().beginTransaction();

            res = singletonInstance.getDb().insertOrThrow("usuarios", null, cv);

            singletonInstance.getDb().setTransactionSuccessful();
        } catch (SQLException e) {
            Log.e("DBManager.insertarUsuario", e.getMessage());
        } finally {
            singletonInstance.getDb().endTransaction();
            singletonInstance.close();
        }

        return res != -1;
    }

    public boolean existeUsuario(String usuario) {
        singletonInstance.openR();

        Cursor cursor = null;
        boolean result = false;
        try {

            cursor = singletonInstance
                    .getDb()
                    .rawQuery(
                            "select count(nombre_usuario) from usuarios "
                                    + "where nombre_usuario = ?",
                            new String[]{usuario});


            while (cursor.moveToNext()) {
                if (cursor.getInt(0) >= 1) {
                    result = true;
                }
            }

        } catch (SQLException e) {
            Log.e("DBManager.existeUsuario", e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }

            singletonInstance.close();
        }

        return result;
    }

    public boolean existeCorreo(String email) {
        singletonInstance.openR();

        Cursor cursor = null;
        boolean result = false;
        try {

            cursor = singletonInstance
                    .getDb()
                    .rawQuery(
                            "select count(mail) from usuarios "
                                    + "where mail = ?",
                            new String[]{email});

            while (cursor.moveToNext()) {
                if (cursor.getInt(0) >= 1) {
                    result = true;
                }
            }

        } catch (SQLException e) {
            Log.e("DBManager.existeCorreo", e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }

            singletonInstance.close();
        }

        return result;
    }

    public boolean UpdateUser (User currentUser)
    {
        singletonInstance.openW();

        ContentValues contentValues = new ContentValues();
        contentValues.put("nombre_real", currentUser.getNombre_real());
        contentValues.put("apellidos", currentUser.getApellidos());
        contentValues.put("mail", currentUser.getMail());
        contentValues.put("telefono", currentUser.getTelefono());
        contentValues.put("localidad", currentUser.getLocalidad());
        contentValues.put("direccion", currentUser.getDireccion());
        contentValues.put("codigo_postal", currentUser.getCodigo_postal());

        long res = -1;
        try {
            singletonInstance.getDb().beginTransaction();

            res = singletonInstance.getDb().update("usuarios", contentValues, "nombre_usuario = ?", new String [] { currentUser.getNombre_usuario() });

            singletonInstance.getDb().setTransactionSuccessful();
        } catch (SQLException e) {
            Log.e("DBManager.insertarUsuario", e.getMessage());
        } finally {
            singletonInstance.getDb().endTransaction();
            singletonInstance.close();
        }

        return res != -1;
    }

    public boolean DeleteUser (String username)
    {
        singletonInstance.openW();

        long res = -1;
        try {
            singletonInstance.getDb().beginTransaction();
            res = singletonInstance.getDb().delete("usuarios", "nombre_usuario = ?", new String [] { username });

            singletonInstance.getDb().setTransactionSuccessful();
        } catch (SQLException e) {
            Log.e("DBManager.insertarUsuario", e.getMessage());
        } finally {
            singletonInstance.getDb().endTransaction();
            singletonInstance.close();
        }

        return res != -1;
    }

    public User getCurrentUser (String username)
    {
        singletonInstance.openR();

        Cursor c = null;
        User result = new User();

        try {

            c = singletonInstance.getDb().rawQuery("SELECT * FROM usuarios WHERE nombre_usuario = ?", new String[] { username });

            while (c.moveToNext()) {
                int nombre_usuario_column_index = c.getColumnIndex("nombre_usuario");
                int nombre_real_column_index = c.getColumnIndex("nombre_real");
                int apellidos_column_index = c.getColumnIndex("apellidos");
                int mail_column_index = c.getColumnIndex("mail");
                int telefono_column_index = c.getColumnIndex("telefono");
                int direccion_column_index = c.getColumnIndex("direccion");
                int localidad_column_index = c.getColumnIndex("localidad");
                int codigo_postal_column_index = c.getColumnIndex("codigo_postal");

                result.setNombre_usuario(c.getString(nombre_usuario_column_index));
                result.setNombre_real(c.getString(nombre_real_column_index));
                result.setApellidos(c.getString(apellidos_column_index));
                result.setMail(c.getString(mail_column_index));
                result.setTelefono(c.getString(telefono_column_index));
                result.setDireccion(c.getString(direccion_column_index));
                result.setLocalidad(c.getString(localidad_column_index));
                result.setCodigo_postal(c.getInt(codigo_postal_column_index));
            }

        } catch (SQLException e) {
            Log.e("DBManager.existeCorreo", e.getMessage());
        } finally {
            if (c != null) {
                c.close();
            }

            singletonInstance.close();
        }

        return result;
    }
}
