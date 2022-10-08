package esei.uvigo.easyfooding;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

public class TicketActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);
        Bundle datos = getIntent().getExtras();
        int num_pedido = datos.getInt("numero_pedido");
        Toast.makeText(TicketActivity.this,String.valueOf(num_pedido),Toast.LENGTH_LONG).show();
    }
}