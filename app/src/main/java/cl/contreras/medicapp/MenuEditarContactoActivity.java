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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu_editar_contacto);

        // Inicializar el DatabaseHelper
        dbHelper = new DatabaseHelper(this);

        // Inicializar los TextViews
        nombrecontactoTextView = findViewById(R.id.nombrecontacto);
        telefonocontactoTextView = findViewById(R.id.telefonocontacto);

        Button botonEditarNombreContacto = findViewById(R.id.btnEditarNombreContacto);
        Button botonEditarFonoContacto = findViewById(R.id.btnEditarFonoContacto);
        Button botonVolverEditarContacto = findViewById(R.id.btnVolverEditarContacto);

        // Mostrar el último contacto guardado
        mostrarUltimoContacto();

        // Conectar el layout EditarContacto con EditarNombreContacto
        botonEditarNombreContacto.setOnClickListener(v -> {
            Intent editNameIntent = new Intent(MenuEditarContactoActivity.this, EditarNombreContactoActivity.class);
            startActivity(editNameIntent);
        });

        // Conectar el layout EditarContacto con EditarNumeroContacto
        botonEditarFonoContacto.setOnClickListener(v -> {
            Intent editPhoneIntent = new Intent(MenuEditarContactoActivity.this, EditarNumeroContactoActivity.class);
            startActivity(editPhoneIntent);
        });

        // Volver al MenuContacto
        botonVolverEditarContacto.setOnClickListener(v -> {
            Intent menuIntent = new Intent(MenuEditarContactoActivity.this, MenuContactoActivity.class);
            startActivity(menuIntent);
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
