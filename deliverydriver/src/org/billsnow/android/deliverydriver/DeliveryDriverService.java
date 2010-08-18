package org.billsnow.android.deliverydriver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class DeliveryDriverService extends Service {
  
  public static final int START = 0;
  public static final int END = 1;
  
  
  public void onCreate() {
    NotificationManager notifier = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    Notification statusicon = new Notification(R.drawable.status, "Shift Started", System.currentTimeMillis());
    Intent intent = new Intent(this, RunListActivity.class);
    statusicon.setLatestEventInfo(this, "Delivery Driver", 
                                  "Shift Started", PendingIntent.getActivity(this, 0, intent, 0));
    notifier.notify(START, statusicon);
  }
  
  public int onStartCommand(Intent intent, int flags, int startid) {
    return START_STICKY;
  }

  @Override
  public IBinder onBind(Intent intent) {
    // TODO Auto-generated method stub
    return null;
  }

}
