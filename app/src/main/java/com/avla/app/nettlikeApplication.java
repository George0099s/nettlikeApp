package com.avla.app;

import android.app.Application;

import com.github.nkzawa.socketio.client.Manager;

import java.net.URI;
import java.net.URISyntaxException;

public class nettlikeApplication extends Application {
    private Manager manager;
    {
        try {
            manager = new Manager(new URI(Constants.BASIC_URL));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public Manager getManager() {
        return manager;
    }

}
