
package com.example.nirmol_nogori.DoorToDoor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.nirmol_nogori.Adapter.LocationAdapter;
import com.example.nirmol_nogori.R;
import com.example.nirmol_nogori.databinding.ActivityDoorToDoorLocationBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Door_to_Door_Location extends AppCompatActivity implements LocationAdapter.OnItemClickListener {
    private ActivityDoorToDoorLocationBinding binding;
    private static final String TAG = "Door_to_Door_Location";
    private RecyclerView rc_location;
    private DatabaseReference databaseReference;
    ArrayList<String> location = new ArrayList<String>();
    private LocationAdapter locationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDoorToDoorLocationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        rc_location = findViewById(R.id.location_recyclerview);
        rc_location.setFitsSystemWindows(true);
        rc_location.setLayoutManager(new LinearLayoutManager(this));
        locationAdapter = new LocationAdapter(location, this);
        rc_location.setAdapter(locationAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("Location and Cleaner");
        databaseReference.keepSynced(true);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                location.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    String key = dataSnapshot1.getKey();
                    location.add(key);
                    Log.d(TAG, "Key:" + key);
                    Log.d(TAG, "Locations:" + location);
                }
                locationAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    //for recycler views item
    @Override
    public void OnItemClick(String location_name) {
        Intent intent = new Intent(Door_to_Door_Location.this, Door_to_Door_Service.class);
        intent.putExtra("locationName", location_name);
        startActivity(intent);
        Log.d(TAG, "OnItemClick: clicked location name:" + location_name);
    }


    //TOdo add searchview 0001


}