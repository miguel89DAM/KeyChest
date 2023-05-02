package rubioclemente.miguelangel.keychest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.concurrent.CompletableFuture;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerCategories;
    FloatingActionButton fabInsertData;
    Toolbar menu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Intent que pasa la información del usuario y categorias para insertar datos de la app en BD
        Intent intenNewData = new Intent(getApplicationContext(),CreateDataActivity.class);
        //Instancia Menu
        menu = (Toolbar) findViewById(R.id.menu);
        setSupportActionBar(menu);
        //Instancia boton flotante para añadir datos
        fabInsertData= findViewById(R.id.fabInsertData);
        //Instancia del recyclerview que muestra las categorias
        recyclerCategories=(RecyclerView)findViewById(R.id.recyclerCategories);

        User user =getIntent().getParcelableExtra("USER");
        Handler handler = new Handler(Looper.getMainLooper());
        CompletableFuture<Category[]> cf = ConexionRetrofit.getCategories(new User(user.getToken()));
        //Si la respuesta es incorrecta enviamos un mensaje con el error al usuario y volvemos al loginActivity
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
                categoryAdapter.notifyDataSetChanged();
            }
            return categories;
        },handler::post);

        //Evento onclick que lanza la Activity CreateDataActivity para añadir registros del usuario
        fabInsertData.setOnClickListener((View v) ->{

            intenNewData.putExtra("USER",user);
            startActivity(intenNewData);
        });
    }

}