package com.example.checkit_app;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class MapService extends Service {
    public MapService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}