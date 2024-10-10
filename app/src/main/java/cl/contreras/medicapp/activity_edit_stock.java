package cl.contreras.medicapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class activity_edit_stock extends AppCompatActivity {

    private TextView textViewStockActual;
    private EditText editTextNuevoStock;
    private Button buttonAceptarStock, buttonCancelarStock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_stock);

        // Enlazar elementos de la interfaz
        textViewStockActual = findViewById(R.id.textViewStockValor);
        editTextNuevoStock = findViewById(R.id.editTextNuevoStock);
        buttonAceptarStock = findViewById(R.id.buttonAceptarStock);
        buttonCancelarStock = findViewById(R.id.buttonCancelarStock);

        // Acción del botón Aceptar
        buttonAceptarStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener el valor ingresado
                String nuevoStock = editTextNuevoStock.getText().toString();

                // Validar que no esté vacío
                if (!nuevoStock.isEmpty()) {
                    // Aquí podrías guardar el valor del nuevo stock o enviarlo a otra actividad
                    Toast.makeText(activity_edit_stock.this, "Nuevo stock guardado: " + nuevoStock, Toast.LENGTH_SHORT).show();
                    // Opcional: Regresar a la actividad principal
                    finish();
                } else {
                    Toast.makeText(activity_edit_stock.this, "Por favor, ingrese el nuevo stock.", Toast.LENGTH_SHORT).show();
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
}

