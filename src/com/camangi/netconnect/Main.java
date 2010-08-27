package com.camangi.netconnect;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.view.KeyEvent;

public class Main extends Activity {

	public int ProcessType = 0;
	public int waitTime = 15;
 


	public String[] ConnectType;
	public AboutWiFi aboutWiFi;
	public ProgressDialog waitFindAP;
	final Handler myHandler = new Handler();

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		aboutWiFi = new AboutWiFi(this);
		ConnectType = getResources().getStringArray(R.array.ConnectType);

		About3G about3g = new About3G(this);

                
		// Step(1) check 3g
		if (about3g.Is3GEnabled(Main.this)) {
			aboutWiFi.SettingWiFiEnable(false);
			ProcessType = 1;
			myHandler.post(Finished);
		} else {
			// Step(2)
			if (aboutWiFi.isWifiEnabled(this)) {
				ProcessType = 2;
				myHandler.post(Finished);
			} else {


				     /*
				      * Step(3)
				      * Show the progress dialog in a thread. If WiFi doesn't connected
				      * after waitTime seconds, it means there is no way to connect
				      * to network. Then, this application will list all the enabled ap.
				     */

				     aboutWiFi.SettingWiFiEnable(true);
				
		   		     String waitTitle = getString(R.string.waitTitle);
				     String waitContent = getString(R.string.waitContent);
				     waitFindAP = ProgressDialog.show(this, waitTitle, waitContent,
				  		true);

				     RunThread_FindAP FindOneAP_Thread = new RunThread_FindAP();
				     FindOneAP_Thread.start();

			}

		}

	}

	// step(3) Find a enabled WiFi ap.
	public class RunThread_FindAP extends Thread {
		
		public void run() {
			try {
				for (int i = 0; i < waitTime; i++) {
					if (aboutWiFi.isWifiEnabled(Main.this)) {
						ProcessType = 2;
						break;
					}
					sleep(1000);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				waitFindAP.dismiss();
				// Step(4) show all the  AP list
				if (ProcessType == 0) {
					myHandler.post(ListAP);
				}else{
					myHandler.post(Finished);
				}

			}
		}
	}

	// step(4) Find all enabled WiFi ap.
	public class RunThread_WifiSetting extends Thread {
		
		public void run() {
			try {
                                while(true){
					if (aboutWiFi.isWifiEnabled(Main.this)) {
						ProcessType = 2;
						break;
					}
					sleep(1000);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				myHandler.post(Finished);

			}
		}
	}


	final Runnable ListAP = new Runnable() {
		public void run() {
                                 //Ask the user set the wif
                                 AlertDialog.Builder builder = new AlertDialog.Builder(Main.this);
                                 builder.setTitle(R.string.wifi_title);
                                 builder.setMessage(R.string.wifi_message);
                                 builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                   public void onClick(DialogInterface dialog, int which) {
			             aboutWiFi.ListAllAP(Main.this);
		                     RunThread_WifiSetting WifiSetting_Thread = new RunThread_WifiSetting();
			             WifiSetting_Thread.start();
                                   }
                                 });
                               
                                 builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                   public void onClick(DialogInterface dialog, int which) {
                                       finish();
                                   }
                                 });
 
                                 builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                         	   public void onCancel(DialogInterface dialog) {
                                       finish();
                                   }
        		         }); 
             
                                builder.create().show();
		}
	};
	
	final Runnable Finished = new Runnable() {
		public void run() {

			// finally step and close this application
			if (ProcessType == 0) {
				RunAndToast(false);
			} else {
				RunAndToast(true);
			}
			finish();
		}
	};


	public void RunAndToast(boolean run) {
		Toast.makeText(this, ConnectType[ProcessType], Toast.LENGTH_LONG)
				.show();
                setResult(RESULT_OK, (new Intent()).putExtra("net_OK", run));
	}
       
        public void onDestroy() {
          super.onDestroy();
          if(ProcessType ==0){
	    aboutWiFi.SettingWiFiEnable(aboutWiFi.OriginalWiFiCheck);
          }
        }

        public void onRestart() {
          super.onRestart();
          if(ProcessType ==0){
	    aboutWiFi.SettingWiFiEnable(aboutWiFi.OriginalWiFiCheck);
            finish();
          }
        }

}
