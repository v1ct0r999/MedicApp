package cl.contreras.medicapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import cl.contreras.medicapp.db.DatabaseHelper;

public class EliminarContactoActivity extends AppCompatActivity {

    private TextView nombrecontactotextview;
    private TextView telefonocontactotextview;
    private DatabaseHelper dbHelper;
    private int contactoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_eliminar_contacto);

        dbHelper = new DatabaseHelper(this);
        contactoId = getIntent().getIntExtra("contactos_id", -1); // Recibir el ID del contacto

        // Verifica que el ID no sea -1
        if (contactoId == -1) {
            Toast.makeText(this, "ID de contacto inválido", Toast.LENGTH_SHORT).show();
            finish(); // Cierra la actividad si el ID es inválido
            return;
        }

        nombrecontactotextview = findViewById(R.id.nombrecontacto);
        telefonocontactotextview = findViewById(R.id.telefonocontacto);

        Button botonVolverEliminarContacto = findViewById(R.id.btnVolverEliminarContacto);
        Button btneliminarcontacto = findViewById(R.id.btnEliminarContacto);

        mostrarUltimoContacto();

        // Volver al Menu Contacto
        botonVolverEliminarContacto.setOnClickListener(v -> {
            startActivity(new Intent(EliminarContactoActivity.this, MenuContactoActivity.class));
            finish();
        });

        btneliminarcontacto.setOnClickListener(v -> {
            new AlertDialog.Builder(EliminarContactoActivity.this)
                    .setTitle("Eliminar Contacto")
                    .setMessage("¿Estás seguro de que deseas eliminar este contacto?")
                    .setPositiveButton("Sí", (dialogInterface, i) -> eliminarContacto(contactoId))
                    .setNegativeButton("No", null)
                    .show();
        });
    }

    private void eliminarContacto(int contactoId) {
        Log.d("EliminarContacto", "ID del contacto a eliminar: " + contactoId);
        boolean result = dbHelper.eliminarContacto(contactoId);

        if (result) {
            Toast.makeText(this, "Contacto eliminado con éxito", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error al eliminar el contacto", Toast.LENGTH_SHORT).show();
        }
        Intent intent = new Intent(EliminarContactoActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);// Cierra esta actividad
        finish();
    }


    private void mostrarUltimoContacto() {
        Cursor cursor = dbHelper.getAllContactos();
        if (cursor != null && cursor.moveToFirst()) {
            // Obtener el nombre y teléfono del primer contacto (el más reciente)
            String nombre = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NOMBRE_CONTACTO));
            String telefono = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TELEFONO));
            nombrecontactotextview.setText(nombre);
            telefonocontactotextview.setText(telefono);
            cursor.close();
        } else {
            Toast.makeText(this, "No hay contactos guardados", Toast.LENGTH_SHORT).show();
        }
    }

}