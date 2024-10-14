package cl.contreras.medicapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MenuEditarAlarmaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu_editar_alarma);

        Button btnEditarDosis = (Button) findViewById(R.id.btnEditarDosis);
        Button btnEditarFrecuencia = (Button) findViewById(R.id.btnEditarFrecuencia);
        Button btnEditarStock = (Button) findViewById(R.id.btnEditarStock);
        Button btnVolverEditarAlarma = (Button) findViewById(R.id.btnVolverEditarAlarma);


        // Conectar el layout EditarContacto con EditarNombreContacto
        btnEditarDosis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuEditarAlarmaActivity.this, EditarDosisActivity.class));
            }
        });

        // Conectar el layout EditarContacto con EditarNumeroContacto
        btnEditarFrecuencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuEditarAlarmaActivity.this, EditarFrecuenciaActivity.class));
            }
        });

        // Volver al MenuContacto
        btnEditarStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuEditarAlarmaActivity.this, EditarStockActivity.class));
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