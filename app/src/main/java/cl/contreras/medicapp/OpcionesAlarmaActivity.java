package cl.contreras.medicapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlarmManager;
import android.app.PendingIntent;

import androidx.appcompat.app.AppCompatActivity;

import cl.contreras.medicapp.db.DatabaseHelper;

public class OpcionesAlarmaActivity extends AppCompatActivity {

    private TextView alarmaNombreTextView;
    private Button btnDetallesAlarma, btnEditarAlarma, btnEliminarAlarma, BotonAtrasAlarma;
    private DatabaseHelper dbHelper;
    private int alarmaId; // ID de la alarma que se recibirá desde el Intent

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opciones_alarma);

        dbHelper = new DatabaseHelper(this);

        // Obtener el ID y nombre de la alarma desde el Intent
        alarmaId = getIntent().getIntExtra("alarma_id", -1); // Recibir el ID de la alarma
        String alarmaNombre = getIntent().getStringExtra("alarma_nombre"); // Nombre opcional

        // Referencias a los elementos de la vista
        alarmaNombreTextView = findViewById(R.id.alarmaNombreTextView);
        btnDetallesAlarma = findViewById(R.id.btnDetallesAlarma);
        btnEditarAlarma = findViewById(R.id.btnEditarAlarma);
        btnEliminarAlarma = findViewById(R.id.btnEliminarAlarma);
        BotonAtrasAlarma = findViewById(R.id.BotonAtrasAlarma);

        // Mostrar el nombre de la alarma en el TextView si está disponible
        if (alarmaNombre != null) {
            alarmaNombreTextView.setText(alarmaNombre);
        }

        // Lógica para cada botón
        // Ir a la pantalla para ver detalles
        btnDetallesAlarma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (alarmaId != -1) {  // Verifica que el ID de la alarma sea válido
                    Intent intent = new Intent(OpcionesAlarmaActivity.this, DetallesAlarmasActivity.class);
                    intent.putExtra("ID", alarmaId); // Pasa el id aquí
                    startActivity(intent); // Luego inicia la nueva actividad
                }
            }
        });

        btnEditarAlarma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OpcionesAlarmaActivity.this, MenuEditarAlarmaActivity.class));
            }
        });

        btnEliminarAlarma.setOnClickListener(v -> {
            new AlertDialog.Builder(OpcionesAlarmaActivity.this)
                    .setTitle("Eliminar alarma")
                    .setMessage("¿Estás seguro de que deseas eliminar esta alarma?")
                    .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (alarmaId != -1) {  // Verifica que el ID de la alarma sea válido
                                eliminarAlarma(alarmaId);
                            }
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        });

        BotonAtrasAlarma.setOnClickListener(v -> {
            Intent intent = new Intent(OpcionesAlarmaActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void eliminarAlarma(int alarmaId) {
        // Cancela la alarma en el sistema antes de eliminarla de la base de datos
        cancelarAlarma(alarmaId);

        boolean result = dbHelper.eliminarAlarma(alarmaId);

        if (result) {
            Toast.makeText(this, "Alarma eliminada con éxito", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error al eliminar la alarma", Toast.LENGTH_SHORT).show();
        }

        Intent intent = new Intent(OpcionesAlarmaActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();  // Cierra esta actividad
    }

    // Método para cancelar la alarma en AlarmManager usando el ID de la alarma
    private void cancelarAlarma(int alarmaId) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(OpcionesAlarmaActivity.this, ReminderBroadcastReceiver.class);
        intent.putExtra("nombre_alarma", ""); // Agrega el mismo extra que en el scheduleReminder si es necesario

        // Usar alarmaId como requestCode
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                alarmaId, // Asegúrate de usar alarmaId aquí también
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // Cancelar la alarma
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
    }

}
