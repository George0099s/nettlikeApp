package com.avla.app.Interface;

public class Presenter implements SearchContract.Presenter{

    private SearchContract.View mView;
    private SearchContract.Repository mRepository;
    private String s;
    //Сообщение
    private String query;
    public Presenter(SearchContract.View mView, String s, String query) {
        this.mView = mView;
        this.s = s;
        this.query = query;
        this.mRepository = new Repository(this.s);
    }

    @Override
    public void onSearch() {
        query = mRepository.loadMessage();
        mView.search(query);
    }

    @Override
    public void onDestroy() {

    }
}
