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

public class EditarStockActivity extends AppCompatActivity {

    private TextView textViewStockActual;
    private EditText editTextNuevoStock;
    private Button buttonAceptarStock, buttonCancelarStock;
    private DatabaseHelper dbHelper;
    private int alarmaId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_stock);

        // Enlazar elementos de la interfaz
        textViewStockActual = findViewById(R.id.textViewStockValor);
        editTextNuevoStock = findViewById(R.id.editTextNuevoStock);
        buttonAceptarStock = findViewById(R.id.buttonAceptarStock);
        buttonCancelarStock = findViewById(R.id.buttonCancelarStock);

        dbHelper = new DatabaseHelper(this);

        alarmaId = getIntent().getIntExtra("alarma_id", -1);

        // Si no se ha recibido un ID válido, mostrar un mensaje de error
        if (alarmaId == -1) {
            Toast.makeText(this, "Error: ID de alarma no encontrado", Toast.LENGTH_SHORT).show();
            finish(); // Terminar la actividad si no se recibe un ID válido
        }

        cargarstockactual();

        // Acción del botón Aceptar
        buttonAceptarStock.setOnClickListener(new View.OnClickListener() {
            @Override
             public void onClick(View v) {
                // Obtener el valor ingresado por el usuario
                String nuevoStock = editTextNuevoStock.getText().toString().trim();

                // Verificar que se haya ingresado una nueva dosis
                if (nuevoStock.isEmpty()) {
                    Toast.makeText(EditarStockActivity.this, "Por favor ingrese un nuevo stock.", Toast.LENGTH_SHORT).show();
                } else {
                    // Llamar a la función para actualizar la stcok en la base de datos
                    actualizarstock(nuevoStock);
                }
            }
        });

        // Acción del botón Cancelar
        buttonCancelarStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cancelar y regresar a la actividad anterior
                finish();
            }
        });

    }

    private void cargarstockactual() {
        // Verificar si alarmaId existe en la base de datos
        String query = "SELECT * FROM " + DatabaseHelper.TABLE_NAME + " WHERE " +
                DatabaseHelper.COLUMN_ID + " = ?";
        Cursor cursor = dbHelper.getReadableDatabase().rawQuery(query, new String[]{String.valueOf(alarmaId)});

        if (cursor != null && cursor.moveToFirst()) {
            String stock = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_STOCK));
            textViewStockActual.setText(stock);
            cursor.close();
        } else {
            // Si no se encuentra la alarma, mostrar un mensaje
            Toast.makeText(this, "Alarma no encontrada con ID: " + alarmaId, Toast.LENGTH_SHORT).show();
        }
    }



    // Método para actualizar la dosis de la alarma en la base de datos
    private void actualizarstock(String nuevost) {
        DatabaseHelper dbHelper = new DatabaseHelper(this);

        // Llamar a la función de la base de datos para actualizar la dosis
        boolean resultado = dbHelper.editarAlarmaStock(alarmaId, Integer.parseInt(nuevost));

        if (resultado) {
            // Si se actualizó correctamente, mostrar un mensaje de éxito
            Toast.makeText(this, "Stock actualizado con éxito", Toast.LENGTH_SHORT).show();
            finish();  // Regresar a la actividad anterior
        } else {
            // Si hubo un error, mostrar un mensaje de error
            Toast.makeText(this, "Error al actualizar el stock", Toast.LENGTH_SHORT).show();
        }
    }
}

