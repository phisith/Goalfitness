package com.example.goalfitness;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


//import com.firebase.geofire.GeoFire;
//import com.firebase.geofire.GeoLocation;
import com.example.goalfitness.Model.Customer_Register;
import com.example.goalfitness.Model.ToDo;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.goalfitness.Constants.ERROR_DIALOG_REQUEST;
import static com.example.goalfitness.Constants.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION;
import static com.example.goalfitness.Constants.PERMISSIONS_REQUEST_ENABLE_GPS;

public class MainActivity extends AppCompatActivity {
    Button btnCustomer, btnPt, video;
    RelativeLayout rootLayout;
    boolean LocationPermissionGranted = false;
    private static final String TAG = "MainActivity";
    private FusedLocationProviderClient mFusedLocationProvider;
    private LocationRequest mLocationRequest;
    FirebaseAuth Auth;
    CircleImageView profile_pic;
    TextView username;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Auth = FirebaseAuth.getInstance();

        btnCustomer = (Button)findViewById(R.id.Customer);
        btnPt = (Button)findViewById(R.id.Pt);
        rootLayout = (RelativeLayout) findViewById(R.id.rootLayout);
        video = (Button) findViewById(R.id.vdo);

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

        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openvdo();
            }
        });

        mFusedLocationProvider = LocationServices.getFusedLocationProviderClient(this);





    }

    private void openvdo() {
        Intent intent = new Intent(this, TodoActivity.class);
        startActivity(intent);
    }


    // check permission for the Map services
    private boolean checkMapServices(){
        if(isServicesOK()){ // google service
            if(MapsEnabled()){ //map service
                return true;
            }
        }
        return false;
    }

    public void DialogEnableGPS() {
        final AlertDialog.Builder Dialog = new AlertDialog.Builder(this);
        Dialog.setMessage("This application need GPS to work, please enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        Intent enableGpsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(enableGpsIntent, PERMISSIONS_REQUEST_ENABLE_GPS);
                    }
                });
        final AlertDialog alert = Dialog.create();
        alert.show();
    }

    public boolean MapsEnabled(){
        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            DialogEnableGPS();
            return false;
        }
        return true;
    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationPermissionGranted = true;
            //updateLastKnowLocation();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    public boolean isServicesOK(){
        Log.d(TAG, "isServicesOK: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);

        if(available == ConnectionResult.SUCCESS){
            //everything is fine and the user can make map requests
            Log.d(TAG, "isServicesOK: Google Play Services is working");
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            //an error occured but we can resolve it
            Log.d(TAG, "isServicesOK: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }else{
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        LocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    LocationPermissionGranted = true;
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: called.");
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ENABLE_GPS: {
                if(LocationPermissionGranted){
                    //updateLastKnowLocation();
                }
                else{
                    getLocationPermission();
                }
            }
        }

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
                        startActivity(new Intent(MainActivity.this, Start.class));
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
                        startActivity(new Intent(MainActivity.this, Profile_PT.class));
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






    public void showC_RegisterDialog(){
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

                        String userid = Auth.getCurrentUser().getUid();
                        Customer_Register mCustomer_R = new Customer_Register();
                        mCustomer_R.setId(userid);
                        mCustomer_R.setEmail(tEmail.getText().toString());
                        mCustomer_R.setName(tName.getText().toString());
                        mCustomer_R.setPasssword(tPassword.getText().toString());
                        mCustomer_R.setPhone(tPhone.getText().toString());
                        mCustomer_R.setImageURL("default");
                        mCustomer_R.setSearch(tName.getText().toString().toLowerCase());
                        mCustomer_R.setBios("");

                        Task<Void> current_user_db = FirebaseDatabase.getInstance().getReference().child("Users").child("Customers").child(userid)
                                .setValue(mCustomer_R)
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
    public void showPT_RegisterDialog(){
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

                        String userid = Auth.getCurrentUser().getUid();


                        Customer_Register mPT_R = new Customer_Register();
                        mPT_R.setId(userid);
                        mPT_R.setEmail(tEmail.getText().toString());
                        mPT_R.setName(tName.getText().toString());
                        mPT_R.setPasssword(tPassword.getText().toString());
                        mPT_R.setPhone(tPhone.getText().toString());
                        mPT_R.setImageURL("default");

                        Task<Void> current_user_db = FirebaseDatabase.getInstance().getReference().child("Users").child("Personal trainer").child(userid)
                                .setValue(mPT_R)
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
    @Override
    protected void onResume() {
        super.onResume();
        if(checkMapServices()){
            if(LocationPermissionGranted){
                //updateLastKnowLocation();
            }
            else{
                getLocationPermission();
            }
        }
    }

}