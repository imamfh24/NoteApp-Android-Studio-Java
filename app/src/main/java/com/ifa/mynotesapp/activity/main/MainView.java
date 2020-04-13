package com.ifa.mynotesapp.activity.main;

import com.ifa.mynotesapp.model.Note;

import java.util.List;

public interface MainView {
    void showLoading();
    void hideLoading();
    void onGetResult(List<Note> notes);
    void onErrorLoading(String message);
}
