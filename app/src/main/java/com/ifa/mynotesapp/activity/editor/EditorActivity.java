package com.ifa.mynotesapp.activity.editor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.ifa.mynotesapp.R;
import com.ifa.mynotesapp.api.ApiClient;
import com.ifa.mynotesapp.api.ApiInterface;
import com.ifa.mynotesapp.model.Note;
import com.thebluealliance.spectrum.SpectrumPalette;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditorActivity extends AppCompatActivity implements EditorView {

    EditText et_title, et_note;
    ProgressDialog progressDialog;
    SpectrumPalette palette;

    EditorPresenter presenter;

    int color, id;

    String title, note;
    Menu actionMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        et_title = findViewById(R.id.title);
        et_note = findViewById(R.id.note);
        palette = findViewById(R.id.palette);

        palette.setOnColorSelectedListener(
                clr -> color = clr
        );

        // Progress Dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");

        presenter = new EditorPresenter(this);

        Intent intent = getIntent();
        id = intent.getIntExtra("id",0);
        title = intent.getStringExtra("title");
        note = intent.getStringExtra("note");
        color = intent.getIntExtra("color",0);

        setDataFromIntentExtra();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_editor, menu);
        actionMenu = menu;

        if(id != 0){
            actionMenu.findItem(R.id.edit).setVisible(true);
            actionMenu.findItem(R.id.delete).setVisible(true);
            actionMenu.findItem(R.id.save).setVisible(false);
            actionMenu.findItem(R.id.update).setVisible(false);
        } else {
            actionMenu.findItem(R.id.edit).setVisible(false);
            actionMenu.findItem(R.id.delete).setVisible(false);
            actionMenu.findItem(R.id.save).setVisible(true);
            actionMenu.findItem(R.id.update).setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String title = et_title.getText().toString().trim();
        String note = et_note.getText().toString().trim();
        int color = this.color;

        switch (item.getItemId()){
            case R.id.save:
                // Save
                if(title.isEmpty()){
                    et_title.setError("Please enter a title");
                } else if (note.isEmpty()){
                    et_note.setError("Please enter a note");
                } else {
                    presenter.saveNote(title, note, color);
                }
                return true;
            case R.id.edit:
                editMode();
                actionMenu.findItem(R.id.edit).setVisible(false);
                actionMenu.findItem(R.id.delete).setVisible(false);
                actionMenu.findItem(R.id.save).setVisible(false);
                actionMenu.findItem(R.id.update).setVisible(true);
                return true;
            case R.id.update:
                if(title.isEmpty()){
                    et_title.setError("Please enter a title");
                } else if (note.isEmpty()){
                    et_note.setError("Please enter a note");
                } else {
                    presenter.updateNote(id, title, note, color);
                }
                return true;
            case R.id.delete:
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setTitle("Delete your note");
                alertDialog.setMessage("Are you sure about this?");
                alertDialog.setNegativeButton("Yes",  (dialog, which) -> {
                    dialog.dismiss();
                    presenter.deleteNote(id);
                });
                alertDialog.setPositiveButton("No", (dialog, which) -> dialog.dismiss());

                alertDialog.show();
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void showProgress() {
        progressDialog.show();
    }

    @Override
    public void hideProgress() {
        progressDialog.hide();
    }

    @Override
    public void onRequestSuccess(String message) {
        Toast.makeText(EditorActivity.this, message, Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish(); // Kembali ke main activity
    }

    @Override
    public void onRequestError(String message) {
        Toast.makeText(EditorActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private void setDataFromIntentExtra() {
        if(id != 0){
            et_title.setText(title);
            et_note.setText(note);
            palette.setSelectedColor(color);

            getSupportActionBar().setTitle("Update Note");
            readMode();
        } else {
            // Default Color Setup
            palette.setSelectedColor(getResources().getColor(R.color.white));
            color = getResources().getColor(R.color.white);
            editMode();
        }
    }

    private void editMode() {
        et_title.setFocusableInTouchMode(true);
        et_note.setFocusableInTouchMode(true);
        palette.setEnabled(true);
    }

    private void readMode() {
        et_title.setFocusableInTouchMode(false);
        et_note.setFocusableInTouchMode(false);
        et_title.setFocusable(false);
        et_note.setFocusable(false);
        palette.setEnabled(false);
    }
}
