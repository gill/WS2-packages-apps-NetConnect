package com.camangi.netconnect;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;

public class AboutWiFi{
	
	public WifiManager wifiManager;
	public ConnectivityManager conManager;
	public boolean OriginalWiFiCheck;

	public AboutWiFi(){	
	}	
	public AboutWiFi(Context context){
		wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		OriginalWiFiCheck = wifiManager.isWifiEnabled();
	}	
	
	public void SettingWiFiEnable(boolean checked){
		wifiManager.setWifiEnabled(checked);
	}
	
	public boolean isWifiEnabled(Context context){
		conManager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
		if (conManager == null)
	    	return false;
		
		NetworkInfo info = conManager.getActiveNetworkInfo();
		if(info ==null){
			return false;
		}
			
		if ((info.getType() == ConnectivityManager.TYPE_WIFI)&&
			(info.isConnected())) {
			return true;
		}
    	
		return false;
	}
	
	
	public void ListAllAP(Context context){	
		//Intent it = new Intent();
		//it.setComponent(new ComponentName("com.android.settings", "com.android.settings.wifi.WifiSettings"));
		//context.startActivity(it);

		Intent it = new Intent("com.camangi.netconnect.ListAP");
                context.startActivity(it);
	}
}
