package ds.wifimagicswitcher

import android.app.Application
import com.crashlytics.android.Crashlytics
import ds.wifimagicswitcher.di.inject
import ds.wifimagicswitcher.prefs.KotlinPrefsManager
import io.fabric.sdk.android.Fabric

class App : Application() {

	override fun onCreate() {
		super.onCreate()
		instance = this
		//LeakCanary.install(this)
		KotlinPrefsManager.init(this, "prefs")
		Fabric.with(this,Crashlytics())
		inject()

	}


	companion object {
		lateinit var instance: App
	}
}