package rubioclemente.miguelangel.keychest;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolderCategory> {

    private Category [] categories;
    private TextView txtCategoryName, txtnumDataUser;

    public CategoryAdapter(Category[] categories){
        this.categories=categories;
    }
    @NonNull
    @Override
    public ViewHolderCategory onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_categories,parent,false);
        return new ViewHolderCategory(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderCategory holder, int position) {
        holder.asignCategories(categories[position]);
    }

    @Override
    public int getItemCount() {
        return categories.length;
    }

    public class ViewHolderCategory extends RecyclerView.ViewHolder {

        public ViewHolderCategory(@NonNull View itemView) {
            super(itemView);
            txtCategoryName=(TextView) itemView.findViewById(R.id.txtCategoryName);
            txtnumDataUser=(TextView) itemView.findViewById(R.id.txtnumRegisterUser);
        }

        public void asignCategories(Category category) {
            txtCategoryName.setText(category.getName());
            txtnumDataUser.setText(Integer.toString(category.getNumRows()));
        }
    }
}
