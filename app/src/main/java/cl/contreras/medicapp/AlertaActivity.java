package cl.contreras.medicapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.media.MediaPlayer;

public class AlertaActivity extends AppCompatActivity {

    private TextView textViewTimer;
    private Handler handler = new Handler();
    private int secondsPassed = 0;
    private Runnable timerRunnable;
    private static final int SNOOZE_TIME = 15 * 60 * 1000; // 15 minutos en milisegundos
    private MediaPlayer mediaPlayer; // Reproductor de sonido

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alerta);

        // Obtener el nombre del evento desde el Intent
        Intent intent = getIntent();
        String alarmaNombre = intent.getStringExtra("nombre_alarma");

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
                if (secondsPassed < 3 * 60) { // Limitar a 3 minutos
                    secondsPassed++;
                    int minutes = secondsPassed / 60;
                    int seconds = secondsPassed % 60;
                    textViewTimer.setText(String.format("%02d:%02d", minutes, seconds));
                    handler.postDelayed(this, 1000); // Actualizar cada segundo
                } else {
                    finish(); // Detener la alarma después de 3 minutos
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

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        long triggerAtMillis = System.currentTimeMillis() + SNOOZE_TIME;

        // Utilizar setExactAndAllowWhileIdle para mayor compatibilidad
        if (alarmManager != null) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
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
}
