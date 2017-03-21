package frc3824.databaserelay;

import android.app.Application;
import android.content.Context;

import frc3824.databaserelay.Comms.ServerConnection;

/**
 * @author frc3824
 * Created: 3/20/17
 */

public class AppContext extends Application{
    private static AppContext app;

    private ServerConnection sc;

    public AppContext() {
        super();
        app = this;
    }

    public static Context getDefaultContext() {
        return app.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sc = ServerConnection.getInstance(getDefaultContext());
        sc.start();
    }

    public static ServerConnection getServerConnection() {
        return app.sc;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        sc.stop();
    }

}
