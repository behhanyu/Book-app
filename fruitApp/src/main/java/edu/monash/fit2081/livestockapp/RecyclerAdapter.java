package edu.monash.fit2081.livestockapp;

import com.google.android.material.snackbar.Snackbar;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    ArrayList<FruitItem> data;


    public RecyclerAdapter(ArrayList<FruitItem> _data) {
        super();
        data = _data;
        Log.d("stock", "got data with size=" + _data.size());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_layout, viewGroup, false); //CardView inflated as RecyclerView list item
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Log.d("stock", "Bind a view for pos" + position);
        viewHolder.itemTitle.setText(data.get(position).getItemTitle());
        viewHolder.itemFamily.setText(data.get(position).getItemFamily());
        viewHolder.itemCalories.setText(data.get(position).getItemCalories());
        viewHolder.itemSugar.setText(data.get(position).getItemSugar());
        viewHolder.itemCarbs.setText(data.get(position).getItemCarbs());
        viewHolder.itemProtein.setText(data.get(position).getItemProtein());



        //a class declared in a method (so called local or anonymous class can only access the method's local variables if they are declared final (1.8 or are effectively final)
        //this has to do with Java closures
        // see https://docs.oracle.com/javase/tutorial/java/javaOO/localclasses.html and https://docs.oracle.com/javase/tutorial/java/javaOO/anonymousclasses.html
        final int fPosition = position;
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() { //set back to itemView for students
            @Override
            public void onClick(View v) {

                Snackbar.make(v, "Item at position " + fPosition + " was clicked!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public View itemView;
        public TextView itemTitle;
        public TextView itemFamily;
        public TextView itemCalories;
        public TextView itemSugar;
        public TextView itemCarbs;
        public TextView itemProtein;




        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            itemTitle = itemView.findViewById(R.id.item_title);
            itemFamily = itemView.findViewById(R.id.item_family);
            itemCalories = itemView.findViewById(R.id.item_calories);
            itemSugar = itemView.findViewById(R.id.item_sugar);
            itemCarbs = itemView.findViewById(R.id.item_carbs);
            itemProtein = itemView.findViewById(R.id.item_protein);

        }
    }


}
