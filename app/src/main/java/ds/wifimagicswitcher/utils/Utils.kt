package ds.wifimagicswitcher.utils

import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import ds.wifimagicswitcher.App
import ds.wifimagicswitcher.WifiReceiver

fun Context.enableWifiReceiver() {
	val component = ComponentName(this, WifiReceiver::class.java)
	this.packageManager.setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP)

	//val status = this.packageManager.getComponentEnabledSetting(component)
	/*if(status == PackageManager.COMPONENT_ENABLED_STATE_ENABLED) {
		//Log.d("receiver is enabled");
	} else if(status == PackageManager.COMPONENT_ENABLED_STATE_DISABLED) {
		//Log.d("receiver is disabled");
	}
*/
}

fun Context.disableWifiReceiver() {
	val component = ComponentName(this, WifiReceiver::class.java)
	this.packageManager.setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP)
}

fun toast(text: String) = Toast.makeText(App.instance, text, 0).show()