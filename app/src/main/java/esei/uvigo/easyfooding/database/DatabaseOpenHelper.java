package esei.uvigo.easyfooding.database;

import android.content.Context;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;


public class DatabaseOpenHelper extends SQLiteAssetHelper {

    //Mismo nombre que en el archivo de /assets/databases/
    private static final String DB_NAME = "easyFooding.db";
    private static final int DB_VERSION = 1;

    //constructor
    public DatabaseOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

}
