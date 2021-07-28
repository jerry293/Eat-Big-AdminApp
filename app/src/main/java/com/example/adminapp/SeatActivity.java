package com.example.adminapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.adminapp.model.SeatAdapter;
import com.example.adminapp.model.SeatModel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class SeatActivity extends AppCompatActivity {

    public SeatAdapter adapter;
    public RecyclerView seat_recycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat);
        seat_recycler = findViewById(R.id.seat_RecyclerView);
        seat_recycler.setLayoutManager(new GridLayoutManager(this, 4));
        FirebaseRecyclerOptions<SeatModel> options =
                new FirebaseRecyclerOptions.Builder<SeatModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Seats"), SeatModel.class)
                        .build();


        adapter =new SeatAdapter(SeatActivity.this,options);
        seat_recycler.setAdapter(adapter);
    }

    @Override
    public void onStart() {

        super.onStart();
        adapter.startListening();


    }


    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();

    }
}