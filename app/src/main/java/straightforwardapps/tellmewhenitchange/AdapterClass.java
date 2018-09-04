package straightforwardapps.tellmewhenitchange;

/**
 * Created by vishal on 04-09-2018.
 */

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class AdapterClass extends RecyclerView.Adapter<AdapterClass.MyViewHolder> {

    private List<graphPractice> itemsList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView id, name;

        public MyViewHolder(View view) {
            super(view);
            id = (TextView) view.findViewById(R.id.id);
            name = (TextView) view.findViewById(R.id.name);
        }
    }


    public AdapterClass(List<graphPractice> itemsList) {
        this.itemsList = itemsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_graph_practice, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        graphPractice graphPractice = itemsList.get(position);
        holder.id.setText(graphPractice.getID());
        holder.name.setText(graphPractice.getName());
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }
}