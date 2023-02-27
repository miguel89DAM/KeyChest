package rubioclemente.miguelangel.keychest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        // Establecemos orientación exclusiva de retrato
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // Ocultamos la barra de titulo
        getSupportActionBar().hide();
        setContentView(R.layout.activity_intro);
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                // Arrancamos la siguiente actividad
                Intent mainIntent = new Intent().setClass(
                        getApplicationContext(), MainActivity.class);
                startActivity(mainIntent);
                // Cerramos esta actividad para que el usuario no pueda volver a ella mediante botón de volver atras
                finish();
            }
        };
        // Simulamos un tiempo en el proceso de carga durante el cual mostramos el splash screen
        Timer timer = new Timer();
        timer.schedule(task, 2000); //Tiempo de espera del temporizador en milisegundos
    }
}