package cl.contreras.medicapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class EditarDosisActivity extends AppCompatActivity {

    private TextView textViewDosisActual;
    private EditText editTextNuevaDosis;
    private Button buttonAceptarDosis, buttonCancelarDosis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_dosis);

        // Enlazar elementos de la interfaz
        textViewDosisActual = findViewById(R.id.textViewDosisValor);
        editTextNuevaDosis = findViewById(R.id.editTextNuevaDosis);
        buttonAceptarDosis = findViewById(R.id.buttonAceptarDosis);
        buttonCancelarDosis = findViewById(R.id.buttonCancelarDosis);

        // Acción del botón Aceptar
        buttonAceptarDosis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener el valor ingresado
                String nuevaDosis = editTextNuevaDosis.getText().toString();

                // Validar que no esté vacío
                if (!nuevaDosis.isEmpty()) {
                    // Aquí podrías guardar el valor del nuevo stock o enviarlo a otra actividad
                    Toast.makeText(EditarDosisActivity.this, "Nueva dosis guardada: " + nuevaDosis, Toast.LENGTH_SHORT).show();
                    // Opcional: Regresar a la actividad principal
                    finish();
                } else {
                    Toast.makeText(EditarDosisActivity.this, "Por favor, ingrese la nueva dosis.", Toast.LENGTH_SHORT).show();
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
}