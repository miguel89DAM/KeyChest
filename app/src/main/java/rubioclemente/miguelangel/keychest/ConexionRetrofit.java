package rubioclemente.miguelangel.keychest;


import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public class ConexionRetrofit {

    static ConexionHttps conexion = new ConexionHttps();
    static  OkHttpClient httpsClient= conexion.getConnection().connectionSpecs(Arrays.asList(ConnectionSpec.MODERN_TLS, ConnectionSpec.COMPATIBLE_TLS)).build();


    //public static OkHttpClient httpClient = new OkHttpClient.Builder().connectionSpecs(Arrays.asList(ConnectionSpec.MODERN_TLS, ConnectionSpec.COMPATIBLE_TLS)).build();

    //Instancia de Retrofit.
    private static Retrofit retrofitInstance=null;

    public ConexionRetrofit() throws NoSuchAlgorithmException {
    }

    public static Retrofit getRetrofit(){
        if(retrofitInstance == null){
            retrofitInstance= new Retrofit.Builder().client(httpsClient).baseUrl("https://192.168.1.10:3300/").addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofitInstance;
    }

    //Interfaz para acceder a la api de KeyChest.
    public interface ServiceUsers {
        //@Headers("Content-Type: application/json")
        @POST("/users/user")
        Call<User> getUser(@Body User user);

        @POST("/users/newUser")
        Call<Integer> insertUser(@Body User user);
    }

    //INSTANCIAR SERVICIO DE Usuarios
    private static ServiceUsers serviceUsersInstance = null;
    public static ServiceUsers getServiceUsers(){
        if(serviceUsersInstance == null){
            serviceUsersInstance=getRetrofit().create(ServiceUsers.class);
        }
        return serviceUsersInstance;
    }

    public static CompletableFuture<User> getUser(User user){

        //Peticion a la web para extraer el usuario
        Call<User>userRequest = getServiceUsers().getUser(user);
        CompletableFuture<User> cf = new CompletableFuture<>();
        userRequest.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                System.out.println(call.request());
                System.out.println( "headers----------------------------------");
                System.out.println( response.headers().toString() );
                System.out.println( "message----------------------------------");
                System.out.println( response.message() );
                System.out.println( response.body());
                if(!response.isSuccessful()){
                    try{
                        String bodyErr = response.errorBody().string();
                        JSONObject bodyObj = new JSONObject( bodyErr);
                        String msg=bodyObj.get("msg").toString();
                        cf.completeExceptionally(new RuntimeException(msg));
                    }catch(IOException io){
                        io.getMessage();
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
                User ms = response.body();
                cf.complete(ms);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                System.out.println( "Error en retrofit ------------------------------------" );
                t.getMessage();
                cf.completeExceptionally(t);
            }
        });
        return cf;
    }

    public static CompletableFuture<Integer> insertUser(User user){

        //Peticion a la web para extraer el usuario
        Call<Integer>userRequest = getServiceUsers().insertUser(user);
        CompletableFuture<Integer> cf = new CompletableFuture<>();
        userRequest.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                System.out.println(call.request());
                System.out.println( "headers----------------------------------");
                System.out.println( response.headers().toString() );
                System.out.println( "message----------------------------------");
                System.out.println( response.message() );
                System.out.println( response.body());
                if(!response.isSuccessful()){
                    try{
                        String bodyErr = response.errorBody().string();
                        JSONObject bodyObj = new JSONObject( bodyErr);
                        String msg=bodyObj.get("msg").toString();
                        cf.completeExceptionally(new RuntimeException(msg));
                    }catch(IOException io){
                        io.getMessage();
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
                Integer checkInsert = response.body();
                cf.complete(checkInsert);
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                System.out.println( "Error en retrofit ------------------------------------" );
                t.getMessage();
                cf.completeExceptionally(t);
            }
        });
        return cf;
    }
}
