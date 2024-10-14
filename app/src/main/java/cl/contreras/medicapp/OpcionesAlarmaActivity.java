package cl.contreras.medicapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlarmManager;
import android.app.PendingIntent;

import androidx.appcompat.app.AppCompatActivity;

public class OpcionesAlarmaActivity extends AppCompatActivity {

    private TextView alarmaNombreTextView;
    private Button btnVerDetalles, btnEditar, btnEliminar, BotonAtrasAlarma;
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
        btnVerDetalles = findViewById(R.id.btnVerDetalles);
        btnEditar = findViewById(R.id.btnEditar);
        btnEliminar = findViewById(R.id.btnEliminar);
        BotonAtrasAlarma = findViewById(R.id.BotonAtrasAlarma);

        // Mostrar el nombre de la alarma en el TextView si está disponible
        if (alarmaNombre != null) {
            alarmaNombreTextView.setText(alarmaNombre);
        }

        // Lógica para cada botón
        btnVerDetalles.setOnClickListener(v -> {
            // Ir a la pantalla para ver detalles
        });

        btnEditar.setOnClickListener(v -> {
            // Ir a la pantalla para editar la alarma
        });

        btnEliminar.setOnClickListener(v -> {
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
        intent.putExtra("alarma_id", alarmaId); // Se envía el ID de la alarma

        // Crear el PendingIntent con el mismo requestCode que se utilizó para programar la alarma
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // Cancelar la alarma
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
    }
}
