package fi.oulu.tol.linnanmaaweather;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.util.EntityUtils;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.net.http.AndroidHttpClient;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

@SuppressLint("NewApi")
public class LinnanmaaWeatherService extends Service {
	private final long seconds = 1000L;
	
	private final LinnanmaaWeatherBinder binder = new LinnanmaaWeatherBinder();
	private static final String WEATHER_URI = "http://weather.willab.fi/weather.xml";
	private String temperature = "Not available";
	
	TimerTask timerTask = new TimerTask() {
		@Override
		public void run(){
			Log.d("D", "Running timer task");
			AndroidHttpClient httpClient = AndroidHttpClient.newInstance("Linnanmaa Weather (Android Example Application)");
    		HttpUriRequest request = new HttpGet(WEATHER_URI);
			try {
				HttpResponse response = httpClient.execute(request);
				if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK){
					temperature = "Server error";
					return;
				}
				HttpEntity entity = response.getEntity();
				String content = EntityUtils.toString(entity);
				String temp = getStringRegion(content, "<tempnow unit=\"C\">", "</tempnow>");
				if (temp == null){
					temperature = "Parse error.";
					return;
				}
				temperature =  temp + " °C";
				return;
			} catch (IOException e) {
				temperature = "Connection error.";
				return;
			}
		}
	};
	Timer timer = new Timer();
	
	
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		Log.d("D", "OnCreate");
		timer.schedule(timerTask, 0L, 20 * this.seconds);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		
		timer.cancel();
	}

	@Override
	public IBinder onBind(Intent arg0) {
		Log.d("D", "on bind");
		return binder;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d("D", "on start command");
		return START_STICKY;
	}
	
	public String getTemperature() {
		Log.d("D", "getTemperature");
		return this.temperature;
	}
	

	public class LinnanmaaWeatherBinder extends Binder {
		LinnanmaaWeatherService getService() {
			return LinnanmaaWeatherService.this;
		}
	}
	
    private String getStringRegion(String string, String before, String after) {
    	try {
	    	int start = string.indexOf(before);
	    	if (start == -1)
	    		return null;
	    	start += before.length();
	    	int end = string.indexOf(after, start);
	    	end -= start;
	    	if (end == -1)
	    		return null;
	    	return string.substring(start, end);
    	} catch (IndexOutOfBoundsException exception) {
    		return null;
    	}
    }

}
