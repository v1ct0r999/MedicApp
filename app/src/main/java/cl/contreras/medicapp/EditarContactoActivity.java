package cl.contreras.medicapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class EditarContactoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_editar_contacto);

        Button botonEditarNombreContacto = (Button) findViewById(R.id.btnEditarNombreContacto);
        Button botonEditarFonoContacto = (Button) findViewById(R.id.btnEditarFonoContacto);
        Button botonVolverEditarContacto = (Button) findViewById(R.id.btnVolverEditarContacto);

        // Conectar el layout EditarContacto con EditarNombreContacto
        botonEditarNombreContacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditarContactoActivity.this, EditarNombreContactoActivity.class));
            }
        });

        // Conectar el layout EditarContacto con EditarNumeroContacto
        botonEditarFonoContacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditarContactoActivity.this, EditarNumeroContactoActivity.class));
            }
        });

        // Volver al MenuContacto
        botonVolverEditarContacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditarContactoActivity.this, MenuContactoActivity.class));
            }
        });

    }
}