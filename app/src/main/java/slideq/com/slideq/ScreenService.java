package slideq.com.slideq;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

public class ScreenService extends Service {

    private ScreenReceiver mReceiver = null;
    private final static String TAG = "Notification";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel notificationChannel =
                    new NotificationChannel(
                            "channel_id",
                            "test",
                            NotificationManager.IMPORTANCE_DEFAULT
                    );
            notificationChannel.setDescription("notification");
            notificationManager.createNotificationChannel(notificationChannel);


        }
        //else
        //startForeground(1, new Notification());

        mReceiver = new ScreenReceiver();
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        registerReceiver(mReceiver, filter);


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        super.onStartCommand(intent, flags, startId);
        //Notification notification = new Notification(R.drawable.ic_launcher_foreground, "서비스 실행됨", System.currentTimeMillis());
        //notification.setLatestEventInfo(getApplicationContext(), "Screen Service", "Foreground로 실행됨", null);
        //startForeground(1, notification);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "channel_id");
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("SlideQ is running in background")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(2, notification);

        if(intent != null){
            if(intent.getAction()==null){
                if(mReceiver==null){
                    mReceiver = new ScreenReceiver();
                    IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
                    registerReceiver(mReceiver, filter);
                }
            }
        }
        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

        if(mReceiver != null){
            unregisterReceiver(mReceiver);
        }

    }

    public void registerRestartAlarm(boolean isOn){
        Intent intent = new Intent(ScreenService.this, RestartReceiver.class);
        intent.setAction(RestartReceiver.ACTION_RESTART_SERVICE);
        PendingIntent sender = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);

        AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
        if(isOn){
            am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 1000, 3000000, sender);
        }else{
            am.cancel(sender);
        }
    }

}