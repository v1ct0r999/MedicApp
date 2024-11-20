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

public class EditarDosisActivity extends AppCompatActivity {

    private TextView textViewDosisActual;
    private EditText editTextNuevaDosis;
    private Button buttonAceptarDosis, buttonCancelarDosis;
    private DatabaseHelper dbHelper;

    private int alarmaId;  // Variable para almacenar el ID de la alarma

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_dosis);

        // Enlazar elementos de la interfaz
        textViewDosisActual = findViewById(R.id.textViewDosisValor);
        editTextNuevaDosis = findViewById(R.id.editTextNuevaDosis);
        buttonAceptarDosis = findViewById(R.id.buttonAceptarDosis);
        buttonCancelarDosis = findViewById(R.id.buttonCancelarDosis);

        dbHelper = new DatabaseHelper(this);

        // Obtener el ID de la alarma que se está editando desde el Intent
        alarmaId = getIntent().getIntExtra("alarma_id", -1);

        // Si no se ha recibido un ID válido, mostrar un mensaje de error
        if (alarmaId == -1) {
            Toast.makeText(this, "Error: ID de alarma no encontrado", Toast.LENGTH_SHORT).show();
            finish(); // Terminar la actividad si no se recibe un ID válido
        }

        // Obtener la dosis actual de la alarma desde la base de datos
        cargarDosisActual();

        // Acción del botón Aceptar
        buttonAceptarDosis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener el valor ingresado por el usuario
                String nuevaDosis = editTextNuevaDosis.getText().toString().trim();

                // Verificar que se haya ingresado una nueva dosis
                if (nuevaDosis.isEmpty()) {
                    Toast.makeText(EditarDosisActivity.this, "Por favor ingrese una nueva dosis.", Toast.LENGTH_SHORT).show();
                } else {
                    // Llamar a la función para actualizar la dosis en la base de datos
                    actualizarDosis(nuevaDosis);
                }
            }
        });

        // Acción del botón Cancelar
        buttonCancelarDosis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cancelar y regresar a la actividad anterior
                finish();
            }
        });
    }

    private void cargarDosisActual() {
        // Verificar si alarmaId existe en la base de datos
        String query = "SELECT * FROM " + DatabaseHelper.TABLE_NAME + " WHERE " +
                DatabaseHelper.COLUMN_ID + " = ?";
        Cursor cursor = dbHelper.getReadableDatabase().rawQuery(query, new String[]{String.valueOf(alarmaId)});

        if (cursor != null && cursor.moveToFirst()) {
            // Si se encuentra la alarma, obtener la dosis
            String dosis = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DOSIS));
            textViewDosisActual.setText(dosis);
            cursor.close();
        } else {
            // Si no se encuentra la alarma, mostrar un mensaje
            Toast.makeText(this, "Alarma no encontrada con ID: " + alarmaId, Toast.LENGTH_SHORT).show();
        }
    }



    // Método para actualizar la dosis de la alarma en la base de datos
    private void actualizarDosis(String nuevaDosis) {
        DatabaseHelper dbHelper = new DatabaseHelper(this);

        // Llamar a la función de la base de datos para actualizar la dosis
        boolean resultado = dbHelper.editarAlarmaDosis(alarmaId, Integer.parseInt(nuevaDosis));

        if (resultado) {
            // Si se actualizó correctamente, mostrar un mensaje de éxito
            Toast.makeText(this, "Dosis actualizada con éxito", Toast.LENGTH_SHORT).show();
            finish();  // Regresar a la actividad anterior
        } else {
            // Si hubo un error, mostrar un mensaje de error
            Toast.makeText(this, "Error al actualizar la dosis", Toast.LENGTH_SHORT).show();
        }
    }
}
