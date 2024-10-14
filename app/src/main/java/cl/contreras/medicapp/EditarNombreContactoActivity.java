package cl.contreras.medicapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class EditarNombreContactoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_editar_nombre_contacto);

        Button botonVolverEditarNombreContacto = (Button) findViewById(R.id.btnVolverEditarNombreContacto);

        // Volver al Menu
        botonVolverEditarNombreContacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditarNombreContactoActivity.this, MenuEditarContactoActivity.class));
            }
        });

    }
}