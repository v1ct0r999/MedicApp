package cl.contreras.medicapp;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class Splash extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private EditText EditTextUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        EditText EditTextUsuario = findViewById(R.id.EditTextUsuario);
        Button btnEntrar = findViewById(R.id.btnEntrar);

        // Inicializa SharedPreferences
        sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);

        // Carga el nombre guardado si existe
        String savedName = sharedPreferences.getString("USER_NAME", null);
        if (savedName != null) {
            EditTextUsuario.setText(savedName);
        }

        // Al hacer clic en el botón de login
        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener el texto del EditText
                String name = EditTextUsuario.getText().toString().trim();

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
}