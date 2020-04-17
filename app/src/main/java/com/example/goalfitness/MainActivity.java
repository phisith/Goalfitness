package com.example.goalfitness;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    Button btnCustomer, btnPt;
    Button btnLogin, btnRegister;
    RelativeLayout rootLayout;

    //EditText tEmail, tPassword;

    FirebaseAuth Auth;
    //FirebaseDatabase db;
    //DatabaseReference users_refer;
    //FirebaseAuth.AuthStateListener firebaseAuthListener;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Auth = FirebaseAuth.getInstance();
        //db = FirebaseDatabase.getInstance();


        Auth = FirebaseAuth.getInstance();
        //firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
        //    @Override
        //    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        //        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        //        if (user != null) {
        //            Intent intent = new Intent(MainActivity.this, Welcome.class);
        //            startActivity(intent);
        //            finish();
        //            return;
        //        }

        //    }
        //};

        btnCustomer = (Button)findViewById(R.id.Customer);
        btnPt = (Button)findViewById(R.id.Pt);
        rootLayout = (RelativeLayout) findViewById(R.id.rootLayout);





        btnCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showC_LoginDialog();
            }
        });

        btnPt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPT_LoginDialog();
            }
        });



    }
    public void showC_LoginDialog(){
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Welcome to Goalfitness");
        dialog.setMessage("Please use your email to login");

        LayoutInflater inflater = LayoutInflater.from(this);
        View login_layout = inflater.inflate(R.layout.activity_login,null);

        final EditText tEmail = login_layout.findViewById(R.id.Email);
        final EditText tPassword = login_layout.findViewById(R.id.Password);

        dialog.setView(login_layout);

        dialog.setNegativeButton("Login", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(TextUtils.isEmpty(tEmail.getText().toString())) {
                    Snackbar.make(rootLayout,"Please fill up your email",Snackbar.LENGTH_LONG)
                            .show();
                    return;
                }

                if(TextUtils.isEmpty(tPassword.getText().toString())) {
                    Snackbar.make(rootLayout,"Please fill up your password",Snackbar.LENGTH_LONG)
                            .show();
                    return;
                }


                final String Email = tEmail.getText().toString();
                final String Password = tPassword.getText().toString();

                Auth.signInWithEmailAndPassword(Email,Password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        startActivity(new Intent(MainActivity.this, Menu.class));
                        finish();
                    }
                });

            }
        });

        dialog.setPositiveButton("Register", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showC_RegisterDialog();

           }
        });

        dialog.show();

    }

    //Personal trainer login dialog
    public void showPT_LoginDialog(){
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Welcome to Goalfitness");
        dialog.setMessage("Please use your email to login");

        LayoutInflater inflater = LayoutInflater.from(this);
        View login_layout = inflater.inflate(R.layout.activity_login,null);

        final EditText tEmail = login_layout.findViewById(R.id.Email);
        final EditText tPassword = login_layout.findViewById(R.id.Password);

        dialog.setView(login_layout);

        dialog.setNegativeButton("Login", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(TextUtils.isEmpty(tEmail.getText().toString())) {
                    Snackbar.make(rootLayout,"Please fill up your email",Snackbar.LENGTH_LONG)
                            .show();
                    return;
                }

                if(TextUtils.isEmpty(tPassword.getText().toString())) {
                    Snackbar.make(rootLayout,"Please fill up your password",Snackbar.LENGTH_LONG)
                            .show();
                    return;
                }


                final String Email = tEmail.getText().toString();
                final String Password = tPassword.getText().toString();

                Auth.signInWithEmailAndPassword(Email,Password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        startActivity(new Intent(MainActivity.this, Menu.class));
                        finish();
                    }
                });

            }
        });

        dialog.setPositiveButton("Register", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showPT_RegisterDialog();

            }
        });

        dialog.show();

    }






    private void showC_RegisterDialog(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Welcome to Goal fitness");
        dialog.setMessage("Please use your email to register");

        LayoutInflater inflater = LayoutInflater.from(this);
        final View customer_register_layout = inflater.inflate(R.layout.layout_customer_register,null);

        final EditText tEmail = customer_register_layout.findViewById(R.id.regisEmail);
        final EditText tPassword = customer_register_layout.findViewById(R.id.regisPassowrd);
        final EditText tPhone = customer_register_layout.findViewById(R.id.regisPhone);
        final EditText tName = customer_register_layout.findViewById(R.id.regisName);

        dialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                if(TextUtils.isEmpty(tEmail.getText().toString())) {
                    Snackbar.make(rootLayout,"Please fill up your email",Snackbar.LENGTH_LONG)
                            .show();
                    return;
                }

                if(TextUtils.isEmpty(tPassword.getText().toString())) {
                    Snackbar.make(rootLayout,"Please fill up your password",Snackbar.LENGTH_LONG)
                            .show();
                    return;
                }
                if(TextUtils.isEmpty(tPhone.getText().toString())) {
                    Snackbar.make(rootLayout,"Please fill up your Phone number",Snackbar.LENGTH_LONG)
                            .show();
                    return;
                }
                if(TextUtils.isEmpty(tName.getText().toString())) {
                    Snackbar.make(rootLayout,"Please fill up your Name",Snackbar.LENGTH_LONG)
                            .show();
                    return;
                }
                if(tPassword.getText().toString().length() < 7) {
                    Snackbar.make(rootLayout,"Your Password is too short, please fill up again",Snackbar.LENGTH_LONG)
                            .show();
                    return;
                }

                final String Email = tEmail.getText().toString();
                final String Name = tName.getText().toString();
                final String Password = tPassword.getText().toString();
                final String Phone = tPhone.getText().toString();
                Auth.createUserWithEmailAndPassword(Email, Password) .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        //Users_local user = new Users_local();
                        //user.setEmail(Email);
                        //user.setName(Name);
                        //user.setPassword(Password);
                        //user.setPhone(Phone);
                        String user_id = Auth.getCurrentUser().getUid();
                        DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("Users").child("Customers").child(user_id);

                        Map newPost = new HashMap();
                        newPost.put("Email", Email);
                        newPost.put("Name", Name);
                        newPost.put("Password", Password);
                        newPost.put("Phone", Phone);

                        current_user_db.setValue(newPost)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Snackbar.make(rootLayout,"Register Success!", Snackbar.LENGTH_LONG)
                                                .show();

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Snackbar.make(rootLayout,"System failed, please try again"+e.getMessage(),Snackbar.LENGTH_LONG)
                                                .show();
                                    }
                                });


                        //users_refer = db.getReference().child("Users").child("Customers").child(String.valueOf(users_refer));
                        //users_refer.child(Auth.getCurrentUser().getUid())
                        //        .setValue(user)
                        //        .addOnSuccessListener(new OnSuccessListener<Void>() {
                        //            @Override
                        //           public void onSuccess(Void aVoid) {
                        //               Snackbar.make(rootLayout, "Register success", Snackbar.LENGTH_SHORT)
                        //                        .show();
                                  //  }
                                //});
                        //users_refer = FirebaseAuth.getInstance().getCurrentUser().getUid();

                        //FirebaseAuth.getInstance().getCurrentUser().getUid()
                        //db.getReference().child("Users").child("Customer").child(users_refer);




                    }
                });



            }
        });
        dialog.setNegativeButton("Back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showC_LoginDialog();
            }
        });

        dialog.setView(customer_register_layout);
        dialog.show();



    }

    private void showPT_RegisterDialog(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Welcome to Goal fitness");
        dialog.setMessage("Please use your email to register");

        LayoutInflater inflater = LayoutInflater.from(this);
        final View Pt_register_layout = inflater.inflate(R.layout.layout_customer_register,null);

        final EditText tEmail = Pt_register_layout.findViewById(R.id.regisEmail);
        final EditText tPassword = Pt_register_layout.findViewById(R.id.regisPassowrd);
        final EditText tPhone = Pt_register_layout.findViewById(R.id.regisPhone);
        final EditText tName = Pt_register_layout.findViewById(R.id.regisName);

        dialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                if(TextUtils.isEmpty(tEmail.getText().toString())) {
                    Snackbar.make(rootLayout,"Please fill up your email",Snackbar.LENGTH_LONG)
                            .show();
                    return;
                }

                if(TextUtils.isEmpty(tPassword.getText().toString())) {
                    Snackbar.make(rootLayout,"Please fill up your password",Snackbar.LENGTH_LONG)
                            .show();
                    return;
                }
                if(TextUtils.isEmpty(tPhone.getText().toString())) {
                    Snackbar.make(rootLayout,"Please fill up your Phone number",Snackbar.LENGTH_LONG)
                            .show();
                    return;
                }
                if(TextUtils.isEmpty(tName.getText().toString())) {
                    Snackbar.make(rootLayout,"Please fill up your Name",Snackbar.LENGTH_LONG)
                            .show();
                    return;
                }
                if(tPassword.getText().toString().length() < 7) {
                    Snackbar.make(rootLayout,"Your Password is too short, please fill up again",Snackbar.LENGTH_LONG)
                            .show();
                    return;
                }

                final String Email = tEmail.getText().toString();
                final String Name = tName.getText().toString();
                final String Password = tPassword.getText().toString();
                final String Phone = tPhone.getText().toString();
                Auth.createUserWithEmailAndPassword(Email, Password) .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        //Users_local user = new Users_local();
                        //user.setEmail(Email);
                        //user.setName(Name);
                        //user.setPassword(Password);
                        //user.setPhone(Phone);
                        String user_id = Auth.getCurrentUser().getUid();
                        DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("Users").child("Personal trainer").child(user_id);

                        Map newPost = new HashMap();
                        newPost.put("Email", Email);
                        newPost.put("Name", Name);
                        newPost.put("Password", Password);
                        newPost.put("Phone", Phone);

                        current_user_db.setValue(newPost)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Snackbar.make(rootLayout,"Register Success!", Snackbar.LENGTH_LONG)
                                                .show();

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Snackbar.make(rootLayout,"System failed, please try again"+e.getMessage(),Snackbar.LENGTH_LONG)
                                                .show();
                                    }
                                });


                        //users_refer = db.getReference().child("Users").child("Customers").child(String.valueOf(users_refer));
                        //users_refer.child(Auth.getCurrentUser().getUid())
                        //        .setValue(user)
                        //        .addOnSuccessListener(new OnSuccessListener<Void>() {
                        //            @Override
                        //           public void onSuccess(Void aVoid) {
                        //               Snackbar.make(rootLayout, "Register success", Snackbar.LENGTH_SHORT)
                        //                        .show();
                        //  }
                        //});
                        //users_refer = FirebaseAuth.getInstance().getCurrentUser().getUid();

                        //FirebaseAuth.getInstance().getCurrentUser().getUid()
                        //db.getReference().child("Users").child("Customer").child(users_refer);




                    }
                });



            }
        });
        dialog.setNegativeButton("Back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showC_LoginDialog();
            }
        });

        dialog.setView(Pt_register_layout);
        dialog.show();



    }

}