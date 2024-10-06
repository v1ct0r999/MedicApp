package cl.contreras.medicapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import androidx.core.app.NotificationCompat;
import android.os.Build;

public class ReminderBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // Obtener el nombre del evento desde el Intent
        String alarmaNombre = intent.getStringExtra("nombre_alarma");

        // Crear un Intent para abrir la AlertaActivity (alarma visual)
        Intent alertaIntent = new Intent(context, AlertaActivity.class);
        alertaIntent.putExtra("nombre_alarma", alarmaNombre);
        alertaIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(alertaIntent);

    }
}
