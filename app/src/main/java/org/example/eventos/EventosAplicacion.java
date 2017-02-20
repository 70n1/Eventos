package org.example.eventos;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by AMARTIN on 20/02/2017.
 */

public class EventosAplicacion extends Application {
    static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private String ITEMS_CHILD_NAME = "eventos";
    private static DatabaseReference eventosReference;
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        EventosAplicacion.context = getApplicationContext();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.setPersistenceEnabled(true);
        eventosReference = database.getReference(ITEMS_CHILD_NAME);
    }
    public static Context getAppContext() {
        return EventosAplicacion.context;
    }
    public static DatabaseReference getItemsReference() {
        return eventosReference;
    }
}
