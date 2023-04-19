package rubioclemente.miguelangel.keychest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.concurrent.CompletableFuture;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        EditText txtEmail =(EditText) findViewById(R.id.txtEmail);
        EditText txtPassword =(EditText) findViewById(R.id.txtPassword);
        EditText txtPasswordVerify =(EditText) findViewById(R.id.txtPasswordVerify);
        CheckBox checkConditions = (CheckBox)findViewById(R.id.checkConditions);
        Button btnSignUp =(Button) findViewById(R.id.btnSignUp);
        TextView txtMessageRegister =(TextView)findViewById(R.id.txtMessageRegister);
        btnSignUp.setOnClickListener((View v) ->{
            if(Utilities.validarDatos(txtEmail,txtPassword,txtPasswordVerify) && checkConditions.isChecked() ){
                User u = new User(txtEmail.getText().toString(),txtPassword.getText().toString());
                //Petición al webservices para acceder a la app
                Handler handler = new Handler(Looper.getMainLooper());
                CompletableFuture<Integer> cf = ConexionRetrofit.insertUser(u);
                //Si la respuesta es incorrecta enviamos un mensaje con el error al usuario
                cf.exceptionally((Throwable t)->{
                    handler.post(()-> Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_LONG).show());
                    return null;
                }).thenApplyAsync((Integer checkInsert)->{
                    //Si la respuesta es correcta derivamos al MainActivity
                    if(checkInsert != 0 && checkInsert != null){
                        //Toast.makeText(getApplicationContext(),"Register Succesfully",Toast.LENGTH_LONG).show();
                        txtMessageRegister.setVisibility(View.VISIBLE);
                        new MaterialAlertDialogBuilder(getApplicationContext(),
                                R.style.Body_ThemeOverlay_MaterialComponents_MaterialAlertDialog).setTitle("PRUEBA").setMessage("Prueba").show();
                        //Intent i = new Intent(getApplicationContext(),LoginActivity.class);
                        //startActivity(i);
                    }
                    return checkInsert;
                },handler::post);
            }
        });
    }
}