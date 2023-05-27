package rubioclemente.miguelangel.keychest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import rubioclemente.miguelangel.keychest.adapters.DataAdapter;
import rubioclemente.miguelangel.keychest.apiservices.ConexionRetrofit;
import rubioclemente.miguelangel.keychest.model.Categories;
import rubioclemente.miguelangel.keychest.model.Data;

public class DataListActivity extends AppCompatActivity {
    private Toolbar menu;
    private RecyclerView recyclerData;
    private ArrayList<Data> dataUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_list);
        //Instancia Menu
        menu = (Toolbar) findViewById(R.id.menu);
        setSupportActionBar(menu);

        recyclerData = (RecyclerView)findViewById(R.id.recyclerData);
        Data data =getIntent().getParcelableExtra("DATA");
        Categories categories=getIntent().getParcelableExtra("CATEGORIES");
        Handler handler = new Handler(Looper.getMainLooper());
        CompletableFuture<Data[]> cf = ConexionRetrofit.getData(data);
        //Si la respuesta es incorrecta enviamos un mensaje con el error al usuario y volvemos al loginActivity
        cf.exceptionally((Throwable t)->{
            if(t.getMessage().contains("jwt expired")){
                Toast.makeText(getApplicationContext(),"Error to get data, please login again",Toast.LENGTH_LONG).show();
                Intent i = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(i);
            }
            handler.post(()-> Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_LONG).show());
            return null;
        }).thenApplyAsync((Data[] userData)->{
            //User userBd = user;
            if(userData != null){
                List<Data>listDataUser =Arrays.asList(userData);
                dataUser = new ArrayList<>(listDataUser);
                DataAdapter dataAdapter = new DataAdapter( dataUser,data,categories);
                recyclerData.setLayoutManager(new LinearLayoutManager(this));
                DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerData.getContext(), LinearLayoutManager.VERTICAL);
                recyclerData.addItemDecoration(mDividerItemDecoration);
                //recyclerData.setHasFixedSize(true);
                recyclerData.setAdapter(dataAdapter);
                dataAdapter.notifyDataSetChanged();

            }
            return userData;
        },handler::post);
    }

    @SuppressLint("RestrictedApi")
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