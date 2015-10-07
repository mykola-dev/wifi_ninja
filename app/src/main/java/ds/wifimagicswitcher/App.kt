package ds.wifimagicswitcher

import android.app.Application
import ds.wifimagicswitcher.di.inject
import ds.wifimagicswitcher.prefs.KotlinPrefsSetup

class App : Application() {

	override fun onCreate() {
		super.onCreate()
		instance = this
		//LeakCanary.install(this)
		KotlinPrefsSetup.init(this, "prefs")
		inject()

	}


	companion object {
		lateinit var instance: App
	}
}