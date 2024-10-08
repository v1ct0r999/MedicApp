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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opciones_alarma);

        // Obtener el nombre de la alarma desde el Intent
        String alarmaNombre = getIntent().getStringExtra("alarma_nombre");

        // Referencias a los elementos de la vista
        alarmaNombreTextView = findViewById(R.id.alarmaNombreTextView);
        btnVerDetalles = findViewById(R.id.btnVerDetalles);
        btnEditar = findViewById(R.id.btnEditar);
        btnEliminar = findViewById(R.id.btnEliminar);
        BotonAtrasAlarma = findViewById(R.id.BotonAtrasAlarma);

        // Mostrar el nombre de la alarma en el TextView
        if (alarmaNombre != null) {
            alarmaNombreTextView.setText(alarmaNombre);
        }

        // Aquí después agregaremos la lógica para cada botón
        btnVerDetalles.setOnClickListener(v -> {
            // ir a la pantalla para ver detalles
        });

        btnEditar.setOnClickListener(v -> {
            // ir a la pantalla para editar la alarma
        });

        btnEliminar.setOnClickListener(v -> {
            // aun nose si ir sa pantalla o hacer una alerta para eliminar la alarma
            new AlertDialog.Builder(OpcionesAlarmaActivity.this)
                    .setTitle("Eliminar alarma")
                    .setMessage("¿Estás seguro de que deseas eliminar esta alarma?")
                    .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            eliminarAlarma(alarmaNombre);
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
    private void eliminarAlarma(String alarmaNombre) {
        DatabaseHelper databaseHelper = new DatabaseHelper(this);

        // Cancela la alarma en el sistema antes de eliminarla de la base de datos
        cancelarAlarma(alarmaNombre);

        boolean result = databaseHelper.eliminarAlarma(alarmaNombre);

        if (result) {
            Toast.makeText(this, "Alarma eliminada con éxito", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error al eliminar la alarma", Toast.LENGTH_SHORT).show();
        }

        Intent intent = new Intent(OpcionesAlarmaActivity.this, MainActivity.class);
        startActivity(intent);
        finish(); // Cierra esta actividad
    }

    // Método para cancelar la alarma en AlarmManager
    private void cancelarAlarma(String alarmaNombre) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(OpcionesAlarmaActivity.this, ReminderBroadcastReceiver.class);
        intent.putExtra("nombre_alarma", alarmaNombre);

        // El mismo PendingIntent con el que se programó la alarma
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Cancelar la alarma
        alarmManager.cancel(pendingIntent);
    }
}