package rubioclemente.miguelangel.keychest.adapters;


import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import rubioclemente.miguelangel.keychest.DataActivity;
import rubioclemente.miguelangel.keychest.DataListActivity;
import rubioclemente.miguelangel.keychest.LoginActivity;
import rubioclemente.miguelangel.keychest.R;
import rubioclemente.miguelangel.keychest.apiservices.ConexionRetrofit;
import rubioclemente.miguelangel.keychest.model.Categories;
import rubioclemente.miguelangel.keychest.model.Category;
import rubioclemente.miguelangel.keychest.model.Data;
import rubioclemente.miguelangel.keychest.model.User;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolderData> /*implements View.OnClickListener*/{

    private ArrayList<Data> dataUser;
    private Data userCategory;
    private Categories categories;
    //private View.OnClickListener listener;


    public DataAdapter(ArrayList<Data>dataUser){
        this.dataUser=dataUser;
    }

    public DataAdapter(ArrayList<Data>dataUser, Data userCategory){
        this.dataUser=dataUser;
        this.userCategory=userCategory;
    }

    public DataAdapter(ArrayList<Data>dataUser, Data userCategory,Categories categories){
        this.dataUser=dataUser;
        this.userCategory=userCategory;
        this.categories=categories;
    }
    @NonNull
    @Override
    public DataAdapter.ViewHolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_data,parent,false);
        //view.setOnClickListener(this);
        return new ViewHolderData(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DataAdapter.ViewHolderData holder, int position) {
        holder.asignData(dataUser.get(position));

        holder.fabEditData.setOnClickListener((View v)->{
            userCategory.setId(dataUser.get(position).getId());
            userCategory.setName(dataUser.get(position).getName());
            userCategory.setDescription(dataUser.get(position).getDescription());
            userCategory.setDataPassword(dataUser.get(position).getDataPassword());
            Intent i = new Intent(v.getContext(), DataActivity.class);
            i.putExtra("DATA",userCategory);
            i.putExtra("CATEGORIES",categories);
            v.getContext().startActivity(i);
        });

        holder.fabDeleteData.setOnClickListener((View v)->{
            userCategory.setId(dataUser.get(position).getId());
            Handler handler = new Handler(Looper.getMainLooper());
            CompletableFuture<String> cf = ConexionRetrofit.deleteData(userCategory);
            //Si la respuesta es incorrecta enviamos un mensaje con el error al usuario y volvemos al loginActivity
            cf.exceptionally((Throwable t)->{
                if(t.getMessage().contains("jwt expired")){
                    Toast.makeText(v.getContext(),"Error to get data, please login again",Toast.LENGTH_LONG).show();
                    Intent i = new Intent(v.getContext(), LoginActivity.class);
                    v.getContext().startActivity(i);
                }
                handler.post(()-> Toast.makeText(v.getContext(),t.getMessage(),Toast.LENGTH_LONG).show());
                return null;
            }).thenApplyAsync((String result)->{
                if(result.equals("1")){
                    dataUser.remove(position);
                    notifyItemRemoved(position);
                }else{
                    System.out.println("ERROR: "+result);
                }
                return result;
            },handler::post);


        });

    }

    @Override
    public int getItemCount() {
        return dataUser.size();
    }

    /*public void setOnclickListener(View.OnClickListener listener){
        this.listener=listener;
    }
    @Override
    public void onClick(View v) {
        if(listener !=null){
            listener.onClick(v);
        }
    }*/

    public class ViewHolderData extends RecyclerView.ViewHolder {

        private TextView txtDataListName;
        private ImageButton fabDeleteData,fabEditData;


        public ViewHolderData(@NonNull View itemView) {
            super(itemView);
            //Data data =getIntent().getParcelableExtra("DATA");

            txtDataListName=(TextView) itemView.findViewById(R.id.txtDataListName);
            fabDeleteData =(ImageButton) itemView.findViewById(R.id.fabDeleteData);
            fabEditData =(ImageButton) itemView.findViewById(R.id.fabEditData);
        }

        public void asignData(Data data) {
            txtDataListName.setText(data.getName());
        }
    }
}
