package rubioclemente.miguelangel.keychest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.concurrent.CompletableFuture;
import rubioclemente.miguelangel.keychest.apiservices.ConexionRetrofit;
import rubioclemente.miguelangel.keychest.model.User;

public class LoginActivity extends AppCompatActivity {

    EditText txtEmail,txtPsswd;
    Button btnSignUp,btnSignIn,btnForgotPswd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        txtEmail = (EditText) findViewById(R.id.userId);
        txtPsswd = (EditText) findViewById(R.id.userPassword);
        btnSignUp = (Button)findViewById(R.id.btnSignUp);
        btnSignIn = (Button) findViewById(R.id.btnSignIn);
        btnForgotPswd = (Button)findViewById(R.id.btnForgotPswd);
        btnSignIn.setOnClickListener((View v)->{
            //Chequear que los campos no están vacíos
            if(Utilities.validarDatos(txtEmail,txtPsswd)){
                User u = new User(txtEmail.getText().toString(),txtPsswd.getText().toString());
                //Petición al webservices para acceder a la app
                Handler handler = new Handler(Looper.getMainLooper());
                CompletableFuture<User> cf = ConexionRetrofit.getUser(u);
                //Si la respuesta es incorrecta enviamos un mensaje con el error al usuario
                cf.exceptionally((Throwable t)->{
                    handler.post(()-> Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_LONG).show());
                    return null;
                }).thenApplyAsync((User user)->{
                    User userBd = user;
                    //Si la respuesta es correcta derivamos al MainActivity
                    if(userBd != null){
                        userBd.setPasswd(txtPsswd.getText().toString());
                        Intent i = new Intent(getApplicationContext(),MainActivity.class);
                        i.putExtra("USER",userBd);
                        startActivity(i);
                    }
                    return user;
                },handler::post);
            }
        });
        //Evento que gestiona el alta de usuarios, lanza la activity SignInActivity
        btnSignUp.setOnClickListener((View v)->{
            Intent i = new Intent(getApplicationContext(), SignUpActivity.class);
            startActivity(i);
        });

        btnForgotPswd.setOnClickListener((View v)->{
            Intent i = new Intent(getApplicationContext(), RecoveryAccountActivity.class);
            startActivity(i);
        });


    }


}