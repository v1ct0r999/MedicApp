package cl.contreras.medicapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MenuEditarAlarmaActivity extends AppCompatActivity {

    private int alarmaId; // Variable para almacenar el ID de la alarma

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu_editar_alarma);

        // Obtener el ID de la alarma que se está editando
        alarmaId = getIntent().getIntExtra("alarmaId", -1);

        Button btnEditarDosis = findViewById(R.id.btnEditarDosis);
        Button btnEditarFrecuencia = findViewById(R.id.btnEditarFrecuencia);
        Button btnEditarStock = findViewById(R.id.btnEditarStock);
        Button btnVolverEditarAlarma = findViewById(R.id.btnVolverEditarAlarma);

        btnEditarDosis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuEditarAlarmaActivity.this, EditarDosisActivity.class);
                intent.putExtra("alarmaId", alarmaId);
                startActivity(intent);
            }
        });

        btnEditarFrecuencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuEditarAlarmaActivity.this, EditarFrecuenciaActivity.class);
                intent.putExtra("alarmId", alarmaId);
                startActivity(intent);
            }
        });

        btnEditarStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuEditarAlarmaActivity.this, EditarStockActivity.class);
                intent.putExtra("alarmaId", alarmaId);
                startActivity(intent);
            }
        });

        btnVolverEditarAlarma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuEditarAlarmaActivity.this, OpcionesAlarmaActivity.class));
            }
        });
    }
}
