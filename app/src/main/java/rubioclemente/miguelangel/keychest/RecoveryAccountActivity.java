package rubioclemente.miguelangel.keychest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.concurrent.CompletableFuture;

import rubioclemente.miguelangel.keychest.adapters.CategoryAdapter;
import rubioclemente.miguelangel.keychest.apiservices.ConexionRetrofit;
import rubioclemente.miguelangel.keychest.model.Categories;
import rubioclemente.miguelangel.keychest.model.Category;
import rubioclemente.miguelangel.keychest.model.Data;
import rubioclemente.miguelangel.keychest.model.User;

public class RecoveryAccountActivity extends AppCompatActivity {

    private EditText txtRecoverEmail,txtRecoverPassword,txtRecoverPasswordVerify;
    private Button btnSubmitRecovery, btnResendEmail;

    private Handler handler;
    private CompletableFuture<String> cf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recovery_account);
        txtRecoverEmail = (EditText) findViewById(R.id.txtRecoverEmail);
        txtRecoverPassword = (EditText) findViewById(R.id.txtRecoverPassword);
        txtRecoverPasswordVerify = (EditText) findViewById(R.id.txtRecoverPasswordVerify);
        btnSubmitRecovery = (Button) findViewById(R.id.btnSubmitRecovery);
        btnResendEmail = (Button) findViewById(R.id.btnResendEmail);

        handler = new Handler(Looper.getMainLooper());

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Atención, está acción eliminará toda información que tenga en su cuenta").setTitle("Reseteo de cuenta");

        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                cf = ConexionRetrofit.recoveryUser(new User(txtRecoverEmail.getText().toString(),txtRecoverPassword.getText().toString()));
                recoveryAccountRequest();
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
        AlertDialog dialog = builder.create();

        btnSubmitRecovery.setOnClickListener((View v)->{
            if(Utilities.validarDatos(txtRecoverEmail,txtRecoverPassword,txtRecoverPasswordVerify)){
                dialog.show();
            }
        });

        btnResendEmail.setOnClickListener((View v)->{
            if(Utilities.validarEmail(txtRecoverEmail)){
            }
        });

    }
    private void recoveryAccountRequest(){
        cf.exceptionally((Throwable t)->{
            handler.post(()-> Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_LONG).show());
            return null;
        }).thenApplyAsync((String result)->{
            if(result.contains("Email sent")){
                Toast.makeText(getApplicationContext(),"Email sent",Toast.LENGTH_LONG).show();
                Intent i = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(i);
                finish();
            }
            return result;
        },handler::post);
    }

}