package straightforwardapps.tellmewhenitchange;

/**
 * Created by vishal on 04-09-2018.
 */

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class AdapterClass extends RecyclerView.Adapter<AdapterClass.MyViewHolder> {


    private RecyclerViewClickListener mListener;
    private List<graphPractice> itemsList;

    AdapterClass(RecyclerViewClickListener listener)
    {
        mListener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView id, name, aU;
        private RecyclerViewClickListener mListener;

        public MyViewHolder(View view, RecyclerViewClickListener listener) {
            super(view);
            id = (TextView) view.findViewById(R.id.id);
            name = (TextView) view.findViewById(R.id.name);
            aU = (TextView) view.findViewById(R.id.amazonUrl);
            mListener = listener;
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            //System.out.println(aU.getText().toString());
            Intent httpIntent = new Intent(Intent.ACTION_VIEW);
            httpIntent.setData(Uri.parse(aU.getText().toString()));
            Context context = view.getContext();
            context.startActivity(httpIntent);
            //mListener.onClick(view, getAdapterPosition());
        }
    }


    public AdapterClass(List<graphPractice> itemsList) {
        this.itemsList = itemsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_graph_practice, parent, false);
        Context context = parent.getContext();
        return new MyViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        graphPractice graphPractice = itemsList.get(position);
        holder.id.setText(graphPractice.getID());
        holder.name.setText(graphPractice.getName());
        holder.aU.setText(graphPractice.getUrl());
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

}