package rubioclemente.miguelangel.keychest;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.concurrent.CompletableFuture;
import rubioclemente.miguelangel.keychest.adapters.CategoryAdapter;
import rubioclemente.miguelangel.keychest.apiservices.ConexionRetrofit;
import rubioclemente.miguelangel.keychest.model.Categories;
import rubioclemente.miguelangel.keychest.model.Category;
import rubioclemente.miguelangel.keychest.model.Data;
import rubioclemente.miguelangel.keychest.model.User;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerCategories;
    private FloatingActionButton fabInsertData;
    private Toolbar menu;
    private User user;
    private Handler handler;
    private CompletableFuture<Category[]> cf;
    private Intent intenNewData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Intent que pasa la información del usuario y categorias para insertar datos de la app en BD
        intenNewData = new Intent(getApplicationContext(),CreateDataActivity.class);
        //Instancia Menu
        menu = (Toolbar) findViewById(R.id.menu);
        setSupportActionBar(menu);

        //Instancia boton flotante para añadir datos
        fabInsertData= findViewById(R.id.fabInsertData);
        //Instancia del recyclerview que muestra las categorias
        recyclerCategories=(RecyclerView)findViewById(R.id.recyclerCategories);
        user =getIntent().getParcelableExtra("USER");
        handler = new Handler(Looper.getMainLooper());
        cf = ConexionRetrofit.getCategories(new User(user.getToken()));
        //Si la respuesta es incorrecta enviamos un mensaje con el error al usuario y volvemos al loginActivity
        getCategoriesRequest();

        //Evento onclick que lanza la Activity CreateDataActivity para añadir registros del usuario
        fabInsertData.setOnClickListener((View v) ->{

            intenNewData.putExtra("USER",user);
            startActivity(intenNewData);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        cf = ConexionRetrofit.getCategories(new User(user.getToken()));
        //Si la respuesta es incorrecta enviamos un mensaje con el error al usuario y volvemos al loginActivity
        getCategoriesRequest();
    }

    private void getCategoriesRequest(){
        cf.exceptionally((Throwable t)->{
            if(t.getMessage().contains("jwt expired")){
                Toast.makeText(getApplicationContext(),"Error to get data, please login again",Toast.LENGTH_LONG).show();
                Intent i = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(i);
            }
            handler.post(()-> Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_LONG).show());
            return null;
        }).thenApplyAsync((Category[] categories)->{
            //User userBd = user;
            //Si la respuesta es correcta derivamos al MainActivity
            if(categories != null){
                //Instanciamos categorias para pasarlo por intent en la creación de nuevos registros
                Categories categories1 =new Categories(categories);
                intenNewData.putExtra("CATEGORIES",categories1);
                CategoryAdapter categoryAdapter = new CategoryAdapter(categories);
                recyclerCategories.setLayoutManager(new LinearLayoutManager(this));
                DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerCategories.getContext(), LinearLayoutManager.VERTICAL);
                recyclerCategories.addItemDecoration(mDividerItemDecoration);
                recyclerCategories.setAdapter(categoryAdapter);
                categoryAdapter.setOnclickListener((View v)->{
                    Category c =categories[recyclerCategories.getChildAdapterPosition(v)];
                    Intent i = new Intent(getApplicationContext(), DataListActivity.class);
                    Data data = new Data(user,c);
                    i.putExtra("DATA",data);
                    i.putExtra("CATEGORIES",categories1);
                    startActivity(i);
                });
                categoryAdapter.notifyDataSetChanged();
            }
            return categories;
        },handler::post);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app_menu, menu);
        if (menu instanceof MenuBuilder) {
            MenuBuilder m = (MenuBuilder) menu;
            m.setOptionalIconsVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
// Handle item selection
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.nav_account:

                return true;
            case R.id.nav_settings:

                return true;
            case R.id.nav_logout:
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}