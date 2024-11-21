package cl.contreras.medicapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import cl.contreras.medicapp.db.DatabaseHelper;

public class MenuEditarContactoActivity extends AppCompatActivity {

    private TextView nombrecontactoTextView; // Para mostrar el nombre
    private TextView telefonocontactoTextView; // Para mostrar el teléfono
    private DatabaseHelper dbHelper;
    private int contactoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_editar_contacto);

        // Inicializar el DatabaseHelper
        dbHelper = new DatabaseHelper(this);
        contactoId = getIntent().getIntExtra("contactos_id", -1);

        // Inicializar los TextViews
        nombrecontactoTextView = findViewById(R.id.nombrecontacto);
        telefonocontactoTextView = findViewById(R.id.telefonocontacto);

        Button botonEditarNombreContacto = findViewById(R.id.btnEditarNombreContacto);
        Button botonEditarFonoContacto = findViewById(R.id.btnEditarFonoContacto);
        Button botonVolverEditarContacto = findViewById(R.id.btnVolverEditarContacto);

        // Mostrar el último contacto guardado
        mostrarUltimoContacto();

        botonEditarNombreContacto.setOnClickListener(v -> {
            Intent intent = new Intent(MenuEditarContactoActivity.this, EditarNombreContactoActivity.class);
            intent.putExtra("contactos_id", contactoId);
            startActivity(intent);
        });

        botonEditarFonoContacto.setOnClickListener(v -> {
            Intent intent = new Intent(MenuEditarContactoActivity.this, EditarNumeroContactoActivity.class);
            intent.putExtra("contactos_id", contactoId);
            startActivity(intent);
        });

        // Volver al MenuContacto
        botonVolverEditarContacto.setOnClickListener(v -> {
            Intent menuIntent = new Intent(MenuEditarContactoActivity.this, MenuContactoActivity.class);
            startActivity(menuIntent);
            finish();
        });
    }

    private void mostrarUltimoContacto() {
        Cursor cursor = dbHelper.getAllContactos();
        if (cursor != null && cursor.moveToFirst()) {
            // Obtener el nombre y teléfono del primer contacto (el más reciente)
            String nombre = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NOMBRE_CONTACTO));
            String telefono = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_TELEFONO));
            nombrecontactoTextView.setText(nombre);
            telefonocontactoTextView.setText(telefono);
            cursor.close();
        } else {
            Toast.makeText(this, "No hay contactos guardados", Toast.LENGTH_SHORT).show();
        }
    }
}
