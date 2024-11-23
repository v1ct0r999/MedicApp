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
        // Obtener el nombre del evento y el ID de la alarma desde el Intent
        String alarmaNombre = intent.getStringExtra("nombre_alarma");
        int alarmaId = intent.getIntExtra("alarma_id", -1);  // Asegúrate de que el ID esté presente en el Intent

        // Crear un Intent para abrir la AlertaActivity (alarma visual)
        Intent alertaIntent = new Intent(context, AlertaActivity.class);
        alertaIntent.putExtra("nombre_alarma", alarmaNombre);
        alertaIntent.putExtra("alarma_id", alarmaId);   // Pasar el ID de la alarma a AlertaActivity
        alertaIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(alertaIntent);
    }
}
