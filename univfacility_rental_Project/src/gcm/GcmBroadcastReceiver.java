package gcm;
import com.example.andproject.R;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.Vibrator;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

public class GcmBroadcastReceiver extends WakefulBroadcastReceiver{
	


	@Override
	public void onReceive(Context context, Intent intent)
	   {
	      Log.i("GcmBroadcastReceiver.java | onReceive", "|" + "================="+"|");
	      Bundle bundle = intent.getExtras();
	      for (String key : bundle.keySet())
	      {
	         Object value = bundle.get(key);
	         Log.i("GcmBroadcastReceiver.java | onReceive", "|" + String.format("%s : %s (%s)", key, value.toString(), value.getClass().getName()) + "|");
	      }
	      Log.i("GcmBroadcastReceiver.java | onReceive", "|" + "================="+"|");
	 
	      // Explicitly specify that GcmIntentService will handle the intent.
	      ComponentName comp = new ComponentName(context.getPackageName(), GcmIntentService.class.getName());
	      // Start the service, keeping the device awake while it is launching.
	      startWakefulService(context, intent.setComponent(comp));
	    //  Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
	   // 밀리 세컨드 단위로 진동시간 설정 
	      Vibrator vibe = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
	   // 밀리 세컨드 단위로 진동시간 설정 
	   vibe.vibrate(500);
	   MediaPlayer mp= MediaPlayer.create(context, R.raw.hangouts_incoming_call);
	   mp.start();
	   PowerManager pm = (PowerManager)context.getSystemService(Context.POWER_SERVICE);
	   boolean isScreenOn = pm.isScreenOn();
	   Log.e("screen on.................................", ""+isScreenOn);

	   if(isScreenOn==false)       
	      {
	        WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK |PowerManager.ACQUIRE_CAUSES_WAKEUP |PowerManager.ON_AFTER_RELEASE,"MyLock");                            
	        wl.acquire(10000);
	        WakeLock wl_cpu = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"MyCpuLock");
	        wl_cpu.acquire(10000);
	      }



	      setResultCode(Activity.RESULT_OK);
	   }

}
