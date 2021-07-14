package com.example.shoppinglist;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppinglist.adapter.ItemsListAdapter;
import com.example.shoppinglist.db.Items;
import com.example.shoppinglist.viewmodel.ShowItemListActivityViewModel;

import java.util.List;

public class ShowItemsListActivity extends AppCompatActivity implements ItemsListAdapter.HandleItemClick {

    private int category_id;
    private ItemsListAdapter itemsListAdapter;
    private ShowItemListActivityViewModel viewModel;
    private RecyclerView recyclerView;
    private TextView noResults;
    private Items itemsToUpdate = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_items_list);

        category_id = getIntent().getIntExtra("category_id", 0);
        String categoryName = getIntent().getStringExtra("category_name");

        getSupportActionBar().setTitle(categoryName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final EditText addNewItem = findViewById(R.id.addNewItem);
        ImageView addItemBtn = findViewById(R.id.addItemBtn);
        noResults = findViewById(R.id.noResultTv);

        addItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemName = addNewItem.getText().toString();
                if (TextUtils.isEmpty(itemName)) {
                    Toast.makeText(ShowItemsListActivity.this, "Enter Item Name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (itemsToUpdate == null)
                    saveNewItem(itemName);
                else
                    updateNewItem(itemName);
            }
        });

        initRecyclerview();
        initViewModel();
        viewModel.getAllItems(category_id);
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this).get(ShowItemListActivityViewModel.class);
        viewModel.getItemsListObserver().observe(this, new Observer<List<Items>>() {
            @Override
            public void onChanged(List<Items> items) {
                if (items == null) {
                    recyclerView.setVisibility(View.GONE);
                    noResults.setVisibility(View.VISIBLE);

                } else {
                    itemsListAdapter.setItemsList(items);
                    recyclerView.setVisibility(View.VISIBLE);
                    noResults.setVisibility(View.GONE);
                }
            }
        });
    }

    private void initRecyclerview() {
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        itemsListAdapter = new ItemsListAdapter(this, this);
        recyclerView.setAdapter(itemsListAdapter);


    }

    private void saveNewItem(String itemName) {
        Items item = new Items();
        item.itemName = itemName;
        item.categoryId = category_id;
        viewModel.insertItems(item);
        ((EditText) findViewById(R.id.addNewItem)).setText("");
    }

    @Override
    public void itemClick(Items item) {
        if (item.completed) {
            item.completed = false;
        } else {
            item.completed = true;
        }
        viewModel.updateItems(item);
    }

    @Override
    public void removeItem(Items item) {
        viewModel.deleteItems(item);
    }

    @Override
    public void editItem(Items item) {
        this.itemsToUpdate = item;
        ((EditText) findViewById(R.id.addNewItem)).setText(item.itemName);

    }

    private void updateNewItem(String newItem){
        itemsToUpdate.itemName = newItem;
        viewModel.updateItems(itemsToUpdate);
        ((EditText) findViewById(R.id.addNewItem)).setText("");
        itemsToUpdate = null;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}