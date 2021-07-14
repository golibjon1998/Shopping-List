package com.example.shoppinglist;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppinglist.adapter.CategoryListAdapter;
import com.example.shoppinglist.db.Category;
import com.example.shoppinglist.viewmodel.MainActivityViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity implements CategoryListAdapter.HandleCategoryClick {

    private MainActivityViewModel viewModel;
    private TextView noResultTv;
    private RecyclerView recyclerView;
    private Category categoryForEdit;
    private CategoryListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("Shopping List");
        noResultTv = findViewById(R.id.noResultTv);
        recyclerView = findViewById(R.id.recyclerview);
        ImageView addNewCategory = findViewById(R.id.addCategoryIv);
        addNewCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddCategoryDialog(false);
            }
        });
        initViewModel();
        initRecyclerView();
        viewModel.getAllCategories();
    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CategoryListAdapter(this, this);
        recyclerView.setAdapter(adapter);

    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        viewModel.getCategoryListObserver().observe(this, new Observer<List<Category>>() {
            @Override
            public void onChanged(List<Category> categories) {
                if (categories == null) {
                    noResultTv.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                } else {
                    adapter.setCategoryList(categories);
                    recyclerView.setVisibility(View.VISIBLE);
                    noResultTv.setVisibility(View.GONE);
                }
            }
        });
    }

    private void showAddCategoryDialog(boolean isForEdit) {
        AlertDialog dialogBuilder = new AlertDialog.Builder(this).create();
        View dialogView = getLayoutInflater().inflate(R.layout.add_category_layout, null);

        EditText enterCategoryInput = dialogView.findViewById(R.id.enterCategoryInput);
        TextView create_btn = dialogView.findViewById(R.id.create_btb);
        TextView cancel_btn = dialogView.findViewById(R.id.cancel_btn);

        if (isForEdit) {
            create_btn.setText("Update");
            enterCategoryInput.setText(categoryForEdit.categoryName);
        }

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBuilder.dismiss();
            }
        });

        create_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = enterCategoryInput.getText().toString();
                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(MainActivity.this, "Enter category name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (isForEdit) {
                    categoryForEdit.categoryName = name;
                    viewModel.updateCategory(categoryForEdit);
                } else {
                    viewModel.insertCategory(name);
                }
                dialogBuilder.dismiss();
            }
        });

        dialogBuilder.setView(dialogView);
        dialogBuilder.show();

    }

    @Override
    public void itemClick(Category category) {
        Intent intent = new Intent(MainActivity.this,ShowItemsListActivity.class);
        intent.putExtra("category_id",category.uid);
        intent.putExtra("category_name",category.categoryName);

        startActivity(intent);
    }

    @Override
    public void removeItem(Category category) {
        viewModel.deleteCategory(category);
    }

    @Override
    public void editItem(Category category) {
        this.categoryForEdit = category;
        showAddCategoryDialog(true);

    }
}