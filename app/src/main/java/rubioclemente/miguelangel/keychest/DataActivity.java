package rubioclemente.miguelangel.keychest;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.concurrent.CompletableFuture;

import rubioclemente.miguelangel.keychest.apiservices.ConexionRetrofit;
import rubioclemente.miguelangel.keychest.model.Categories;
import rubioclemente.miguelangel.keychest.model.Category;
import rubioclemente.miguelangel.keychest.model.Data;

public class DataActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private Toolbar menu;
    private EditText txtDataName,txtDataDescription,txtDataPassword;
    private ImageButton fabChangeData, fabRevealData,fabCopyData;
    private Spinner spinnerCategory;
    private Data data;
    private Categories categories;
    private Category c;
    private  Handler handler;
    private CompletableFuture<String> cf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        //Instancia Menu
        menu = (Toolbar) findViewById(R.id.menu);
        setSupportActionBar(menu);
        ActionBar actionBar = getSupportActionBar();
        menu.setNavigationIcon(R.drawable.baseline_arrow_back_24);
        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);
        menu.inflateMenu(R.menu.data_menu);
        //Extraccion de datos y categorias para mostrar información
        data =getIntent().getParcelableExtra("DATA");
        categories =getIntent().getParcelableExtra("CATEGORIES");
        //Creacion de spinner para seleccionar categorias
        spinnerCategory=(Spinner)findViewById(R.id.category_spinnerData);
        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this, R.layout.spinner_list, categories.getCategories());
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_list);
        spinnerCategory.setAdapter(spinnerArrayAdapter);
        for(int i = 0; i < categories.getCategories().length; i++){
            if(categories.getCategories()[i].getName().equals(data.getCategory().getName())){
                spinnerCategory.setSelection(i);
            }
        }
        spinnerCategory.setOnItemSelectedListener(this);
        spinnerCategory.setEnabled(false);
        //Instancia de elementos del layout
        txtDataName=(EditText) findViewById(R.id.txtDataName);
        txtDataDescription=(EditText) findViewById(R.id.txtDataDescription);
        txtDataPassword=(EditText) findViewById(R.id.txtDataPassword);
        fabChangeData =(ImageButton) findViewById(R.id.fabChangeData);
        fabRevealData =(ImageButton) findViewById(R.id.fabRevealData);
        fabCopyData =(ImageButton) findViewById(R.id.fabCopyData);

        //Seteo de datos en campos
        txtDataName.setText(data.getName());
        txtDataDescription.setText(data.getDescription());
        txtDataPassword.setText(data.getDataPassword());
        fabChangeData.setEnabled(false);

        //Listener para mostrar u ocultar contraseña
        fabRevealData.setOnClickListener((View v)->{
            if(fabRevealData.getTooltipText().toString().equals("REVEAL")){
                Drawable imgHidePassword= ResourcesCompat.getDrawable(v.getResources(),R.drawable.baseline_visibility_off_24,null);
                fabRevealData.setImageDrawable(imgHidePassword);
                txtDataPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                fabRevealData.setTooltipText("HIDE");
            }else{
                Drawable imgRevealPassword= ResourcesCompat.getDrawable(v.getResources(),R.drawable.baseline_remove_red_eye_24,null);
                fabRevealData.setTooltipText("REVEAL");
                fabRevealData.setImageDrawable(imgRevealPassword);
                txtDataPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

            }
        });
        //Listener para copiar datos al portapapeles
        fabCopyData.setOnClickListener((View v)->{
            ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            String getstring = txtDataPassword.getText().toString();
            ClipData clip = ClipData.newPlainText("DATAPASSWORD", getstring);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(getApplicationContext(),"Text copied",Toast.LENGTH_SHORT).show();
        });

        fabChangeData.setOnClickListener((View v)->{
            if(txtDataName.getText().toString().isEmpty()){
                txtDataName.setError("The name can't be empty");
                return;
            }
            if(txtDataPassword.getText().toString().isEmpty()){
                txtDataPassword.setError("The password can't be empty");
                return;
            }
            //Objeto dato que enviaremos al servidor
            Data dataUpdated = new Data(
                    data.getId(),
                    txtDataName.getText().toString(),
                    txtDataDescription.getText().toString(),
                    txtDataPassword.getText().toString(),
                    data.getUser(),
                    c
            );
            handler = new Handler(Looper.getMainLooper());
            cf = ConexionRetrofit.updateData(dataUpdated);
            updateDataRequest();
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.data_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
// Handle item selection
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.item_modificate:
                txtDataName.setEnabled(true);
                txtDataDescription.setEnabled(true);
                txtDataPassword.setEnabled(true);
                spinnerCategory.setEnabled(true);
                fabChangeData.setEnabled(true);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Metodos implementados de spinner

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        c =(Category)parent.getItemAtPosition(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void updateDataRequest(){
        cf.exceptionally((Throwable t)->{
            handler.post(()-> Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_LONG).show());
            return null;
        }).thenApplyAsync((String checkInsert)->{
            //Si la respuesta es correcta derivamos al MainActivity
            if(checkInsert.equals("1")){
                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                i.putExtra("USER",data.getUser());
                startActivity(i);
            }
            return checkInsert;
        },handler::post);
    }
}