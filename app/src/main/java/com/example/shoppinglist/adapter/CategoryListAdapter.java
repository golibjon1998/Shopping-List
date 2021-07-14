package com.example.shoppinglist.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppinglist.R;
import com.example.shoppinglist.db.Category;

import java.util.List;

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.MyViewHolder> {

    private Context context;
    private List<Category> categoryList;
    private HandleCategoryClick clickListener;

    public CategoryListAdapter(Context context,HandleCategoryClick clickListener) {
        this.context = context;
        this.clickListener = clickListener;
    }

    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoryListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryListAdapter.MyViewHolder holder, int position) {
        Category category = categoryList.get(position);
        holder.tvCategoryName.setText(category.categoryName);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.itemClick(category);
            }
        });


        holder.editCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.editItem(category);
            }
        });


        holder.removeCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.removeItem(category);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryList == null || categoryList.size() == 0 ? 0 : categoryList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvCategoryName;
        ImageView removeCategory;
        ImageView editCategory;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
            removeCategory = itemView.findViewById(R.id.removeCategory);
            editCategory = itemView.findViewById(R.id.editCategory);
        }
    }
    public interface HandleCategoryClick{

        void itemClick(Category category);
        void removeItem(Category category);
        void editItem(Category category);
    }
}
