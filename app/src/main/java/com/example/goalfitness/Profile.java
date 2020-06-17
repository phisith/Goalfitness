package com.example.goalfitness;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.goalfitness.Model.Customer_Register;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

import static java.security.AccessController.getContext;

public class Profile extends AppCompatActivity {
    RelativeLayout rootLayout;

    CircleImageView profile_pic;
    TextView username;

    DatabaseReference ref;
    FirebaseUser fUser;

    StorageReference mSRefer;
    private static final int IMAGE_REQUEST = 1;
    private Uri imageUri;
    private StorageTask mStorageTask;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_layout);

        rootLayout = (RelativeLayout) findViewById(R.id.rootLayout);

        profile_pic = findViewById(R.id.profile_pic);
        username = findViewById(R.id.username);

        mSRefer = FirebaseStorage.getInstance().getReference("upload");

        fUser = FirebaseAuth.getInstance().getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference().child("Users").child("Customers").child(fUser.getUid());

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Customer_Register customer_register = dataSnapshot.getValue(Customer_Register.class);
                username.setText(customer_register.getName());
                if (customer_register.getImageURL().equals("default")){
                    profile_pic.setImageResource(R.mipmap.ic_launcher);
                }
                else {
                    Glide.with(getApplicationContext()).load(customer_register.getImageURL()).into(profile_pic);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImage();
            }
        });

    }

    private void openImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUEST);
    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImage(){
        //add progressbar here //later
        if (imageUri !=null) {
            final StorageReference fileReference = mSRefer.child(System.currentTimeMillis()
                    + "." + getFileExtension(imageUri));

            mStorageTask = fileReference.putFile(imageUri);
            mStorageTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        String mUri = downloadUri.toString();

                        ref = FirebaseDatabase.getInstance().getReference().child("Users").child("Customers").child(fUser.getUid());
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("imageURL", mUri);
                        ref.updateChildren(map);

                        //progressbar finish here;
                    } else {
                        Snackbar.make(rootLayout, "Failed", Snackbar.LENGTH_LONG)
                                .show();
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Snackbar.make(rootLayout, e.getMessage(), Snackbar.LENGTH_LONG)
                            .show();
                }
            });
        }
        else{
            Snackbar.make(rootLayout,"No image selected",Snackbar.LENGTH_LONG)
                    .show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK
        && data != null && data.getData() != null){
            imageUri = data.getData();

            if (mStorageTask != null && mStorageTask.isInProgress()){
                Snackbar.make(rootLayout,"uploading",Snackbar.LENGTH_LONG)
                        .show();
            }
            else{
                uploadImage();
            }
        }
    }
}
