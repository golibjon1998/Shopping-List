package com.example.shoppinglist.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.shoppinglist.db.AppDatabase;
import com.example.shoppinglist.db.Category;

import java.util.List;

public class MainActivityViewModel extends AndroidViewModel {

    private MutableLiveData<List<Category>> listOfCategory;
    private AppDatabase appDatabase;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);

        listOfCategory = new MutableLiveData<>();
        appDatabase = AppDatabase.getDbInstance(getApplication().getApplicationContext());

    }

    public MutableLiveData<List<Category>> getCategoryListObserver() {
        return listOfCategory;
    }

    public void getAllCategories() {
        List<Category> categoryList = appDatabase.shoppingListDao().getAllCategoryList();
        if (categoryList.size() > 0) {
            listOfCategory.postValue(categoryList);
        } else {
            listOfCategory.postValue(null);
        }
    }

    public void insertCategory(String catName){
        Category category = new Category();
        category.categoryName = catName;
        appDatabase.shoppingListDao().insertCategory(category);
        getAllCategories();
    }

    public void updateCategory(Category category){
        appDatabase.shoppingListDao().updateCategory(category);
        getAllCategories();
    }

    public void deleteCategory(Category category){
        appDatabase.shoppingListDao().deleteCategory(category);
        getAllCategories();
    }
}
