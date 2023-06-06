package com.fit2081.smstokenizer_w5;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fit2081.smstokenizer_w5.provider.Book;

import java.util.ArrayList;
import java.util.List;

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.ViewHolder>{

    List<Book> data = new ArrayList<>();

    public void MyRecyclerAdapter(){
    }
    public void setData(List<Book> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false); //CardView inflated as RecyclerView list item
        ViewHolder viewHolder = new ViewHolder(v);
        Log.d("week6App","onCreateViewHolder");
        return viewHolder;
        }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bookidTv.setText("Book ID: " + data.get(position).getBookId());
        holder.titleTv.setText(data.get(position).getTitle());
        holder.isbnTv.setText("ISBN: " + data.get(position).getIsbn());
        holder.authorTv.setText(data.get(position).getAuthor());
        holder.descriptionTv.setText("Description: " + data.get(position).getDescription());
        holder.priceTv.setText("RM" + data.get(position).getPrice());

        Log.d("week6App","onBindViewHolder");

        }

    @Override
    public int getItemCount() {
        return data.size();
        }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView bookidTv, titleTv, isbnTv, authorTv, descriptionTv, priceTv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bookidTv = itemView.findViewById(R.id.bookid_id);
            titleTv = itemView.findViewById(R.id.title_id);
            isbnTv = itemView.findViewById(R.id.isbn_id);
            authorTv = itemView.findViewById(R.id.author_id);
            descriptionTv = itemView.findViewById(R.id.description_id);
            priceTv = itemView.findViewById(R.id.price_id);

        }
    }
}
