package android.example.com.nttaxi.controller;

import android.content.Context;
import android.example.com.nttaxi.R;
import android.example.com.nttaxi.model.Ridelist;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by mohanad on 10/08/17.
 */

public class HistoryAdapter extends ArrayAdapter<Ridelist> {

    public HistoryAdapter(@NonNull Context context , @NonNull ArrayList<Ridelist> places ) {
        super(context, 0, places);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.history_list_item, parent, false);
        }

        //get the recipe object
        final Ridelist ride = getItem(position);

        TextView from = (TextView)listItemView.findViewById(R.id.txt_from);
        TextView to = (TextView)listItemView.findViewById(R.id.txt_to);
        TextView driver_name = (TextView)listItemView.findViewById(R.id.txt_driver_name);
        TextView status = (TextView)listItemView.findViewById(R.id.txt_status);

        from.setText(ride.getLocation());
        to.setText(ride.getDroplocation());
        driver_name.setText(ride.getDriverName());
        status.setText(ride.getAccept());

        return  listItemView;
    }
}
