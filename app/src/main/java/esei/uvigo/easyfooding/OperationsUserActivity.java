package esei.uvigo.easyfooding;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class OperationsUserActivity {
    private SharedPreferences sp;

    public static String hashearMD5(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");

            byte[] messageDigest = md.digest(password.getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            StringBuilder hashtext = new StringBuilder(no.toString(16));
            while (hashtext.length() < 32) {
                hashtext.insert(0, "0");
            }
            return hashtext.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static void showToastMsg(Context context, String msg) {
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        toast.show();
    }

    public static void setSession(Context context, String usuario) {
        SharedPreferences.Editor ed = context.getSharedPreferences("data", Context.MODE_PRIVATE).edit();
        ed.putString("nombre_usuario", usuario);
        ed.apply();
    }

    public static String getUserFromSession(Context context) {
        SharedPreferences sp = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        return sp.getString("nombre_usuario", "");
    }
}
