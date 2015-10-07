package ds.wifimagicswitcher

import android.app.Service
import android.content.Intent
import android.os.IBinder

class Daemon : Service() {

	override fun onBind(intent: Intent?): IBinder? = null

	override fun onStart(intent: Intent?, startId: Int) {
		super.onStart(intent, startId)
	}
}