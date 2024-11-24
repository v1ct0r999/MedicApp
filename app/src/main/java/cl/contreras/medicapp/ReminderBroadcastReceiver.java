package cl.contreras.medicapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import androidx.core.app.NotificationCompat;
import android.os.Build;
import android.util.Log;

import cl.contreras.medicapp.db.Alarmas;
import cl.contreras.medicapp.db.DatabaseHelper;

public class ReminderBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // Obtener el nombre del evento y el ID de la alarma desde el Intent
        String alarmaNombre = intent.getStringExtra("nombre_alarma");
        int alarmaId = intent.getIntExtra("alarma_id", -1);  // Asegúrate de que el ID esté presente en el Intent


        // Verificar el stock antes de proceder
        if (alarmaId != -1) {
            DatabaseHelper dbHelper = new DatabaseHelper(context);
            // Obtener la alarma desde la base de datos
            Alarmas alarma = dbHelper.detalleAlarma(alarmaId);

            if (alarma != null) {
                // Obtener el stock actual
                int stockActual = Integer.parseInt(alarma.getStock());

                // Verificar si el stock es 0
                if (stockActual == 0) {
                    // No hacer nada si el stock es 0
                    Log.w("ReminderBroadcastReceiver", "No hay stock disponible para la alarma, no se activará.");
                    return;
                }
            } else {
                // Si no se encontró la alarma, no hacer nada
                Log.e("ReminderBroadcastReceiver", "No se encontró la alarma con ID: " + alarmaId);
                return;
            }
        } else {
            // Si el ID de la alarma no es válido, no hacer nada
            Log.e("ReminderBroadcastReceiver", "ID de alarma no encontrado en el Intent.");
            return;
        }

        // Crear un Intent para abrir la AlertaActivity (alarma visual)
        Intent alertaIntent = new Intent(context, AlertaActivity.class);
        alertaIntent.putExtra("nombre_alarma", alarmaNombre);
        alertaIntent.putExtra("alarma_id", alarmaId);   // Pasar el ID de la alarma a AlertaActivity
        alertaIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(alertaIntent);
    }
}
