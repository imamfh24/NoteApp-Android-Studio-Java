package com.ifa.mynotesapp.activity.main;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ifa.mynotesapp.R;
import com.ifa.mynotesapp.activity.editor.EditorActivity;
import com.ifa.mynotesapp.model.Note;

import java.util.List;

public class MainActivity extends AppCompatActivity implements MainView {

    private static final int INTENT_ADD = 100;
    private static final int INTENT_EDIT = 200;

    FloatingActionButton fab;

    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefresh;

    MainPresenter presenter;
    MainAdapter adapter;
    MainAdapter.ItemCLickListener itemCLickListener; //?
    List<Note> note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        swipeRefresh = findViewById(R.id.swipe_refresh);
        recyclerView = findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fab = findViewById(R.id.add);
        fab.setOnClickListener(view -> {
            startActivityForResult(new Intent(MainActivity.this, EditorActivity.class), INTENT_ADD);
        });

        presenter = new MainPresenter(this);
        presenter.getData();

        swipeRefresh.setOnRefreshListener(
                () -> presenter.getData()
        );

        itemCLickListener = ((view, position) -> {
            int id = note.get(position).getId();
            String title = note.get(position).getTitle();
            String notes = note.get(position).getNote();
            int color = note.get(position).getColor();
            
            Intent intent = new Intent(this, EditorActivity.class);
            intent.putExtra("id", id);
            intent.putExtra("title",title);
            intent.putExtra("note", notes);
            intent.putExtra("color", color);
            startActivityForResult(intent, INTENT_EDIT);
            
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == INTENT_ADD && resultCode == RESULT_OK){
            presenter.getData(); //Reload data
        } else if (requestCode == INTENT_EDIT && resultCode == RESULT_OK) {
            presenter.getData(); //Reload dat
        }
    }

    @Override
    public void showLoading() {
        swipeRefresh.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        swipeRefresh.setRefreshing(false);
    }

    @Override
    public void onGetResult(List<Note> notes) {
        adapter = new MainAdapter(this,notes, itemCLickListener);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
        note = notes;
    }

    @Override
    public void onErrorLoading(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
