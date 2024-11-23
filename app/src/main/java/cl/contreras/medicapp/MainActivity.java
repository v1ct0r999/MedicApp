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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.util.Calendar;

import cl.contreras.medicapp.db.DatabaseHelper;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper dbHelper;
    private SharedPreferences sharedPreferences;
    private LinearLayout tvNoAlarmas;
    private LinearLayout alarmaLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView Saludo = findViewById(R.id.Saludo);

        sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);

        // Obtener el nombre guardado en SharedPreferences
        String name = sharedPreferences.getString("USER_NAME", "Usuario");


        // Obtener la hora actual usando Calendar para compatibilidad con API 24
        Calendar calendar = Calendar.getInstance();
        int horaActual = calendar.get(Calendar.HOUR_OF_DAY);  // 24-hour format

        // Definir el saludo basado en la hora actual
        String saludo = "";
        if (horaActual < 12) {
            saludo = "Buenos días " + name + " :)";
        } else if (horaActual < 18) {
            saludo = "Buenas tardes " + name + " :)";
        } else {
            saludo = "Buenas noches " + name + " :)";
        }

        // Mostrar el saludo en la interfaz
        Saludo.setText(saludo);

        dbHelper = new DatabaseHelper(this);
        alarmaLayout = findViewById(R.id.alarmaLayout);
        tvNoAlarmas = findViewById(R.id.tvNoAlarmas);
        Button btnAddAlarma = findViewById(R.id.btnAddAlarma);

        btnAddAlarma.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddAlarmaActivity.class);
            startActivity(intent);
            finish();
        });

        loadAlarmas();

        ImageButton botonCalendario = (ImageButton) findViewById(R.id.calendario);
        ImageButton botonContacto = (ImageButton) findViewById(R.id.agregarcontacto);
        ImageButton botonemergerncia = (ImageButton) findViewById(R.id.botonemergencia);

        ImageButton btnCerrarSesion = (ImageButton) findViewById(R.id.btncerrarsesion);

        btnCerrarSesion.setOnClickListener(v -> {
            // Eliminar el nombre de usuario de SharedPreferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove("USER_NAME");
            editor.apply();

            // Mostrar mensaje de confirmación
            Toast.makeText(MainActivity.this, "Sesión cerrada", Toast.LENGTH_SHORT).show();

            // Redirigir al usuario a la pantalla de inicio de sesión (Splash)
            Intent intent = new Intent(MainActivity.this, Splash.class);
            startActivity(intent);
            finish();
        });


        botonCalendario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CalendarioActivity.class));
                finish();
            }
        });
        botonContacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MenuContactoActivity.class));
                finish();
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
            tvNoAlarmas.setVisibility(View.GONE);
            alarmaLayout.setVisibility(View.VISIBLE);
            do {
                int id = cursor.getInt(0);  // Obtener ID de la alarma
                String nombre = cursor.getString(1);

                // Crear un nuevo botón grande
                Button alarmaButton = new Button(this);
                alarmaButton.setText(nombre + "\n\nVer detalles");
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        400
                );
                params.setMargins(0, 20, 0, 20); // Margen superior e inferior de 20px
                alarmaButton.setLayoutParams(params);
                alarmaButton.setPadding(10, 20, 80, 20);
                alarmaButton.setTextSize(25);
                alarmaButton.setBackgroundColor(Color.WHITE);
                alarmaButton.setTextColor(Color.BLACK);

                alarmaButton.setOnClickListener(v -> {
                    Intent intent = new Intent(MainActivity.this, OpcionesAlarmaActivity.class);
                    intent.putExtra("alarma_id", id);  // Enviar el ID de la alarma
                    intent.putExtra("alarma_nombre", nombre);
                    startActivity(intent);
                });

                // Agregar el botón al layout dinámico
                alarmaLayout.addView(alarmaButton);

            } while (cursor.moveToNext());
        } else {
            tvNoAlarmas.setVisibility(View.VISIBLE);
            alarmaLayout.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAlarmas(); // Recargar la lista cuando regrese a la actividad principal
    }


}
