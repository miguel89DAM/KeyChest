package rubioclemente.miguelangel.keychest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import rubioclemente.miguelangel.keychest.model.User;

public class SettingsActivity extends AppCompatActivity {

    //Declaracion de variables
    private EditText txtEmailUser, txtDataPassword, txtVerifyPassword;
    private ImageButton fabRevealData, fabChangeUserData;
    private LinearLayout lytChangeUserData;
    private Button btnEnableChangeUserData;
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        //Instancia de variables
        user =getIntent().getParcelableExtra("USER");
        txtEmailUser =(EditText)findViewById(R.id.txtEmailUser);
        txtDataPassword =(EditText)findViewById(R.id.txtDataPassword);
        txtVerifyPassword =(EditText)findViewById(R.id.txtVerifyPassword);
        fabRevealData = (ImageButton) findViewById(R.id.fabRevealData);
        fabChangeUserData = (ImageButton) findViewById(R.id.fabChangeUserData);
        lytChangeUserData = (LinearLayout)findViewById(R.id.lytChangeUserData);
        btnEnableChangeUserData =(Button)findViewById(R.id.btnEnableChangeUserData);

        //Imprimir datos del usuario al iniciar activity
        txtEmailUser.setText(user.getEmail());
        txtDataPassword.setText(user.getPasswd());

        //Listener para mostrar u ocultar contraseña
        fabRevealData.setOnClickListener((View v)->{
            if(fabRevealData.getTooltipText().toString().equals("REVEAL")){
                Drawable imgHidePassword= ResourcesCompat.getDrawable(v.getResources(),R.drawable.baseline_visibility_off_24,null);
                fabRevealData.setImageDrawable(imgHidePassword);
                txtDataPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                txtVerifyPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                fabRevealData.setTooltipText("HIDE");
            }else{
                Drawable imgRevealPassword= ResourcesCompat.getDrawable(v.getResources(),R.drawable.baseline_remove_red_eye_24,null);
                fabRevealData.setTooltipText("REVEAL");
                fabRevealData.setImageDrawable(imgRevealPassword);
                txtDataPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                txtVerifyPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        });

        //Listener que activa los campos para modificar los datos
        btnEnableChangeUserData.setOnClickListener((View v)->{
            btnEnableChangeUserData.setVisibility(View.GONE);
            txtDataPassword.setEnabled(true);
            lytChangeUserData.setVisibility(View.VISIBLE);

        });

        //listener que envia los datos modificados al servidor
        fabChangeUserData.setOnClickListener((View v)->{
            if(Utilities.validarDatos(txtEmailUser,txtDataPassword,txtVerifyPassword)){
                User u = new User(user.getToken(),user.getPasswd(),txtVerifyPassword.getText().toString());
            }
        });
    }
}