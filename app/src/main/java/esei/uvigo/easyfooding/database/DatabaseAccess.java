package esei.uvigo.easyfooding.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import esei.uvigo.easyfooding.R;
import esei.uvigo.easyfooding.UsuarioRegistro;
import esei.uvigo.easyfooding.core.Carrito;
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
    public void open() {
        this.db = openHelper.getWritableDatabase();
    }

    // close the database connection
    public void close() {
        if (db != null) this.db.close();
    }

    public SQLiteDatabase getDb() {
        return db;
    }
}
