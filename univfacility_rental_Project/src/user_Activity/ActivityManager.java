package user_Activity;

import java.util.ArrayList;

import android.app.Activity;

public class ActivityManager {

	public static ArrayList<Activity> activityManager;
	
	   //Activity의 생성 시 추가 해주는 함수
		public static void addActivity(Activity activity){
		    if (activityManager == null) {
		        activityManager = new ArrayList<Activity>();
		    }
		    activityManager.add(activity);
		}
		
		// 삭제할 Activity를 찾아서 종료해주는 함수 (onBackPressed 함수에 셋팅)
		public static void finishedActivity(Activity activity) {
		    int index = activityManager.indexOf(activity);
		    if (index >= 0) {
		         Activity at = activityManager.remove(index);
		        at.finish();
		    }
		}
		
		// Activity 이름으로 Activity를 찾아오는 함수 
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
		
		// 모든 Activity를 종료하는 함수 (프로세스 종료 시점에 호출)
		public static void finishedAllActivity() {
		    for (Activity activity : activityManager) {
		        activity.finish();
		    }
		    activityManager.clear();
		    activityManager = null;
		} 

}