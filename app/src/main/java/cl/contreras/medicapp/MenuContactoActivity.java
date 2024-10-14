package cl.contreras.medicapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MenuContactoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu_contacto);

        Button botonNuevoContacto = (Button) findViewById(R.id.btnNuevoContacto);
        Button botonEditarContacto = (Button) findViewById(R.id.btnEditarContacto);
        Button botonEliminarContacto = (Button) findViewById(R.id.btnEliminar);
        Button botonVolverContacto = (Button) findViewById(R.id.btnVolverContacto);


        // Conectar el layout menucontacto con agregarcontacto
        botonNuevoContacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuContactoActivity.this, AgregarContactoActivity.class));
            }
        });

        // Conectar el layout menucontacto con editarcontacto
        botonEditarContacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuContactoActivity.this, MenuEditarContactoActivity.class));
            }
        });

        // Conectar el layout menucontacto con eliminarcontacto
        botonEliminarContacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuContactoActivity.this, EliminarContactoActivity.class));
            }
        });

        // Volver al Menu
        botonVolverContacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuContactoActivity.this, MainActivity.class));
            }
        });

    }
}