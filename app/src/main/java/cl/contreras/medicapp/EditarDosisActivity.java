package cl.contreras.medicapp;

import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import cl.contreras.medicapp.db.Alarmas;
import cl.contreras.medicapp.db.DatabaseHelper;

public class EditarDosisActivity extends AppCompatActivity {

    private TextView textViewDosisActual;
    private EditText editTextNuevaDosis;
    private Button buttonAceptarDosis, buttonCancelarDosis;
    Alarmas alarma;
    int id = -1;
    boolean correcto = false;
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
        /*
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
        */
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                id = extras.getInt("ID", -1); // Cambia "id" por "ID" para que coincida con lo que pasas
            }
        } else {
            id = savedInstanceState.getInt("ID", -1); // Cambia aquí también
        }

        DatabaseHelper dbAlarmas = new DatabaseHelper(EditarDosisActivity.this);
        if (id != -1) {
            alarma = dbAlarmas.detalleAlarma(id);
        } else {
            Log.e("DetallesAlarmasActivity", "ID no válido: " + id);
            // Aquí puedes manejar el caso en que el ID no sea válido, como mostrar un mensaje al usuario
        }

        if (alarma != null) {
            editTextNuevaDosis.setText(alarma.getDosis());
        }

        buttonAceptarDosis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!editTextNuevaDosis.getText().toString().equals("")) {
                    dbAlarmas.editarALarmaDosis(id, editTextNuevaDosis.getText().toString());

                    if (correcto) {
                        Toast.makeText(EditarDosisActivity.this, "REGISTRO MODIFICADO", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(EditarDosisActivity.this, "ERROR AL MODIFCAR ALARMA ", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(EditarDosisActivity.this, "DEBE LLENAR EL CAMPO OBLIGATORI", Toast.LENGTH_SHORT).show();
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