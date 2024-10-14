package cl.contreras.medicapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MenuEditarContactoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu_editar_contacto);

        Button botonEditarNombreContacto = (Button) findViewById(R.id.btnEditarNombreContacto);
        Button botonEditarFonoContacto = (Button) findViewById(R.id.btnEditarFonoContacto);
        Button botonVolverEditarContacto = (Button) findViewById(R.id.btnVolverEditarContacto);

        // Conectar el layout EditarContacto con EditarNombreContacto
        botonEditarNombreContacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuEditarContactoActivity.this, EditarNombreContactoActivity.class));
            }
        });

        // Conectar el layout EditarContacto con EditarNumeroContacto
        botonEditarFonoContacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuEditarContactoActivity.this, EditarNumeroContactoActivity.class));
            }
        });

        // Volver al MenuContacto
        botonVolverEditarContacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuEditarContactoActivity.this, MenuContactoActivity.class));
            }
        });

    }
}