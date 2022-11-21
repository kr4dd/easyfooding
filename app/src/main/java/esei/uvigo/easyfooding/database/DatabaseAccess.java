package esei.uvigo.easyfooding.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;

import esei.uvigo.easyfooding.entities.User;
import esei.uvigo.easyfooding.core.Comida;
import esei.uvigo.easyfooding.core.LineaPedidos;
import esei.uvigo.easyfooding.core.ListaPedidos;

public class DatabaseAccess {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase db;
    private static DatabaseAccess instance;

    // private constructor
    public DatabaseAccess(Context context) {
        this.openHelper = new DatabaseOpenHelper(context);
    }

    // to return the single instance of database
    public static DatabaseAccess getInstance(Context context) {
        if (instance == null) instance = new DatabaseAccess(context);

        return instance;
    }

    // open the database connection
    public void openW() {
        this.db = openHelper.getWritableDatabase();
    }

    // open the database connection
    public void openR() {
        this.db = openHelper.getReadableDatabase();
    }

    // close the database connection
    public void close() {
        if (db != null) this.db.close();
    }

    public SQLiteDatabase getDb() {
        return db;
    }

    public User getCurrentUser (String username)
    {
        Cursor c = db.rawQuery("SELECT * FROM usuarios WHERE nombre_usuario = ?", new String[] { username });
        User dbEntity = new User();

        while (c.moveToNext()) {
            int nombre_usuario_column_index = c.getColumnIndex("nombre_usuario");
            int nombre_real_column_index = c.getColumnIndex("nombre_real");
            int apellidos_column_index = c.getColumnIndex("apellidos");
            int mail_column_index = c.getColumnIndex("mail");
            int telefono_column_index = c.getColumnIndex("telefono");
            int direccion_column_index = c.getColumnIndex("direccion");
            int localidad_column_index = c.getColumnIndex("localidad");
            int codigo_postal_column_index = c.getColumnIndex("codigo_postal");

            dbEntity.setNombre_usuario(c.getString(nombre_usuario_column_index));
            dbEntity.setNombre_real(c.getString(nombre_real_column_index));
            dbEntity.setApellidos(c.getString(apellidos_column_index));
            dbEntity.setMail(c.getString(mail_column_index));
            dbEntity.setTelefono(c.getString(telefono_column_index));
            dbEntity.setDireccion(c.getString(direccion_column_index));
            dbEntity.setLocalidad(c.getString(localidad_column_index));
            dbEntity.setCodigo_postal(c.getInt(codigo_postal_column_index));
        }

        return dbEntity;
    }

    public int UpdateUser (User currentUser)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put("nombre_real", currentUser.getNombre_real());
        contentValues.put("apellidos", currentUser.getApellidos());
        contentValues.put("mail", currentUser.getMail());
        contentValues.put("telefono", currentUser.getTelefono());
        contentValues.put("localidad", currentUser.getLocalidad());
        contentValues.put("direccion", currentUser.getDireccion());
        contentValues.put("codigo_postal", currentUser.getCodigo_postal());

        return db.update("usuarios", contentValues, "nombre_usuario = ?", new String [] { currentUser.getNombre_usuario() });
    }

    public int DeleteUser (String username)
    {
        return db.delete("usuarios", "nombre_usuario = ?", new String [] { username });
    }
}
