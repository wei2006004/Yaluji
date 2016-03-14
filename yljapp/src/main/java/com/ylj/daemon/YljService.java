package com.ylj.daemon;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class YljService extends Service {
    public YljService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
