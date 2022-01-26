package com.vthree.rentbaseapplication.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.vthree.rentbaseapplication.Adapter.BookOrderAdapter;
import com.vthree.rentbaseapplication.Adapter.MyOrderAdapter;
import com.vthree.rentbaseapplication.ModelClass.BookingModel;
import com.vthree.rentbaseapplication.R;
import com.vthree.rentbaseapplication.preferences.PrefManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class BookedOrderActivity extends AppCompatActivity {
RecyclerView book_order_recycler;
    PrefManager prefManager;
    String user_id;
    DatabaseReference databaseReference;

    List<BookingModel> list;
    BookOrderAdapter myOrderAdapter;
    int flag=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booked_order);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Booked Order");
        book_order_recycler=findViewById(R.id.book_order_recycler);
        prefManager = new PrefManager(this);
        user_id = prefManager.getString("user_id");
        databaseReference = FirebaseDatabase.getInstance().getReference("equipmentBooking").child("data");
        book_order_recycler.hasFixedSize();
        book_order_recycler.setLayoutManager(new LinearLayoutManager(BookedOrderActivity.this));
        list=new ArrayList<>();
        myOrderAdapter=new BookOrderAdapter(list,this);
        book_order_recycler.setAdapter(myOrderAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("daa", "onresume");
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        Query key = databaseReference.child("equipmentBooking").child("data").orderByChild("book_user_id").equalTo(user_id);
        key.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    BookingModel model = snapshot.getValue(BookingModel.class);

                    if (list.size() > 0) {
                        for (int i = 0; i < list.size(); i++) {
                            if (model.getB_id().equals(list.get(i).getB_id())) {
                                flag = 1;
                                Log.d("data2", model.getEquipment_name().toString());
                            }
                        }
                        if (flag == 1) {
                        } else {
                            flag = 0;

                            list.add(model);
                            Log.d("data", model.getEquipment_name().toString());
                            Log.d("data", model.getBook_contact().toString());

                        }
                    } else {

                        list.add(model);
                        Log.d("data11", model.getEquipment_name().toString());
                        Log.d("data11", model.getBook_contact().toString());

                    }
                    // list.add(model);
                    HashSet<BookingModel> hashSet = new HashSet<BookingModel>();
                    hashSet.addAll(list);
                    list.clear();
                    list.addAll(hashSet);
                    Log.d("size1", "" + list.size());

                    myOrderAdapter.notifyDataSetChanged();

                    Log.d("daaa", model.getEquipment_name().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
