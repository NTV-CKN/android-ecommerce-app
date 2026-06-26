//package com.infix.phukiencongnghe.data.source.local.repo;
//
//import android.content.Context;
//
//import androidx.lifecycle.LiveData;
//
//import com.infix.phukiencongnghe.data.source.local.AppDatabase;
//import com.infix.phukiencongnghe.data.source.local.dao.CartDAO;
//import com.infix.phukiencongnghe.data.source.local.entity.CartEntity;
//
//import java.util.List;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
//public class CartLocalRepositoryImpl implements ICartLocalRepository{
//    private final CartDAO cartDAO;
//    private final ExecutorService executors;
//
//    public CartLocalRepositoryImpl(Context context) {
//        this.cartDAO = AppDatabase.getInstance(context).cartDAO();
//        this.executors = Executors.newSingleThreadExecutor();
//    }
//
//    public LiveData<List<CartEntity>> getAll() {
//        return cartDAO.getAll();
//    }
//
//    public void addItem(CartEntity item) {
//        executors.execute(() -> cartDAO.insert(item));
//    }
//
//    public void updateItem(CartEntity item) {
//        executors.execute(() -> cartDAO.update(item));
//    }
//
//    public void deleteItem(CartEntity item) {
//        executors.execute(() -> cartDAO.delete(item));
//    }
//
//    public void deleteById(List<Integer> ids) {
//        executors.execute(() -> cartDAO.deleteById(ids));
//    }
//
//    public void clearAll() {
//        executors.execute(cartDAO::clearAll);
//    }
//}
