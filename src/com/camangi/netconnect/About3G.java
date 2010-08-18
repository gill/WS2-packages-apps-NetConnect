package com.camangi.netconnect;

import android.content.Context;
import android.telephony.TelephonyManager;

public class About3G {
	public TelephonyManager telManager;
	
	public About3G(){
		
	}
	
	public About3G(Context context){
		telManager = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
	}
	
	public boolean Is3GEnabled(){       
        // NetWork Type: {"UNKNOWN", "GPRS", "EDGE", "UMTS", "CDMA", "EVDO 0", "EVDO A", "1xRTT", "HSDPA", "HSUPA", "HSPA"};
        int networkType = telManager.getNetworkType();
        if(networkType==3){
    		return true;
        }

        return false;
        
	}
}
