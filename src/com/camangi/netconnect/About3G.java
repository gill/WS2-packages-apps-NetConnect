package com.camangi.netconnect;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class About3G {
	public TelephonyManager telManager;
	public ConnectivityManager conManager;

	public About3G(){
		
	}
	
	public About3G(Context context){
		telManager = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
	}
	
	public boolean Is3GEnabled(Context context){    
                telManager = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
		conManager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
		if (conManager == null)
	    	return false;
		
		NetworkInfo info = conManager.getActiveNetworkInfo();
		if(info ==null){
			return false;
		}
			
		if ((info.getType() == ConnectivityManager.TYPE_MOBILE)&&
			(info.isConnected())) {
			// NetWork Type: {"UNKNOWN", "GPRS", "EDGE", "UMTS", "CDMA", "EVDO 0", "EVDO A", "1xRTT", "HSDPA", "HSUPA", "HSPA"};
                        int networkType = telManager.getNetworkType();
                        if(networkType==3){
    		             return true;
                        }
                        return false;
	        }
		return false;
	}



}
