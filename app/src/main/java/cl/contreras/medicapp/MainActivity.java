package cl.contreras.medicapp;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import cl.contreras.medicapp.db.DatabaseHelper;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper dbHelper;
    LinearLayout alarmaLayout;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
            }
        }

        TextView Saludo = findViewById(R.id.Saludo);

        sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);

        // Obtener el nombre guardado en SharedPreferences
        String name = sharedPreferences.getString("USER_NAME", "Usuario");

        // Mostrar el saludo con el nombre
        Saludo.setText("Buenos dias " + name + " :)");

        dbHelper = new DatabaseHelper(this);
        alarmaLayout = findViewById(R.id.alarmaLayout);
        Button btnAddAlarma = findViewById(R.id.btnAddAlarma);

        btnAddAlarma.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddAlarmaActivity.class);
            startActivity(intent);
        });

        loadAlarmas();

        ImageButton botonCalendario = (ImageButton) findViewById(R.id.calendario);
        ImageButton botonContacto = (ImageButton) findViewById(R.id.agregarcontacto);
        ImageButton botonemergerncia = (ImageButton) findViewById(R.id.botonemergencia);


        botonCalendario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CalendarioActivity.class));
            }
        });
        botonContacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MenuContactoActivity.class));
            }
        });
        botonemergerncia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Supongamos que tienes el ID del contacto que quieres marcar
                int contactoId = 1; // Cambia esto según tu lógica

                // Obtener el número de teléfono de la base de datos
                String telefono = dbHelper.getTelefonoPorId(contactoId);

                if (telefono != null) {
                    Intent accionLlamar = new Intent(Intent.ACTION_DIAL);
                    accionLlamar.setData(Uri.parse("tel:" + telefono)); // Prepara el número para marcar
                    startActivity(accionLlamar); // Inicia la actividad
                } else {
                    // Manejar el caso en que no se encuentra el número
                    Toast.makeText(view.getContext(), "Número no encontrado", Toast.LENGTH_SHORT).show();
                }
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
                alarmaButton.setText(nombre + "\n\nVer detalles");
                alarmaButton.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        400
                ));
                alarmaButton.setPadding(10, 20, 80, 10);
                alarmaButton.setTextSize(25);
                alarmaButton.setBackgroundColor(Color.WHITE);

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
}


