package cl.contreras.medicapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.media.MediaPlayer;
import android.util.Log;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import android.view.WindowManager;


import cl.contreras.medicapp.db.Alarmas;
import cl.contreras.medicapp.db.DatabaseHelper;


public class AlertaActivity extends AppCompatActivity {

    private TextView textViewTimer;
    private Handler handler = new Handler();
    private int secondsPassed = 0;
    private Runnable timerRunnable;
    private static final int SNOOZE_TIME = 15 * 60 * 1000; // 15 minutos en milisegundos
    private MediaPlayer mediaPlayer; // Reproductor de sonido
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_alerta);

        crearCanalDeNotificacion();

        TextView RecordatorioNombre = findViewById(R.id.RecordatorioNombre);

        sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);

        // Obtener el nombre guardado en SharedPreferences
        String name = sharedPreferences.getString("USER_NAME", "Usuario");

        // Mostrar el saludo con el nombre
        RecordatorioNombre.setText("!!Hola usuario " + name + " Es hora de su medicamento¡¡");

        // Obtener el nombre del evento desde el Intent
        Intent intent = getIntent();
        String alarmaNombre = intent.getStringExtra("nombre_alarma");
        int alarmaId = getIntent().getIntExtra("alarma_id", -1);

        // Mostrar el nombre del evento en la alarma
        TextView textViewAlarma = findViewById(R.id.textViewAlarma);
        textViewAlarma.setText(alarmaNombre);

        // Inicializar el temporizador
        textViewTimer = findViewById(R.id.textViewTimer);
        startTimer();

        mediaPlayer = MediaPlayer.create(this, R.raw.alarma);
        mediaPlayer.setLooping(true);  // Activar el bucle para el sonido
        mediaPlayer.start();

        // Botón para detener la alarma
        Button btnStopAlarm = findViewById(R.id.btnStopAlarm);
        btnStopAlarm.setOnClickListener(v -> {
            if (alarmaId != -1) { // Usar la variable declarada anteriormente
                reducirStock(alarmaId); // Disminuir el stock
            } else {
                Log.e("AlertaActivity", "ID de alarma no encontrado en el Intent.");
            }
            stopAlarmSound(); // Detener el sonido
            finish();
        });



        // Botón para posponer la alarma 15 minutos
        Button btnSnooze = findViewById(R.id.btnSnooze);
        btnSnooze.setOnClickListener(v -> {
            stopAlarmSound();
            snoozeAlarm();
            finish(); // Cerrar la alarma actual
        });
    }

    private void startTimer() {
        timerRunnable = new Runnable() {
            @Override
            public void run() {
                if (secondsPassed < 2 * 60) { // Limitar a 2 minutos
                    secondsPassed++;
                    int minutes = secondsPassed / 60;
                    int seconds = secondsPassed % 60;
                    textViewTimer.setText(String.format("%02d:%02d", minutes, seconds));
                    handler.postDelayed(this, 1000); // Actualizar cada segundo
                } else {
                    finish(); // Detener la alarma después de 2 minutos
                }
            }
        };
        handler.post(timerRunnable);
    }

    private void snoozeAlarm() {
        Intent snoozeIntent = new Intent(this, ReminderBroadcastReceiver.class);
        snoozeIntent.putExtra("nombre_alarma", getIntent().getStringExtra("nombre_alarma"));

        // A partir de Android 12, se requiere especificar el FLAG_IMMUTABLE para PendingIntent
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                0,
                snoozeIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        long triggerAtMillis = System.currentTimeMillis() + SNOOZE_TIME;
        
        // Solo verifica el permiso si estamos en Android 12 (API 31) o superior
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // Utilizar setExactAndAllowWhileIdle para mayor compatibilidad
            if (alarmManager != null && alarmManager.canScheduleExactAlarms()) {
                long trigegersArMillis = System.currentTimeMillis() + SNOOZE_TIME;
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
            } else {
                // Manejar el caso en que no se puede programar alarmas exactas
                // Por ejemplo, informar al usuario que debe habilitar el permiso
            }
        } else {
            // Para versiones anteriores a Android 12, puedes programar alarmas normalmente
            long trigegersArMillis = System.currentTimeMillis() + SNOOZE_TIME;
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
        }
    }

    private void reducirStock(int alarmaId) {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        Alarmas alarma = dbHelper.detalleAlarma(alarmaId);

        if (alarma != null) {
            int dosis = Integer.parseInt(alarma.getDosis());
            int stockActual = Integer.parseInt(alarma.getStock());

            if (stockActual >= dosis) {
                int nuevoStock = stockActual - dosis;
                boolean actualizado = dbHelper.editarAlarmaStock(alarmaId, nuevoStock);

                if (actualizado) {
                    Log.d("AlertaActivity", "Stock actualizado a: " + nuevoStock);
                    verificarStockBajo(this, alarmaId); // Verifica si el stock es bajo y envía una notificación
                } else {
                    Log.e("AlertaActivity", "Error al actualizar el stock.");
                }
            } else {
                Log.w("AlertaActivity", "No hay suficiente stock para esta dosis.");
            }
        } else {
            Log.e("AlertaActivity", "No se encontró la alarma con ID: " + alarmaId);
        }
    }


    private void stopAlarmSound() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(timerRunnable); // Detener el temporizador cuando se cierre la actividad
        stopAlarmSound();
    }

    private void verificarStockBajo(Context context, int alarmaId) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        Alarmas alarma = dbHelper.detalleAlarma(alarmaId);

        if (alarma != null) {
            int stockActual = Integer.parseInt(alarma.getStock());
            if (stockActual <= 5) {
                enviarNotificacion(context, alarmaId, alarma.getNombre(), stockActual);
            }
        } else {
            Log.e("AlertaActivity", "No se encontró la alarma con ID: " + alarmaId);
        }
    }

    private void enviarNotificacion(Context context, int alarmaId, String nombreAlarma, int stockActual) {
        Intent intent = new Intent(context, EditarStockActivity.class);
        intent.putExtra("alarma_id", alarmaId);
        intent.putExtra("nombre_alarma", nombreAlarma);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                alarmaId,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // Crear la notificación
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "MedicAppChannel")
                .setSmallIcon(R.drawable.baseline_notifications_24)
                .setContentTitle("Stock bajo")
                .setContentText("El stock de " + nombreAlarma + " es de " + stockActual + ". Actualízalo.")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        // Mostrar la notificación
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(alarmaId, builder.build());
    }

    private void crearCanalDeNotificacion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "MedicAppChannel";
            String description = "Canal para notificaciones de stock bajo";
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel channel = new NotificationChannel("MedicAppChannel", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }


}
