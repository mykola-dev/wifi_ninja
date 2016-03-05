package ds.wifimagicswitcher.di

import android.content.Context
import android.net.wifi.WifiManager
import ds.wifimagicswitcher.App
import org.greenrobot.eventbus.EventBus
import uy.kohesive.injekt.InjektMain
import uy.kohesive.injekt.api.InjektRegistrar
import uy.kohesive.injekt.api.addSingleton
import uy.kohesive.injekt.api.addSingletonFactory

fun App.inject() {
	object : InjektMain() {
		override fun InjektRegistrar.registerInjectables() {
			// app context stuff
			val ctx = this@inject
			addSingleton(ctx)
			addSingletonFactory { ctx.getSystemService(Context.WIFI_SERVICE) as WifiManager }
			addSingletonFactory{ EventBus() }
		}
	}
}
