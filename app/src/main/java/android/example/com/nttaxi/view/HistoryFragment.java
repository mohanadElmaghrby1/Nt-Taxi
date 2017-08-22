package android.example.com.nttaxi.view;

import android.example.com.nttaxi.R;
import android.example.com.nttaxi.controller.HistoryAdapter;
import android.example.com.nttaxi.controller.networkController.RequestCallBack;
import android.example.com.nttaxi.controller.networkController.Service;
import android.example.com.nttaxi.model.History;
import android.example.com.nttaxi.model.Ridelist;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;


public class HistoryFragment extends Fragment {

    private ListView listView;

    private HistoryAdapter adapter;

    public HistoryFragment() {
        //Required empty public constructor

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_history, container, false);

        listView=(ListView)v.findViewById(R.id.list_view);

        Service.getRides(getContext(), new RequestCallBack() {
            @Override
            public void success(String response) {
                //this Gson parse and return base objecy contains all json response data using generic
                History baseObject = new Gson().fromJson(response ,new TypeToken<History>(){}.getType());

                int result=baseObject.getSuccess();
                if (result==1){
                    adapter = new HistoryAdapter(getContext() , (ArrayList<Ridelist>) baseObject.getRidelist());
                    listView.setAdapter(adapter);
                }

            }

            @Override
            public void error(Exception exc) {

            }
        });
        return  v;
    }

}
