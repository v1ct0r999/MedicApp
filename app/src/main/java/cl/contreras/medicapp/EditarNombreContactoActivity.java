package cl.contreras.medicapp;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import cl.contreras.medicapp.db.DatabaseHelper;

public class EditarNombreContactoActivity extends AppCompatActivity {

    private TextView NombreContactoActual;
    private EditText NuevoNombreContacto;
    private Button btnAceptarEditarNombreContacto, btnVolverEditarNombreContacto;
    private DatabaseHelper dbHelper;
    private int contactoId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_nombre_contacto);

        // Enlazar elementos de la interfaz
        NombreContactoActual = findViewById(R.id.NombreContactoActual);
        NuevoNombreContacto = findViewById(R.id.NuevoNombreContacto);
        btnAceptarEditarNombreContacto = findViewById(R.id.btnAceptarEditarNombreContacto);
        btnVolverEditarNombreContacto = findViewById(R.id.btnVolverEditarNombreContacto);

        dbHelper = new DatabaseHelper(this);

        // Obtener el ID de la alarma que se está editando desde el Intent
        contactoId = getIntent().getIntExtra("contactos_id", -1);

        // Si no se ha recibido un ID válido, mostrar un mensaje de error
        if (contactoId == -1) {
            Toast.makeText(this, "Error: ID del contacto no encontrado", Toast.LENGTH_SHORT).show();
            finish(); // Terminar la actividad si no se recibe un ID válido
        }

        // Obtener la dosis actual de la alarma desde la base de datos
        cargarNombreActual();

        // Acción del botón Aceptar
        btnAceptarEditarNombreContacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener el valor ingresado por el usuario
                String nuevoNombre = NuevoNombreContacto.getText().toString().trim();

                // Verificar que se haya ingresado una nueva dosis
                if (nuevoNombre.isEmpty()) {
                    Toast.makeText(EditarNombreContactoActivity.this, "Por favor ingrese un nuevo nombre.", Toast.LENGTH_SHORT).show();
                } else {
                    // Llamar a la función para actualizar la dosis en la base de datos
                    actualizarNombre(nuevoNombre);
                }
            }
        });

        // Acción del botón Cancelar
        btnVolverEditarNombreContacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cancelar y regresar a la actividad anterior
                finish();
            }
        });
    }

    private void cargarNombreActual() {
        // Verificar si contactoId existe en la base de datos
        String query = "SELECT * FROM " + DatabaseHelper.TABLE_CONTACTOS + " WHERE " +
                DatabaseHelper.COLUMN_ID_CONTACTO + " = ?";
        Cursor cursor = dbHelper.getReadableDatabase().rawQuery(query, new String[]{String.valueOf(contactoId)});

        if (cursor != null && cursor.moveToFirst()) {
            // Si se encuentra el contacto, obtener el nombre
            String nombreContacto = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NOMBRE_CONTACTO));
            NombreContactoActual.setText(nombreContacto);
            cursor.close();
        } else {
            // Si no se encuentra el contacto, mostrar un mensaje
            Toast.makeText(this, "Contacto no encontrado con ID: " + contactoId, Toast.LENGTH_SHORT).show();
        }
    }



    private void actualizarNombre(String nuevoNombre) {
        DatabaseHelper dbHelper = new DatabaseHelper(this);

        // Llamar a la función de la base de datos para actualizar el nombre
        boolean resultado = dbHelper.editarNombreContacto(contactoId, nuevoNombre);

        if (resultado) {
            // Si se actualizó correctamente, mostrar un mensaje de éxito
            Toast.makeText(this, "Nombre actualizado con éxito", Toast.LENGTH_SHORT).show();
            finish();  // Regresar a la actividad anterior
        } else {
            // Si hubo un error, mostrar un mensaje de error
            Toast.makeText(this, "Error al actualizar el nombre", Toast.LENGTH_SHORT).show();
        }
    }


}