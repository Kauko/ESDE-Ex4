package fi.oulu.tol.linnanmaaweather;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import fi.oulu.tol.linnanmaaweather.LinnanmaaWeatherService.LinnanmaaWeatherBinder;

public class LinnanmaaWeatherActivity extends Activity {
	
	
	private TextView mTemperatureLabel;
	
	private LinnanmaaWeatherService mWeatherService;
	private ServiceConnection mServiceConnection = new ServiceConnection() {
		
		   @Override
		   public void onServiceConnected(ComponentName name, IBinder binder) {
			   Log.d("D", "On Service Connected");
		      LinnanmaaWeatherBinder weatherBinder = (LinnanmaaWeatherBinder)binder;
		      mWeatherService = weatherBinder.getService();

		      mTemperatureLabel.setText(mWeatherService.getTemperature());
		   }
		   @Override
		   public void onServiceDisconnected(ComponentName name) {
			   mWeatherService = null;
		   }
	};
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mTemperatureLabel = (TextView) findViewById(R.id.temperature_label);
        

        Intent intent = new Intent(this, LinnanmaaWeatherService.class);
        startService(intent);
    }
    public void onRefreshButtonClick(View view) {
    	if (mWeatherService != null){
    		Log.d("D", "Setting text");
    		mTemperatureLabel.setText(mWeatherService.getTemperature());
    	}else{
    		Log.d("D", "It's null!");
    	}
    }
    
    

    @Override
    protected void onStart() {
    	super.onStart();
    	Log.d("D", "On Start");
    	Intent intent = new Intent(this, LinnanmaaWeatherService.class);
    	bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }
 
    @Override
    protected void onStop() {
    	super.onStop();
    	if (mWeatherService != null) {
    		unbindService(mServiceConnection);
    		mWeatherService = null;
    	}
    }
}