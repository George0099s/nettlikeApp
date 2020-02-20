package com.avla.app.Interface;

public class SearchContract {

   public interface View {
        void search(String query);
    }

    public interface Presenter {
        void onSearch();
        void onDestroy();
    }

   public interface Repository {
        String loadMessage();
    }
}
