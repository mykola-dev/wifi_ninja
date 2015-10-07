package ds.wifimagicswitcher.di

import android.app.Activity
import android.content.Context
import android.net.wifi.WifiManager
import android.widget.Toast
import de.greenrobot.event.EventBus
import ds.wifimagicswitcher.App
import uy.kohesive.injekt.InjektMain
import uy.kohesive.injekt.api.*
import uy.kohesive.injekt.registry.default.DefaultRegistrar

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
