package rubioclemente.miguelangel.keychest.apiservices;


import android.util.Log;

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
import retrofit2.http.DELETE;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import rubioclemente.miguelangel.keychest.apiservices.ConexionHttps;
import rubioclemente.miguelangel.keychest.model.Category;
import rubioclemente.miguelangel.keychest.model.Data;
import rubioclemente.miguelangel.keychest.model.User;

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
            retrofitInstance= new Retrofit.Builder().client(httpsClient).baseUrl("https://www.keychest.org:3300/").addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofitInstance;
    }

    //Interfaz para acceder a la api de KeyChest.
    public interface ServiceKeychest {
        //@Headers("Content-Type: application/json")
        //Login
        @POST("/users/user")
        Call<User> getUser(@Body User user);
        //Insert user
        @POST("/users/newUser")
        Call<Integer> insertUser(@Body User user);

        @POST("/users/recovery")
        Call<String> recoveryUser(@Body User user);
        //GetCategories
        @POST("/categories")
        Call<Category[]> getCategories(@Body User userToken);

        @POST("/data/new")
        Call<String> createData(@Body Data newData);

        @POST("/data/getData")
        Call<Data[]> getData(@Body Data data);

        @HTTP(method = "DELETE", path = "/data/deleteData", hasBody = true)
        Call<String> deleteData(@Body Data data);

        @PUT("/data/updateData")
        Call<String> updateData(@Body Data data);
    }

    //INSTANCIAR SERVICIO DE Usuarios
    private static ServiceKeychest serviceKeychestInstance = null;
    public static ServiceKeychest getServiceKeychest(){
        if(serviceKeychestInstance == null){
            serviceKeychestInstance =getRetrofit().create(ServiceKeychest.class);
        }
        return serviceKeychestInstance;
    }

    public static CompletableFuture<User> getUser(User user){

        //Peticion a la web para extraer el usuario
        Call<User>userRequest = getServiceKeychest().getUser(user);
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
        Call<Integer>userRequest = getServiceKeychest().insertUser(user);
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

    public static CompletableFuture<String> recoveryUser(User user){

        //Peticion a la web para guardar datos en BD
        Call<String>recoveryUserRequest = getServiceKeychest().recoveryUser(user);
        CompletableFuture<String> cf = new CompletableFuture<>();
        recoveryUserRequest.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
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
                        Log.d("ERR",e.getMessage());
                        throw new RuntimeException(e);
                    }
                }
                String responsePrueba = response.body();
                cf.complete(responsePrueba);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                System.out.println( "Error en retrofit ------------------------------------" );
                t.getMessage();
                cf.completeExceptionally(t);
            }
        });
        return cf;
    }

    public static CompletableFuture<Category[]> getCategories(User userToken){

        //Peticion a la web para extraer el usuario
        Call<Category[]>categoriesRequest = getServiceKeychest().getCategories(userToken);
        CompletableFuture<Category[]> cf = new CompletableFuture<>();
        categoriesRequest.enqueue(new Callback<Category[]>() {
            @Override
            public void onResponse(Call<Category[]> call, Response<Category[]> response) {
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
                Category[] checkInsert = response.body();
                cf.complete(checkInsert);
            }

            @Override
            public void onFailure(Call<Category[]> call, Throwable t) {
                System.out.println( "Error en retrofit ------------------------------------" );
                t.getMessage();
                cf.completeExceptionally(t);
            }
        });
        return cf;
    }

    public static CompletableFuture<String> createData(Data newData){

        //Peticion a la web para guardar datos en BD
        Call<String>newDataRequest = getServiceKeychest().createData(newData);
        CompletableFuture<String> cf = new CompletableFuture<>();
        newDataRequest.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
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
                        Log.d("ERR",e.getMessage());
                        throw new RuntimeException(e);
                    }
                }
                String responsePrueba = response.body();
                cf.complete(responsePrueba);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                System.out.println( "Error en retrofit ------------------------------------" );
                t.getMessage();
                cf.completeExceptionally(t);
            }
        });
        return cf;
    }

    public static CompletableFuture<Data[]> getData(Data data){

        //Peticion a la web para guardar datos en BD
        Call<Data[]>getDataRequest = getServiceKeychest().getData(data);
        CompletableFuture<Data[]> cf = new CompletableFuture<>();
        getDataRequest.enqueue(new Callback<Data[]>() {
            @Override
            public void onResponse(Call<Data[]> call, Response<Data[]> response) {
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
                        Log.d("ERR",e.getMessage());
                        throw new RuntimeException(e);
                    }
                }
                Data[] responsePrueba = response.body();
                cf.complete(responsePrueba);
            }

            @Override
            public void onFailure(Call<Data[]> call, Throwable t) {
                System.out.println( "Error en retrofit ------------------------------------" );
                t.getMessage();
                cf.completeExceptionally(t);
            }
        });
        return cf;
    }

    public static CompletableFuture<String> deleteData(Data data){

        //Peticion a la web para guardar datos en BD
        Call<String>getDataRequest = getServiceKeychest().deleteData(data);
        CompletableFuture<String> cf = new CompletableFuture<>();
        getDataRequest.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
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
                        Log.d("ERR",e.getMessage());
                        throw new RuntimeException(e);
                    }
                }
                String responsePrueba = response.body();
                cf.complete(responsePrueba);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                System.out.println( "Error en retrofit ------------------------------------" );
                t.getMessage();
                cf.completeExceptionally(t);
            }
        });
        return cf;
    }

    public static CompletableFuture<String> updateData(Data data){

        //Peticion a la web para guardar datos en BD
        Call<String>getDataRequest = getServiceKeychest().updateData(data);
        CompletableFuture<String> cf = new CompletableFuture<>();
        getDataRequest.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
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
                        Log.d("ERR",e.getMessage());
                        throw new RuntimeException(e);
                    }
                }
                String responsePrueba = response.body();
                cf.complete(responsePrueba);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                System.out.println( "Error en retrofit ------------------------------------" );
                t.getMessage();
                cf.completeExceptionally(t);
            }
        });
        return cf;
    }
}
