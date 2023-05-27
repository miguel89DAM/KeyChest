package rubioclemente.miguelangel.keychest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;
import java.util.concurrent.CompletableFuture;
import rubioclemente.miguelangel.keychest.apiservices.ConexionRetrofit;
import rubioclemente.miguelangel.keychest.model.Categories;
import rubioclemente.miguelangel.keychest.model.Category;
import rubioclemente.miguelangel.keychest.model.Data;
import rubioclemente.miguelangel.keychest.model.User;

public class CreateDataActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Toolbar menu;
    private Spinner spinnerCategory;
    private EditText txtNameData,txtDescriptionData,txtPasswordData;
    private Button btnDataSubmit,btnRandomPassword;
    private ImageButton btnRevealPassword;
    //Generamos objeto categoria que instanciaremos en la selección.
    private Category c;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_data);
        //Instancia del menu
        menu = (Toolbar) findViewById(R.id.menu);
        setSupportActionBar(menu);
        //Instancia del desplegable spinner que muestra las categorias
        spinnerCategory = (Spinner) findViewById(R.id.category_spinner);
        txtNameData =(EditText)findViewById(R.id.txtNameData);
        txtDescriptionData =(EditText)findViewById(R.id.txtDescriptionData);
        txtPasswordData =(EditText)findViewById(R.id.txtPasswordData);
        btnDataSubmit =(Button)findViewById(R.id.btnDataSubmit);
        btnRandomPassword =(Button)findViewById(R.id.btnRandomPassword);
        btnRevealPassword =(ImageButton)findViewById(R.id.btnRevealPassword);
        //Recogemos usuario y categorias del parcelable procedente de MainActivity
        User user =getIntent().getParcelableExtra("USER");
        Categories categories = getIntent().getParcelableExtra("CATEGORIES");
        //Generamos el desplegable con las categorias de la app.
        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this, R.layout.spinner_list, categories.getCategories());
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_list);
        spinnerCategory.setAdapter(spinnerArrayAdapter);
        spinnerCategory.setOnItemSelectedListener(this);
        //Listener de envío de informacion al servidor
        btnDataSubmit.setOnClickListener((View v) ->{
            if(txtNameData.getText().toString().isEmpty()){
                txtNameData.setError("The name can't be empty");
                return;
            }
            if(txtPasswordData
                    .getText().toString().isEmpty()){
                txtPasswordData.setError("The password can't be empty");
                return;
            }
            //Objeto dato que enviaremos al servidor
            Data data = new Data(txtNameData.getText().toString(),
                    txtDescriptionData.getText().toString(),
                    txtPasswordData.getText().toString(),
                    user,
                    c
            );
            Handler handler = new Handler(Looper.getMainLooper());
            CompletableFuture<String> cf = ConexionRetrofit.createData(data);
            //Si la respuesta es incorrecta enviamos un mensaje con el error al usuario
            cf.exceptionally((Throwable t)->{
                handler.post(()-> Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_LONG).show());
                return null;
            }).thenApplyAsync((String checkInsert)->{
                //Si la respuesta es correcta derivamos al MainActivity
                if(checkInsert.equals("1")){
                    Intent i = new Intent(getApplicationContext(),MainActivity.class);
                    i.putExtra("USER",user);
                    startActivity(i);
                }
                return checkInsert;
            },handler::post);
            System.out.println(c.getName());
        });
        //Listener generacion de password aleatoria
        btnRandomPassword.setOnClickListener((View v)->{
            txtPasswordData.setText(Utilities.randomText());
        });
        //Listener de revelacion y ocultación de contraseña en el front
        btnRevealPassword.setOnClickListener((View v)->{
            if(btnRevealPassword.getTooltipText().toString().equals("REVEAL")){
                Drawable imgHidePassword= ResourcesCompat.getDrawable(getResources(),R.drawable.baseline_visibility_off_24,null);
                btnRevealPassword.setImageDrawable(imgHidePassword);
                txtPasswordData.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                btnRevealPassword.setTooltipText("HIDE");
            }else{
                Drawable imgRevealPassword= ResourcesCompat.getDrawable(getResources(),R.drawable.baseline_remove_red_eye_24,null);
                btnRevealPassword.setTooltipText("REVEAL");
                btnRevealPassword.setImageDrawable(imgRevealPassword);
                txtPasswordData.setTransformationMethod(PasswordTransformationMethod.getInstance());

            }

        });
    }

    //Metodos implementados de spinner

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
        c =(Category)parent.getItemAtPosition(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}