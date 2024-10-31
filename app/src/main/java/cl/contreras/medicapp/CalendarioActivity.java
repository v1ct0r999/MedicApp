package cl.contreras.medicapp;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class CalendarioActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private TextView tvCalendario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendario);

        dbHelper = new DatabaseHelper(this);
        tvCalendario = findViewById(R.id.tvCalendario);

        mostrarAlarmas();
    }

    private void mostrarAlarmas() {
        Cursor cursor = dbHelper.getAllAlarmas();
        StringBuilder calendarioTexto = new StringBuilder();
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.getDefault());  // Formato para el día de la semana

        while (cursor.moveToNext()) {
            String nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"));
            int frecuencia = cursor.getInt(cursor.getColumnIndexOrThrow("frecuencia"));
            String horaInicial = cursor.getString(cursor.getColumnIndexOrThrow("hora_inicial"));

            // Encabezado del medicamento
            calendarioTexto.append("Medicamento: ").append(nombre).append("\n");
            calendarioTexto.append("Próximas alarmas:\n");

            // Calcula las alarmas próximas agrupadas por día
            ArrayList<AlarmaDia> alarmasPorDia = calcularProximasAlarmasAgrupadasPorDia(horaInicial, frecuencia);

            String ultimoDia = "";
            for (AlarmaDia alarmaDia : alarmasPorDia) {
                if (!ultimoDia.equals(alarmaDia.diaSemana)) {
                    ultimoDia = alarmaDia.diaSemana;
                    calendarioTexto.append("Día ").append(alarmaDia.diaSemana).append(":\n");
                }
                calendarioTexto.append("   ").append(alarmaDia.hora).append("\n");
            }
            calendarioTexto.append("\n");
        }

        cursor.close();
        tvCalendario.setText(calendarioTexto.toString());
    }

    // Clase auxiliar para manejar alarmas por día y hora
    private class AlarmaDia {
        String diaSemana;
        String hora;

        AlarmaDia(String diaSemana, String hora) {
            this.diaSemana = diaSemana;
            this.hora = hora;
        }
    }

    private ArrayList<AlarmaDia> calcularProximasAlarmasAgrupadasPorDia(String horaInicial, int frecuencia) {
        ArrayList<AlarmaDia> proximasAlarmas = new ArrayList<>();
        SimpleDateFormat sdfHora = new SimpleDateFormat("HH:mm", Locale.getDefault());
        SimpleDateFormat sdfDia = new SimpleDateFormat("EEEE", Locale.getDefault());

        try {
            Date horaInicio = sdfHora.parse(horaInicial);

            for (int i = 0; i < 7 * (24 / frecuencia); i++) { // calcula 7 días de alarmas
                long proximaHora = horaInicio.getTime() + TimeUnit.HOURS.toMillis(i * frecuencia);
                Date proximaFecha = new Date(proximaHora);

                String diaSemana = sdfDia.format(proximaFecha);  // Día de la semana
                String hora = sdfHora.format(proximaFecha);      // Hora de la alarma

                proximasAlarmas.add(new AlarmaDia(diaSemana, hora));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return proximasAlarmas;
    }

}
