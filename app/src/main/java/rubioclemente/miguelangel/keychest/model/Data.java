package rubioclemente.miguelangel.keychest.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Data implements Parcelable {

    private int id;
    private String name,description,dataPassword;
    private User user;
    private Category category;


    public Data(){

    }

    public Data(User user, Category category){
        this.user = user;
        this.category = category;
    }

    public Data( String name, String description, String dataPassword, User user, Category category) {
        this.name = name;
        this.description = description;
        this.dataPassword = dataPassword;
        this.user = user;
        this.category = category;
    }

    public Data(int id, String name, String description, String dataPassword, User user, Category category) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.dataPassword = dataPassword;
        this.user = user;
        this.category = category;
    }

    protected Data(Parcel in) {
        id = in.readInt();
        name = in.readString();
        description = in.readString();
        dataPassword = in.readString();
        user = in.readParcelable(User.class.getClassLoader());
        category = in.readParcelable(Category.class.getClassLoader());
    }

    public static final Creator<Data> CREATOR = new Creator<Data>() {
        @Override
        public Data createFromParcel(Parcel in) {
            return new Data(in);
        }

        @Override
        public Data[] newArray(int size) {
            return new Data[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDataPassword() {
        return dataPassword;
    }

    public void setDataPassword(String dataPassword) {
        this.dataPassword = dataPassword;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(dataPassword);
        dest.writeParcelable(user, flags);
        dest.writeParcelable(category, flags);
    }
}
