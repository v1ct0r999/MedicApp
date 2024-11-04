package cl.contreras.medicapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class AgregarContactoActivity extends AppCompatActivity {

    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_agregar_contacto);

        // Inicializar el DatabaseHelper
        dbHelper = new DatabaseHelper(this);

        Button botonVolverAgregarContacto = findViewById(R.id.btnVolverAgregarContacto);

        // Volver al menu contacto
        botonVolverAgregarContacto.setOnClickListener(v -> {
            startActivity(new Intent(AgregarContactoActivity.this, MenuContactoActivity.class));
        });

        EditText Itelefono = findViewById(R.id.telefonoContacto);
        EditText Inombre = findViewById(R.id.nombreContacto);
        Button botonguardarcontacto = findViewById(R.id.btnAceptarContacto);

        botonguardarcontacto.setOnClickListener(view -> {
            String telefono = Itelefono.getText().toString();
            String nombre = Inombre.getText().toString();

            if (telefono.isEmpty() || nombre.isEmpty()) {
                Toast.makeText(AgregarContactoActivity.this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show();
            } else {
                // Ahora dbHelper no es nulo
                boolean isInserted = dbHelper.addContacto(nombre, telefono);
                if (isInserted) {
                    // Obtener el ID del último contacto insertado
                    int contactoId = dbHelper.getLastInsertedId(); // Implementa este método en DatabaseHelper
                    Toast.makeText(AgregarContactoActivity.this, "Contacto guardado", Toast.LENGTH_SHORT).show();
                    finish(); // Volver a la actividad principal
                } else {
                    Toast.makeText(AgregarContactoActivity.this, "Error al guardar", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
