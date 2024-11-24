package cl.contreras.medicapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import cl.contreras.medicapp.db.Alarmas;
import cl.contreras.medicapp.db.DatabaseHelper;

public class DetallesAlarmasActivity extends AppCompatActivity {

    TextView Nombre, Dosis, Stock, Frecuencia;
    Button Volver;

    Alarmas alarma;
    int id = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_alarmas);

        Volver = findViewById(R.id.btnVolverDetalles);
        Nombre = findViewById(R.id.detalleNombre);
        Dosis = findViewById(R.id.detalleDosis);
        Stock = findViewById(R.id.detalleStock);
        Frecuencia = findViewById(R.id.detalleFrecuencia);

        /*
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                id = Integer.parseInt(null);
            } else {
                id = extras.getInt("id");
            }
        } else {
            id = (int) savedInstanceState.getSerializable("id");
        }
        */
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                id = extras.getInt("ID", -1); // Cambia "id" por "ID" para que coincida con lo que pasas
            }
        } else {
            id = savedInstanceState.getInt("ID", -1); // Cambia aquí también
        }

        DatabaseHelper dbAlarmas = new DatabaseHelper(DetallesAlarmasActivity.this);
        if (id != -1) {
            alarma = dbAlarmas.detalleAlarma(id);
        } else {
            Log.e("DetallesAlarmasActivity", "ID no válido: " + id);
            // Aquí puedes manejar el caso en que el ID no sea válido, como mostrar un mensaje al usuario
        }

        if (alarma != null) {
            Nombre.setText(alarma.getNombre());
            Dosis.setText(alarma.getDosis());
            Stock.setText(alarma.getStock());
            Frecuencia.setText(alarma.getFrecuencia());
        }

        Volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cancelar y regresar a la actividad anterior
                finish();
            }
        });

    }
}