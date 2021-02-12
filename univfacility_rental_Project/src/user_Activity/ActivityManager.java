package user_Activity;

import java.util.ArrayList;

import android.app.Activity;

public class ActivityManager {

	public static ArrayList<Activity> activityManager;
	
	   //Activity�� ���� �� �߰� ���ִ� �Լ�
		public static void addActivity(Activity activity){
		    if (activityManager == null) {
		        activityManager = new ArrayList<Activity>();
		    }
		    activityManager.add(activity);
		}
		
		// ������ Activity�� ã�Ƽ� �������ִ� �Լ� (onBackPressed �Լ��� ����)
		public static void finishedActivity(Activity activity) {
		    int index = activityManager.indexOf(activity);
		    if (index >= 0) {
		         Activity at = activityManager.remove(index);
		        at.finish();
		    }
		}
		
		// Activity �̸����� Activity�� ã�ƿ��� �Լ� 
		public static Activity findActivity(String className) {
		    Activity activity = null;
		    for (Activity at : activityManager) {
		          if (className.equals(at.getClass())) {
		              activity = at;
		              break;
		         }
		    }
		    return activity;
		}
		
		// ��� Activity�� �����ϴ� �Լ� (���μ��� ���� ������ ȣ��)
		public static void finishedAllActivity() {
		    for (Activity activity : activityManager) {
		        activity.finish();
		    }
		    activityManager.clear();
		    activityManager = null;
		} 

}