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

public class EditarNumeroContactoActivity extends AppCompatActivity {

    private TextView NumeroContactoActual;
    private EditText NuevoNumeroContacto;
    private Button btnAceptarEditarNumeroContacto, btnVolverEditarNumeroContacto;
    private DatabaseHelper dbHelper;
    private int contactoId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_numero_contacto);

        // Enlazar elementos de la interfaz
        NumeroContactoActual = findViewById(R.id.NumeroContactoActual);
        NuevoNumeroContacto = findViewById(R.id.NuevoNumeroContacto);
        btnAceptarEditarNumeroContacto = findViewById(R.id.btnAceptarEditarNumeroContacto);
        btnVolverEditarNumeroContacto = findViewById(R.id.btnVolverEditarNumeroContacto);

        dbHelper = new DatabaseHelper(this);

        // Obtener el ID de la alarma que se está editando desde el Intent
        contactoId = getIntent().getIntExtra("contactos_id", -1);

        // Si no se ha recibido un ID válido, mostrar un mensaje de error
        if (contactoId == -1) {
            Toast.makeText(this, "Error: ID del contacto no encontrado", Toast.LENGTH_SHORT).show();
            finish(); // Terminar la actividad si no se recibe un ID válido
        }

        // Obtener la dosis actual de la alarma desde la base de datos
        cargarNumeroActual();

        // Acción del botón Aceptar
        btnAceptarEditarNumeroContacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener el valor ingresado por el usuario
                String nuevoNumero = NuevoNumeroContacto.getText().toString().trim();

                // Verificar que se haya ingresado un nuevo número
                if (nuevoNumero.isEmpty()) {
                    Toast.makeText(EditarNumeroContactoActivity.this, "Por favor ingrese un nuevo número.", Toast.LENGTH_SHORT).show();
                } else {
                    // Llamar a la función para actualizar el número en la base de datos
                    actualizarNumero(nuevoNumero);
                }
            }
        });


        // Acción del botón Cancelar
        btnVolverEditarNumeroContacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cancelar y regresar a la actividad anterior
                finish();
            }
        });
    }

    private void cargarNumeroActual() {
        // Verificar si contactoId existe en la base de datos
        String query = "SELECT * FROM " + DatabaseHelper.TABLE_CONTACTOS + " WHERE " +
                DatabaseHelper.COLUMN_ID_CONTACTO + " = ?";
        Cursor cursor = dbHelper.getReadableDatabase().rawQuery(query, new String[]{String.valueOf(contactoId)});

        if (cursor != null && cursor.moveToFirst()) {
            // Si se encuentra el contacto, obtener el nombre
            String numeroContacto = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TELEFONO));
            NumeroContactoActual.setText(numeroContacto);
            cursor.close();
        } else {
            // Si no se encuentra el contacto, mostrar un mensaje
            Toast.makeText(this, "Contacto no encontrado con ID: " + contactoId, Toast.LENGTH_SHORT).show();
        }
    }



    private void actualizarNumero(String nuevoNumero) {
        // Usar la instancia existente de DatabaseHelper
        boolean resultado = dbHelper.editarNumeroContacto(contactoId, nuevoNumero);

        if (resultado) {
            // Si se actualizó correctamente, mostrar un mensaje de éxito
            Toast.makeText(this, "Número actualizado con éxito", Toast.LENGTH_SHORT).show();
            finish(); // Regresar a la actividad anterior
        } else {
            // Si hubo un error, mostrar un mensaje de error
            Toast.makeText(this, "Error al actualizar el número", Toast.LENGTH_SHORT).show();
        }
    }



}