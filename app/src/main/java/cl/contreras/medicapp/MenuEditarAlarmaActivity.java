package cl.contreras.medicapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import cl.contreras.medicapp.db.DatabaseHelper;

public class MenuEditarAlarmaActivity extends AppCompatActivity {

    private int alarmaId; // Variable para almacenar el ID de la alarma
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_editar_alarma);

        dbHelper = new DatabaseHelper(this);

        // Obtener el ID de la alarma que se está editando
        alarmaId = getIntent().getIntExtra("alarma_id", -1);

        Button btnEditarDosis = findViewById(R.id.btnEditarDosis);
        Button btnEditarFrecuencia = findViewById(R.id.btnEditarFrecuencia);
        Button btnEditarStock = findViewById(R.id.btnEditarStock);
        Button btnVolverEditarAlarma = findViewById(R.id.btnVolverEditarAlarma);

        btnEditarDosis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (alarmaId != -1) {  // Verifica que el ID de la alarma sea válido
                    Intent intent = new Intent(MenuEditarAlarmaActivity.this, EditarDosisActivity.class);
                    intent.putExtra("alarma_id", alarmaId);
                    startActivity(intent);
                }
            }
        });

        btnEditarFrecuencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (alarmaId != -1) {  // Verifica que el ID de la alarma sea válido
                    Intent intent = new Intent(MenuEditarAlarmaActivity.this, EditarFrecuenciaActivity.class);
                    intent.putExtra("alarma_id", alarmaId);
                    startActivity(intent);
                }
            }
        });

        btnEditarStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (alarmaId != -1) {  // Verifica que el ID de la alarma sea válido
                    Intent intent = new Intent(MenuEditarAlarmaActivity.this, EditarStockActivity.class);
                    intent.putExtra("alarma_id", alarmaId);
                    startActivity(intent);
                }
            }
        });

        btnVolverEditarAlarma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cancelar y regresar a la actividad anterior
                finish();
            }
        });
    }
}
