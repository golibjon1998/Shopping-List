package com.example.shoppinglist.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.shoppinglist.db.AppDatabase;
import com.example.shoppinglist.db.Category;
import com.example.shoppinglist.db.Items;

import java.util.List;

public class ShowItemListActivityViewModel extends AndroidViewModel {

    private MutableLiveData<List<Items>> listOfItems;
    private AppDatabase appDatabase;

    public ShowItemListActivityViewModel(@NonNull Application application) {
        super(application);

        listOfItems = new MutableLiveData<>();
        appDatabase = AppDatabase.getDbInstance(getApplication().getApplicationContext());

    }

    public MutableLiveData<List<Items>> getItemsListObserver() {
        return listOfItems;
    }

    public void getAllItems(int categoryId) {
        List<Items> itemsList = appDatabase.shoppingListDao().getAllItemsList(categoryId);
        if (itemsList.size() > 0) {
            listOfItems.postValue(itemsList);
        } else {
            listOfItems.postValue(null);
        }
    }

    public void insertItems(Items item){
        appDatabase.shoppingListDao().insertItems(item);
        getAllItems(item.categoryId);
    }

    public void updateItems(Items item){
        appDatabase.shoppingListDao().updateItems(item);
        getAllItems(item.categoryId);
    }

    public void deleteItems(Items item){
        appDatabase.shoppingListDao().deleteItem(item);
        getAllItems(item.categoryId);
    }
}
