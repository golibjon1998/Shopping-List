package com.example.shoppinglist.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppinglist.R;
import com.example.shoppinglist.db.Items;

import java.util.List;

public class ItemsListAdapter extends RecyclerView.Adapter<ItemsListAdapter.MyViewHolder> {

    private Context context;
    private List<Items> itemsList;
    private HandleItemClick clickListener;

    public ItemsListAdapter(Context context, HandleItemClick clickListener) {
        this.context = context;
        this.clickListener = clickListener;
    }

    public void setItemsList(List<Items> itemsList) {
        this.itemsList = itemsList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ItemsListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemsListAdapter.MyViewHolder holder, int position) {
        Items items = itemsList.get(position);
        holder.tvItemName.setText(items.itemName);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.itemClick(items);
            }
        });


        holder.editCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.editItem(items);
            }
        });


        holder.removeCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.removeItem(items);
            }
        });

        if (items.completed) {
            holder.tvItemName.setPaintFlags(holder.tvItemName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.tvItemName.setPaintFlags(0);
        }

    }

    @Override
    public int getItemCount() {
        return itemsList == null || itemsList.size() == 0 ? 0 : itemsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvItemName;
        ImageView removeCategory;
        ImageView editCategory;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItemName = itemView.findViewById(R.id.tvCategoryName);
            removeCategory = itemView.findViewById(R.id.removeCategory);
            editCategory = itemView.findViewById(R.id.editCategory);
        }
    }

    public interface HandleItemClick {

        void itemClick(Items item);

        void removeItem(Items item);

        void editItem(Items item);
    }
}
