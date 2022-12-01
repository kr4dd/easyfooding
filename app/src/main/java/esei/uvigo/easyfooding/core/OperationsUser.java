package esei.uvigo.easyfooding.core;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import esei.uvigo.easyfooding.R;
import esei.uvigo.easyfooding.view.CarritoActivity;
import esei.uvigo.easyfooding.view.InicioActivity;
import esei.uvigo.easyfooding.view.OptionsActivity;
import esei.uvigo.easyfooding.view.PedidosActivity;
import esei.uvigo.easyfooding.view.PerfilActivity;

public class OperationsUser {

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
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
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

    public static String getActualDateSpanishStrFormat() {
        Calendar cal = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(cal.getTime());
    }

    public static void cambiarActividadPanelInterno(View v, Activity a, Context c) {
        // Cambiar a la actividad de Inicio
        LinearLayout inicio = v.findViewById(R.id.inicio);
        inicio.setOnClickListener(
                view -> {
                    a.finish();
                    a.startActivity(new Intent(c, InicioActivity.class));
                });

        // Cambiar a la actividad Perfil
        LinearLayout perfil = v.findViewById(R.id.perfil);
        perfil.setOnClickListener(
                view -> {
                    a.finish();
                    a.startActivity(new Intent(c, PerfilActivity.class));
                });

        // Cambiar a la actividad Carrito
        LinearLayout carrito = v.findViewById(R.id.carrito);
        carrito.setOnClickListener(
                view -> {
                    a.finish();
                    a.startActivity(new Intent(c, CarritoActivity.class));
                });

        // Cambiar a la actividad Pedidos
        LinearLayout pedidos = v.findViewById(R.id.pedidos);
        pedidos.setOnClickListener(
                view -> {
                    a.finish();
                    a.startActivity(new Intent(c, PedidosActivity.class));
                });

        // Cambiar a la actividad Ajustes
        LinearLayout ajustes = v.findViewById(R.id.ajustes);
        ajustes.setOnClickListener(
                view -> {
                    a.finish();
                    a.startActivity(new Intent(c, OptionsActivity.class));
                });
    }

}
