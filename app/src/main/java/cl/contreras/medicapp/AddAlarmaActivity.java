package cl.contreras.medicapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import java.util.Calendar;
import androidx.appcompat.app.AppCompatActivity;

import cl.contreras.medicapp.db.DatabaseHelper;

public class AddAlarmaActivity extends AppCompatActivity {

    DatabaseHelper dbHelper;
    EditText editTextNombre, editTextDosis, editTextStock, editTextFrecuencia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarma);

        dbHelper = new DatabaseHelper(this);

        editTextNombre = findViewById(R.id.editTextNombre);
        editTextDosis = findViewById(R.id.editTextDosis);
        editTextStock = findViewById(R.id.editTextStock);
        editTextFrecuencia = findViewById(R.id.editTextFrecuencia);
        Button btnSave = findViewById(R.id.btnSave);
        Button btnCancel = findViewById(R.id.btnCancel);

        btnSave.setOnClickListener(v -> {
            String nombre = editTextNombre.getText().toString();
            String dosisText = editTextDosis.getText().toString();
            String stockText = editTextStock.getText().toString();
            String frecuenciaText = editTextFrecuencia.getText().toString();

            if (nombre.isEmpty() || dosisText.isEmpty() || stockText.isEmpty() || frecuenciaText.isEmpty()) {
                Toast.makeText(AddAlarmaActivity.this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show();
            } else {
                int dosis = Integer.parseInt(dosisText);
                int stock = Integer.parseInt(stockText);
                int frecuencia = Integer.parseInt(frecuenciaText);

                boolean isInserted = dbHelper.addAlarma(nombre, dosis, stock, frecuencia);
                if (isInserted) {
                    int alarmaId = dbHelper.getLastInsertedId();
                    scheduleReminder(nombre, frecuencia, alarmaId);
                    Toast.makeText(AddAlarmaActivity.this, "Alarma guardada", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AddAlarmaActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(AddAlarmaActivity.this, "Error al guardar", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnCancel.setOnClickListener(v -> {
            Intent intent = new Intent(AddAlarmaActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    // Método para programar el recordatorio
    private void scheduleReminder(String nombre, int frecuencia, int alarmaId) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(AddAlarmaActivity.this, ReminderBroadcastReceiver.class);
        intent.putExtra("nombre_alarma", nombre);
        intent.putExtra("alarma_id", alarmaId);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                alarmaId, // Cambiado para usar el ID único de la alarma
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );


        // Programar la alarma para que se active cada X minutos
        long triggerTime = Calendar.getInstance().getTimeInMillis() + frecuencia * 60000; // 60000 milisegundos en un minuto

        // Configurar una alarma repetitiva
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, triggerTime, frecuencia * 60000, pendingIntent);
    }

}