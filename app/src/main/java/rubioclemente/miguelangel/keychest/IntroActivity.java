package rubioclemente.miguelangel.keychest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
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
        //getSupportActionBar().hide();
        setContentView(R.layout.activity_intro);
        ImageView background = (ImageView) findViewById(R.id.logoIntro);
        Animation animation = AnimationUtils.loadAnimation(this,R.anim.side_slide);
        background.startAnimation(animation);

        TimerTask task = new TimerTask() {
            public void run() {
                // Arrancamos la siguiente actividad
                Intent loginIntent = new Intent().setClass(getApplicationContext(), LoginActivity.class);
                startActivity(loginIntent);
                // Cerramos esta actividad para que el usuario no pueda volver a ella mediante botón de volver atras
                finish();
            }
        };
        // Simulamos un tiempo en el proceso de carga durante el cual mostramos el splash screen
        Timer timer = new Timer();
        timer.schedule(task, 3000); //Tiempo de espera del temporizador en milisegundos

    }
}