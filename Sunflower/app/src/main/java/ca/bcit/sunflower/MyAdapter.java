package ca.bcit.sunflower;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amazonaws.amplify.generated.graphql.ListUsersQuery;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private List<ListUsersQuery.Item> mData = new ArrayList<>();
    private LayoutInflater mInflater;


    // data is passed into the constructor
    MyAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_row, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindData(mData.get(position));
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // resets the list with a new set of data
    public void setItems(List<ListUsersQuery.Item> items) {
        mData = items;
    }

    // stores and recycles views as they are scrolled off screen
    class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_fname;
        TextView txt_lname;
        TextView txt_id;

        ViewHolder(View itemView) {
            super(itemView);
            txt_fname = itemView.findViewById(R.id.txt_fname);
            txt_lname = itemView.findViewById(R.id.txt_lname);
            txt_id = itemView.findViewById(R.id.txt_id);
        }

        void bindData(ListUsersQuery.Item item) {
            txt_id.setText(item.id());
            txt_fname.setText(item.fname());
            txt_lname.setText(item.lname());
//            txt_lastlogin.setText(item.lastlogin());
//            txt_startdate.setText(item.startdate());
//            txt_enddate.setText(item.enddate());
//            txt_misseddays.setText(item.misseddates());
//            txt_responsetype.setText(item.responsetype());
        }
    }
}
