package com.example.goalfitness.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.goalfitness.Adapter.UserAdapter;
import com.example.goalfitness.Model.Customer_Register;
import com.example.goalfitness.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class UsersView extends Fragment {

    private RecyclerView mRecyclerView;

    private UserAdapter mUserAdapter;
    private List<Customer_Register> mCustomer_Register;

    EditText search_pt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_users_view, container, false);

        mRecyclerView = view.findViewById(R.id.userlistview);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mCustomer_Register = new ArrayList<>();

        readUsers();

        search_pt = view.findViewById(R.id.search_pt);
        search_pt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchPT(s.toString().toLowerCase());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }

    private void searchPT(String toString) {

        final FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
        Query query = FirebaseDatabase.getInstance().getReference().child("Users").child("Personal trainer").orderByChild("search").startAt(toString).endAt(toString+"\uf8ff"); // search name

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mCustomer_Register.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Customer_Register customer_register = snapshot.getValue(Customer_Register.class);

                    if(!customer_register.getId().equals(fuser.getUid())){
                        mCustomer_Register.add(customer_register);
                    }
                }

                mUserAdapter = new UserAdapter(getContext(), mCustomer_Register, false, false);
                mRecyclerView.setAdapter(mUserAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void readUsers() {

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users")
                .child("Personal trainer");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (search_pt.getText().toString().equals("")) {
                    mCustomer_Register.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Customer_Register customer_register = snapshot.getValue(Customer_Register.class);

                        assert customer_register != null;
                        assert firebaseUser != null;
                        if (!customer_register.getEmail().equals(firebaseUser.getUid())) {
                            mCustomer_Register.add(customer_register);
                        }
                    }

                    mUserAdapter = new UserAdapter(getContext(), mCustomer_Register, false, false);
                    mRecyclerView.setAdapter(mUserAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
