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

public class EditarFrecuenciaActivity extends AppCompatActivity {

    private TextView textViewFrecuenciaActual;
    private EditText editTextNuevaFrecuencia;
    private Button buttonAceptarFrecuencia, buttonCancelarFrecuencia;
    private DatabaseHelper dbHelper;
    private int alarmaId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_frecuencia);

        // Enlazar elementos de la interfaz
        textViewFrecuenciaActual = findViewById(R.id.textViewFrecuenciaValor);
        editTextNuevaFrecuencia = findViewById(R.id.editTextNuevaFrecuencia);
        buttonAceptarFrecuencia = findViewById(R.id.buttonAceptarFrecuencia);
        buttonCancelarFrecuencia = findViewById(R.id.buttonCancelarFrecuencia);


        dbHelper = new DatabaseHelper(this);

        alarmaId = getIntent().getIntExtra("alarma_id", -1);

        // Si no se ha recibido un ID válido, mostrar un mensaje de error
        if (alarmaId == -1) {
            Toast.makeText(this, "Error: ID de alarma no encontrado", Toast.LENGTH_SHORT).show();
            finish(); // Terminar la actividad si no se recibe un ID válido
        }

        cargarfrecuenciaActual();

        // Acción del botón Aceptar
        buttonAceptarFrecuencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener el valor ingresado por el usuario
                String nuevaf = editTextNuevaFrecuencia.getText().toString().trim();

                // Verificar que se haya ingresado una nueva dosis
                if (nuevaf.isEmpty()) {
                    Toast.makeText(EditarFrecuenciaActivity.this, "Por favor ingrese una nueva frencuencia.", Toast.LENGTH_SHORT).show();
                } else {
                    // Llamar a la función para actualizar la dosis en la base de datos
                    actualizarfrecuencia(nuevaf);
                }
            }
        });

        // Acción del botón Cancelar
        buttonCancelarFrecuencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cancelar y regresar a la actividad anterior
                finish();
            }
        });

    }

    private void cargarfrecuenciaActual() {
        // Verificar si alarmaId existe en la base de datos
        String query = "SELECT * FROM " + DatabaseHelper.TABLE_NAME + " WHERE " +
                DatabaseHelper.COLUMN_ID + " = ?";
        Cursor cursor = dbHelper.getReadableDatabase().rawQuery(query, new String[]{String.valueOf(alarmaId)});

        if (cursor != null && cursor.moveToFirst()) {
            // Si se encuentra la alarma, obtener la dosis
            String dosis = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_FRECUENCIA));
            textViewFrecuenciaActual.setText(dosis);
            cursor.close();
        } else {
            // Si no se encuentra la alarma, mostrar un mensaje
            Toast.makeText(this, "Alarma no encontrada con ID: " + alarmaId, Toast.LENGTH_SHORT).show();
        }
    }



    // Método para actualizar la dosis de la alarma en la base de datos
    private void actualizarfrecuencia(String nuevaf) {
        DatabaseHelper dbHelper = new DatabaseHelper(this);

        // Llamar a la función de la base de datos para actualizar la dosis
        boolean resultado = dbHelper.editarAlarmaFrecuencia(alarmaId, Integer.parseInt(nuevaf));

        if (resultado) {
            // Si se actualizó correctamente, mostrar un mensaje de éxito
            Toast.makeText(this, "Frecuencia actualizada con éxito", Toast.LENGTH_SHORT).show();
            finish();  // Regresar a la actividad anterior
        } else {
            // Si hubo un error, mostrar un mensaje de error
            Toast.makeText(this, "Error al actualizar la frecuencia", Toast.LENGTH_SHORT).show();
        }
    }
}
