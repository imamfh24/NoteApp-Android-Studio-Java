package com.ifa.mynotesapp.activity.main;

import androidx.annotation.NonNull;

import com.ifa.mynotesapp.api.ApiClient;
import com.ifa.mynotesapp.api.ApiInterface;
import com.ifa.mynotesapp.model.Note;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainPresenter {
    private MainView view;

    public MainPresenter(MainView view) {
        this.view = view;
    }

    void getData(){
        view.showLoading();
        //Request to server
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<Note>> call = apiInterface.getNotes();
        call.enqueue(new Callback<List<Note>>() {
            @Override
            public void onResponse(@NonNull Call<List<Note>> call,@NonNull Response<List<Note>> response) {
                view.hideLoading();
                if(response.isSuccessful() && response.body() != null){
                    view.onGetResult(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Note>> call,@NonNull Throwable t) {
                view.hideLoading();
                view.onErrorLoading(t.getLocalizedMessage());
            }
        });
    }
}
