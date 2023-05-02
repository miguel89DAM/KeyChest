package rubioclemente.miguelangel.keychest;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Categories implements Parcelable {
    private Category[] categories;
    public Categories(Category[] categories){
        this.categories = categories;
    }

    protected Categories(Parcel in) {
        categories = in.createTypedArray(Category.CREATOR);
    }

    public static final Creator<Categories> CREATOR = new Creator<Categories>() {
        @Override
        public Categories createFromParcel(Parcel in) {
            return new Categories(in);
        }

        @Override
        public Categories[] newArray(int size) {
            return new Categories[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeTypedArray(categories, flags);
    }

    public Category[] getCategories() {
        return categories;
    }

    public void setCategories(Category[] categories) {
        this.categories = categories;
    }
}
