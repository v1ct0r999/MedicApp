package cl.contreras.medicapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper dbHelper;
    LinearLayout alarmaLayout;
    Button buttonEditDosis, buttonEditFrecuencia, buttonEditStock;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
            }
        }

        dbHelper = new DatabaseHelper(this);
        alarmaLayout = findViewById(R.id.alarmaLayout);
        Button btnAddAlarma = findViewById(R.id.btnAddAlarma);

        btnAddAlarma.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddAlarmaActivity.class);
            startActivity(intent);
        });

        // Enlazar los botones de edición
        buttonEditDosis = findViewById(R.id.buttonAceptarDosis);
        buttonEditFrecuencia = findViewById(R.id.buttonAceptarFrecuencia);
        buttonEditStock = findViewById(R.id.buttonAceptarStock);


        // Navegar a EditDosisActivity al hacer clic en el botón de Editar Dosis
        buttonEditDosis.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, EditDosisActivity.class);
            startActivity(intent);
        });

        // Navegar a EditFrecuenciaActivity al hacer clic en el botón de Editar Frecuencia
        buttonEditFrecuencia.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, activity_edit_frecuencia.class);
            startActivity(intent);
        });

        // Navegar a EditStockActivity al hacer clic en el botón de Editar Stock
        buttonEditStock.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, activity_edit_stock.class);
            startActivity(intent);
        });

        loadAlarmas(); // Cargar las alarmas desde la base de datos

        ImageButton botonContacto = (ImageButton) findViewById(R.id.agregarcontacto);

        botonContacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MenuContactoActivity.class));
            }
        });
    }

    private void loadAlarmas() {
        alarmaLayout.removeAllViews(); // Limpiar el layout antes de cargar los datos nuevos
        Cursor cursor = dbHelper.getAllAlarmas();

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);  // Obtener ID de la alarma
                String nombre = cursor.getString(1);

                // Crear un nuevo botón grande
                Button alarmaButton = new Button(this);
                alarmaButton.setText("Nombre Alarma\n" + nombre + "\n\nVer detalles");
                alarmaButton.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        400
                ));
                alarmaButton.setPadding(10, 20, 80, 10);
                alarmaButton.setTextSize(25);

                alarmaButton.setOnClickListener(v -> {
                    Intent intent = new Intent(MainActivity.this, OpcionesAlarmaActivity.class);
                    intent.putExtra("alarma_id", id);  // Enviar el ID de la alarma
                    startActivity(intent);
                });

                // Agregar el botón al layout dinámico
                alarmaLayout.addView(alarmaButton);
            } while (cursor.moveToNext());
        } else {
            Toast.makeText(this, "No hay alarmas disponibles", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAlarmas(); // Recargar la lista cuando regrese a la actividad principal
    }

    // Gestión del resultado del permiso
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permisos de notificación concedidos", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permisos de notificación denegados", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
}
