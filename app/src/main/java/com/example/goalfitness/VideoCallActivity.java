package com.example.goalfitness;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.opentok.android.OpentokError;
import com.opentok.android.Publisher;
import com.opentok.android.PublisherKit;
import com.opentok.android.Session;
import com.opentok.android.Stream;
import com.opentok.android.Subscriber;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class VideoCallActivity extends AppCompatActivity implements  Session.SessionListener, PublisherKit.PublisherListener {

    private static String API_KEY="46792684";
    private static String SESSION_ID="2_MX40Njc5MjY4NH5-MTU5MjIzMjMwNDEzMH5DYUp0SWYxb25zN3hiZnhvWjJKQ1ZuZVN-fg";
    private static String TOKEN="T1==cGFydG5lcl9pZD00Njc5MjY4NCZzaWc9ZGUwMWI3MmUyMTRiZDc3MWEzOTljMDRiMDdlNDJmYWMxMzI4YzA2ZTpzZXNzaW9uX2lkPTJfTVg0ME5qYzVNalk0Tkg1LU1UVTVNakl6TWpNd05ERXpNSDVEWVVwMFNXWXhiMjV6TjNoaVpuaHZXakpLUTFadVpWTi1mZyZjcmVhdGVfdGltZT0xNTkyMjMyMzMxJm5vbmNlPTAuNzcxODU5NTA4MTY1MTI4OSZyb2xlPXB1Ymxpc2hlciZleHBpcmVfdGltZT0xNTkyMjM1OTMxJmluaXRpYWxfbGF5b3V0X2NsYXNzX2xpc3Q9";
    private static String LOG_TAG= MainActivity.class.getSimpleName();
    private static final int RC_SETTINGS = 123;

    private Session session;
    private FrameLayout PublisherContainer;
    private FrameLayout SubscriberContainer;

    private Publisher publisher;
    private Subscriber subscriber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_call);
        requestPermission();

        PublisherContainer = (FrameLayout) findViewById(R.id.publisher_container);
        SubscriberContainer = (FrameLayout) findViewById(R.id.subscriber_container);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,this);
    }

    @AfterPermissionGranted(RC_SETTINGS)
    private void requestPermission(){
        String[] perm = {Manifest.permission.INTERNET, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO};
        if(EasyPermissions.hasPermissions(this,perm)){
            session = new Session.Builder(this,API_KEY,SESSION_ID).build();
            session.setSessionListener(this);
            session.connect(TOKEN);
        }
        else{
            EasyPermissions.requestPermissions(this, "this app need to acccess your camera and mic", RC_SETTINGS,perm);
        }
    }




    @Override
    public void onConnected(Session session) {

        publisher = new Publisher.Builder(this).build();
        publisher.setPublisherListener(this);

        PublisherContainer.addView(publisher.getView());
        session.publish(publisher);

    }

    @Override
    public void onDisconnected(Session session) {

    }

    @Override
    public void onStreamReceived(Session session, Stream stream) {

        if(subscriber == null){
            subscriber = new Subscriber.Builder(this, stream).build();
            session.subscribe(subscriber);
            SubscriberContainer.addView(subscriber.getView());
        }

    }

    @Override
    public void onStreamDropped(Session session, Stream stream) {

        if (subscriber != null){
            subscriber= null;
            SubscriberContainer.removeAllViews();
        }

    }

    @Override
    public void onError(Session session, OpentokError opentokError) {

    }

    @Override
    public void onStreamCreated(PublisherKit publisherKit, Stream stream) {

    }

    @Override
    public void onStreamDestroyed(PublisherKit publisherKit, Stream stream) {

    }

    @Override
    public void onError(PublisherKit publisherKit, OpentokError opentokError) {

    }
}
