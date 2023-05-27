package rubioclemente.miguelangel.keychest.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Category implements Parcelable {

    private int id,numRows;
    private String name;
    private Category[] categories;

    public Category(){

    }
    public Category(Category[] categories){
        this.categories=categories;
    }

    public Category(int id, String name,int numRows){
        this.id = id;
        this.name = name;
        this.numRows = numRows;
    }

    protected Category(Parcel in) {
        id = in.readInt();
        numRows = in.readInt();
        name = in.readString();
        categories = in.createTypedArray(Category.CREATOR);
    }

    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(numRows);
        dest.writeString(name);
        dest.writeTypedArray( categories, flags);
    }

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

    public int getNumRows() {
        return numRows;
    }

    public void setNumRows(int numRows) {
        this.numRows = numRows;
    }

    public Category[] getCategories() {
        return categories;
    }

    public void setCategories(Category[] categories) {
        this.categories = categories;
    }

    @Override
    public String toString() {
        return name;
    }
}
