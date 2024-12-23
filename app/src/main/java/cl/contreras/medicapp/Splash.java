package cl.contreras.medicapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class Splash extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private EditText EditTextUsuario;
    private static final int REQUEST_CODE_NOTIFICATIONS = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        EditTextUsuario = findViewById(R.id.EditTextUsuario);
        Button btnEntrar = findViewById(R.id.btnEntrar);

        // Inicializa SharedPreferences
        sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);

        // Validar permisos en tiempo de ejecución
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13+
            if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, REQUEST_CODE_NOTIFICATIONS);
            }
        }

        // Carga el nombre guardado si existe
        String savedName = sharedPreferences.getString("USER_NAME", null);
        if (savedName != null) {
            EditTextUsuario.setText(savedName);
            startActivity(new Intent(Splash.this, MainActivity.class));
            finish();
        }

        // Al hacer clic en el botón de login
        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener el texto del EditText
                String name = EditTextUsuario.getText().toString().trim();
                EditTextUsuario.setText("");

                // Validar si el campo de nombre está vacío
                if (name.isEmpty()) {
                    // Mostrar un mensaje de error si está vacío
                    Toast.makeText(Splash.this, "Por favor, ingresa tu nombre", Toast.LENGTH_SHORT).show();
                } else {
                    // Guardar el nombre en SharedPreferences
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("USER_NAME", name);
                    editor.apply();

                    // Intent para ir a la pantalla de saludo
                    Intent intent = new Intent(Splash.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    // Manejar el resultado de la solicitud de permisos
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_NOTIFICATIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permiso de notificaciones concedido", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permiso de notificaciones denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
