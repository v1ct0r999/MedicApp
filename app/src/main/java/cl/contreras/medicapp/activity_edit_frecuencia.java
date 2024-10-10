package cl.contreras.medicapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class activity_edit_frecuencia extends AppCompatActivity {

    private TextView textViewFrecuenciaActual;
    private EditText editTextNuevaFrecuencia;
    private Button buttonAceptarFrecuencia, buttonCancelarFrecuencia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_frecuencia);

        // Enlazar elementos de la interfaz
        textViewFrecuenciaActual = findViewById(R.id.textViewFrecuenciaValor);
        editTextNuevaFrecuencia = findViewById(R.id.editTextNuevaFrecuencia);
        buttonAceptarFrecuencia = findViewById(R.id.buttonAceptarFrecuencia);
        buttonCancelarFrecuencia = findViewById(R.id.buttonCancelarFrecuencia);

        // Acción del botón Aceptar
        buttonAceptarFrecuencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener el valor ingresado
                String nuevaFrecuencia = editTextNuevaFrecuencia.getText().toString();

                // Validar que no esté vacío
                if (!nuevaFrecuencia.isEmpty()) {
                    // Aquí podrías guardar el valor de la nueva frecuencia o enviarlo a otra actividad
                    Toast.makeText(activity_edit_frecuencia.this, "Nueva frecuencia guardada: " + nuevaFrecuencia, Toast.LENGTH_SHORT).show();
                    // Opcional: Regresar a la actividad principal
                    finish();
                } else {
                    Toast.makeText(activity_edit_frecuencia.this, "Por favor, ingrese una nueva frecuencia.", Toast.LENGTH_SHORT).show();
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
}
