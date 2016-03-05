package ds.wifimagicswitcher

import android.app.Application
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import ds.wifimagicswitcher.di.inject
import ds.wifimagicswitcher.prefs.KotlinPrefsSetup
import io.fabric.sdk.android.Fabric

class App : Application() {

	override fun onCreate() {
		super.onCreate()
		instance = this
		//LeakCanary.install(this)
		KotlinPrefsSetup.init(this, "prefs")

		val crashlyticsCore = CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build()
		Fabric.with(this, Crashlytics.Builder().core(crashlyticsCore).build())
		inject()

	}


	companion object {
		lateinit var instance: App
	}
}