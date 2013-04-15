package fi.oulu.tol.linnanmaaweather;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class LinnanmaaWeatherService extends Service {
	private final LinnanmaaWeatherBinder binder = new LinnanmaaWeatherBinder();
	@Override
	public IBinder onBind(Intent arg0) {
		return binder;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return START_STICKY;
	}
	
	public String getTemperature() {
		return "Unknown";
	}
	

	public class LinnanmaaWeatherBinder extends Binder {
		LinnanmaaWeatherService getService() {
			return LinnanmaaWeatherService.this;
		}
	}

}
