package cl.contreras.medicapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import android.view.View;
import java.util.Calendar;

import cl.contreras.medicapp.db.DatabaseHelper;

public class CalendarioActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private TextView tvCalendario;
    private LinearLayout tvNoAlarmas;
    private Button btnVolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendario);

        dbHelper = new DatabaseHelper(this);
        tvCalendario = findViewById(R.id.tvCalendario);
        tvNoAlarmas = findViewById(R.id.tvNoAlarmas); // Referencia al mensaje de "No existen registros"
        btnVolver = findViewById(R.id.btnVolver);


        mostrarAlarmas();

        btnVolver.setOnClickListener(v -> {
            Intent menuIntent = new Intent(CalendarioActivity.this, MainActivity.class);
            startActivity(menuIntent);
            finish();
        });

    }

    private void mostrarAlarmas() {
        Cursor cursor = dbHelper.getAllAlarmas();
        StringBuilder calendarioTexto = new StringBuilder();

        // Crear un SimpleDateFormat con la localización en español
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", new Locale("es", "ES"));

        // Lista para almacenar todas las alarmas de todos los días
        Map<String, List<AlarmaInfo>> alarmasPorDia = new LinkedHashMap<>();

        // Los días de la semana en orden
        String[] diasSemana = {"lunes", "martes", "miércoles", "jueves", "viernes", "sábado", "domingo"};

        if (cursor.getCount() == 0) {
            // Si no hay registros, muestra el mensaje de "No existen registros"
            tvNoAlarmas.setVisibility(View.VISIBLE);
            tvCalendario.setVisibility(View.GONE);
        } else {
            // Si hay registros, oculta el mensaje y muestra las alarmas
            tvNoAlarmas.setVisibility(View.GONE);
            tvCalendario.setVisibility(View.VISIBLE);

            // Agrupar las alarmas por día
            while (cursor.moveToNext()) {
                String nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"));
                int frecuencia = cursor.getInt(cursor.getColumnIndexOrThrow("frecuencia"));
                String horaInicial = cursor.getString(cursor.getColumnIndexOrThrow("hora_inicial"));

                // Calcula las alarmas próximas agrupadas por día
                ArrayList<AlarmaInfo> alarmasPorDiaActual = calcularProximasAlarmasAgrupadasPorDia(horaInicial, frecuencia);

                // Agregar el nombre de la alarma a cada alarma y agrupar por día
                for (AlarmaInfo alarma : alarmasPorDiaActual) {
                    alarma.nombre = nombre; // Asignar el nombre del medicamento

                    // Obtener el día de la semana en formato completo (lunes, martes, etc.)
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(alarma.timestamp);
                    String diaSemana = new SimpleDateFormat("EEEE", new Locale("es", "ES")).format(calendar.getTime()).toLowerCase();

                    // Agrupar las alarmas por día de la semana
                    if (!alarmasPorDia.containsKey(diaSemana)) {
                        alarmasPorDia.put(diaSemana, new ArrayList<>());
                    }
                    alarmasPorDia.get(diaSemana).add(alarma); // Añadir alarma a la lista del día correspondiente
                }
            }

            // Ordenar las alarmas por día (lunes a domingo)
            for (String dia : diasSemana) {
                if (alarmasPorDia.containsKey(dia)) {
                    List<AlarmaInfo> alarmasDelDia = alarmasPorDia.get(dia);

                    // Ordenar las alarmas dentro de cada día por timestamp (hora)
                    Collections.sort(alarmasDelDia, new Comparator<AlarmaInfo>() {
                        @Override
                        public int compare(AlarmaInfo a1, AlarmaInfo a2) {
                            return Long.compare(a1.timestamp, a2.timestamp); // Ordenar por timestamp
                        }
                    });

                    // Agregar las alarmas del día al StringBuilder
                    calendarioTexto.append(dia.substring(0, 1).toUpperCase() + dia.substring(1) + ":\n"); // Capitalizar el primer letra del día
                    for (AlarmaInfo alarma : alarmasDelDia) {
                        calendarioTexto.append("   " + alarma.nombre + " - " + alarma.hora + "\n");
                    }
                    calendarioTexto.append("\n");
                }
            }

            // Mostrar el calendario con todas las alarmas ordenadas
            tvCalendario.setText(calendarioTexto.toString());
        }

        cursor.close();
    }




    // Clase auxiliar para manejar alarmas por día y hora
    private class AlarmaInfo {
        String nombre;
        String hora;
        long timestamp;  // Usaremos el timestamp (milisegundos desde el 1 de enero de 1970) para ordenar

        AlarmaInfo(String nombre, String hora, long timestamp) {
            this.nombre = nombre;
            this.hora = hora;
            this.timestamp = timestamp;
        }
    }


    private ArrayList<AlarmaInfo> calcularProximasAlarmasAgrupadasPorDia(String horaInicial, int frecuencia) {
        ArrayList<AlarmaInfo> proximasAlarmas = new ArrayList<>();
        SimpleDateFormat sdfHora = new SimpleDateFormat("HH:mm", Locale.getDefault());
        SimpleDateFormat sdfDia = new SimpleDateFormat("EEEE", new Locale("es", "ES"));
        Calendar calendarioActual = Calendar.getInstance();

        try {
            // Parsear la hora inicial con la fecha actual
            Date horaInicio = sdfHora.parse(horaInicial);
            if (horaInicio != null) {
                calendarioActual.set(Calendar.HOUR_OF_DAY, horaInicio.getHours());
                calendarioActual.set(Calendar.MINUTE, horaInicio.getMinutes());
                calendarioActual.set(Calendar.SECOND, 0);
            }

            // Calcular las próximas alarmas
            for (int i = 0; i < 7 * (24 / frecuencia); i++) { // calcula 7 días de alarmas
                long proximaHora = calendarioActual.getTimeInMillis() + TimeUnit.HOURS.toMillis(i * frecuencia);
                Date proximaFecha = new Date(proximaHora);
                String hora = sdfHora.format(proximaFecha);

                proximasAlarmas.add(new AlarmaInfo("Alarma", hora, proximaHora)); // "Alarma" es el nombre temporal, se modificará más adelante
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return proximasAlarmas;
    }

}
