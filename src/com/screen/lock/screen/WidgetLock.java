package com.screen.lock.screen;


import android.app.PendingIntent;
import android.app.admin.DevicePolicyManager;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.widget.RemoteViews;
import android.widget.Toast;

public class WidgetLock extends AppWidgetProvider {

	public static String ACTION_WIDGET_RECEIVER = "ActionReceiverWidget";
	private Context c;
	
	

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		// Actualizar el widget

		RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
				R.layout.widget);


		Intent active = new Intent(context, WidgetLock.class);
		active.setAction(ACTION_WIDGET_RECEIVER);

		active.putExtra("msg", "Message for Button 1");
		PendingIntent actionPendingIntent = PendingIntent.getBroadcast(context,0, active, 0);
		
		remoteViews.setOnClickPendingIntent(R.id.button_lock,
				actionPendingIntent);
		
		appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
	}

	@Override
	public void onReceive(Context context, Intent intent) {

		final String action = intent.getAction();

		 
         
		c = context;
		
		if (AppWidgetManager.ACTION_APPWIDGET_DELETED.equals(action)) {

			final int appWidgetId = intent.getExtras().getInt(
					AppWidgetManager.EXTRA_APPWIDGET_ID,
					AppWidgetManager.INVALID_APPWIDGET_ID);
			if (appWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
				this.onDeleted(context, new int[] { appWidgetId });
			}
		} else {
			// check, if our Action was called
			if (intent.getAction().equals(ACTION_WIDGET_RECEIVER)) {
				try {
					
					//se crea una política de administrador en el sistema
					DevicePolicyManager mDPM;
					mDPM = (DevicePolicyManager)context.getSystemService(Context.DEVICE_POLICY_SERVICE);
					ComponentName mAdminName = new ComponentName(context,AdminReceiver.class);
					
					Intent intent2 = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
			        intent2.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mAdminName);
			        intent2.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
			                "lock");
			        intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			        context.startActivity(intent2);
			        
			        mDPM.lockNow();
					
					
				} catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(context, context.getString(R.string.no_admin), Toast.LENGTH_LONG).show();
					Intent intent3 =  new Intent(context, TutoActivity.class);
					intent3.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					context.startActivity(intent3);
				}
				
			}
			super.onReceive(context, intent);
		}
	}
	
	
}