package rubioclemente.miguelangel.keychest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import java.util.Arrays;

public class CreateDataActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Toolbar menu;
    Spinner spinnerCategory;
    EditText txtNameData,txtDescriptionData,txtPasswordData;
    Button btnDataSubmit;
    //Generamos objeto categoria que instanciaremos en la selección.
    Category c;
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
        //Recogemos usuario y categorias del parcelable procedente de MainActivity
        User user =getIntent().getParcelableExtra("USER");
        Categories categories = getIntent().getParcelableExtra("CATEGORIES");
        //Category[] categories;
        //Parcelable []parcelable = getIntent().getParcelableArrayExtra("CATEGORIES");
        //categories = Arrays.copyOf(parcelable, parcelable.length, Category[].class);
        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this, R.layout.spinner_list, categories.getCategories());
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_list);
        spinnerCategory.setAdapter(spinnerArrayAdapter);
        spinnerCategory.setOnItemSelectedListener(this);

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

            System.out.println(c.getName());
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