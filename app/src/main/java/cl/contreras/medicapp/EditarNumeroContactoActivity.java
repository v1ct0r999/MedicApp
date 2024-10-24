package cl.contreras.medicapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class EditarNumeroContactoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_editar_numero_contacto);

        Button botonVolverEditarNumeroContacto = (Button) findViewById(R.id.btnVolverEditarNumeroContacto);

        // Volver al Menu
        botonVolverEditarNumeroContacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditarNumeroContactoActivity.this, MenuEditarContactoActivity.class));
            }
        });

    }
}