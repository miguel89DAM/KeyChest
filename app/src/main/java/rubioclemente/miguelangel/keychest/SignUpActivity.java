package rubioclemente.miguelangel.keychest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
    EditText txtEmail,txtPassword,txtPasswordVerify;
    CheckBox checkConditions;
    Button btnSignUp;
    TextView txtMessageRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        //Instancia de elementos
        txtEmail =(EditText) findViewById(R.id.txtEmail);
        txtPassword =(EditText) findViewById(R.id.txtPassword);
        txtPasswordVerify =(EditText) findViewById(R.id.txtPasswordVerify);
        checkConditions = (CheckBox)findViewById(R.id.checkConditions);
        btnSignUp =(Button) findViewById(R.id.btnSignUp);
        txtMessageRegister =(TextView)findViewById(R.id.txtMessageRegister);
        //Pop up informativo al usuario para que revise su email
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Hemos mandado un email para validar su cuenta, haga clic en el enlace para poder usar la aplicacion").setTitle("Confirmación de cuenta");

        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent i = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(i);
            }
        });
        AlertDialog dialog = builder.create();
        //Evento onclick envia los datos de usuario al servidor
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
                        dialog.show();
                    }
                    return checkInsert;
                },handler::post);
            }
        });
    }
}