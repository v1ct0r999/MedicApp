package cl.contreras.medicapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);

        Button btniniciar = (Button) findViewById(R.id.btnentrar);
        EditText usuario = (EditText) findViewById(R.id.usuario);

        btniniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usuarioInput = usuario.getText().toString().trim();

                // Validar el campo "usuario"
                if (usuarioInput.isEmpty()) {
                    usuario.setError("El campo de usuario no puede estar vacío");
                } else {
                    startActivity(new Intent(Splash.this, MainActivity.class));
                }
            }
        });

    }
}