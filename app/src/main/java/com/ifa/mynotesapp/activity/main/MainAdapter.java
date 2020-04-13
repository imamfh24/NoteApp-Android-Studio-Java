package com.ifa.mynotesapp.activity.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.ifa.mynotesapp.R;
import com.ifa.mynotesapp.model.Note;

import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.RecyclerViewAdapter> {

    private Context context;
    private List<Note> notes;
    private ItemCLickListener itemCLickListener;

    public MainAdapter(Context context, List<Note> notes, ItemCLickListener itemCLickListener) {
        this.context = context;
        this.notes = notes;
        this.itemCLickListener = itemCLickListener;
    }




    @NonNull
    @Override
    public RecyclerViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_note, parent,false);
        return new RecyclerViewAdapter(view, itemCLickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter holder, int position) {
        Note note = notes.get(position);
        holder.tv_title.setText(note.getTitle());
        holder.tv_note.setText(note.getNote());
        holder.tv_date.setText(note.getDate());
        holder.card_item.setCardBackgroundColor(note.getColor());
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public class RecyclerViewAdapter extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tv_title, tv_note, tv_date;
        CardView card_item;
        ItemCLickListener itemCLickListener;


        public RecyclerViewAdapter(@NonNull View itemView, ItemCLickListener itemCLickListener) {
            super(itemView);

            tv_title = itemView.findViewById(R.id.title);
            tv_note = itemView.findViewById(R.id.note);
            tv_date = itemView.findViewById(R.id.date);
            card_item = itemView.findViewById(R.id.card_item);
            this.itemCLickListener = itemCLickListener;
            card_item.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemCLickListener.onItemClick(v, getAdapterPosition());
        }
    }

    public interface ItemCLickListener {
        void onItemClick(View view, int position);
    }
}
