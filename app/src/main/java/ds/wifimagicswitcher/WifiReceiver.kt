package ds.wifimagicswitcher

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import de.greenrobot.event.EventBus
import ds.wifimagicswitcher.prefs.Prefs
import ds.wifimagicswitcher.utils.L
import ds.wifimagicswitcher.utils.toast
import uy.kohesive.injekt.injectLazy

const val MAX_LEVEL: Int = 100

class WifiReceiver : BroadcastReceiver() {


	val wifi: WifiManager by injectLazy()
	val bus by injectLazy<EventBus>()

	override fun onReceive(context: Context, intent: Intent) {
		if (!Prefs.serviceEnabled || !wifi.isWifiEnabled)
			return

		L.v("====> action=${intent.action}")
		L.v("scan results:")
		wifi.scanResults.forEachIndexed { i, it ->
			val level = WifiManager.calculateSignalLevel(it.level, MAX_LEVEL)
			L.v("$i: SSID=${it.SSID} BSSID=${it.BSSID} level=$level ${it.capabilities}")
		}
		L.v("configured networks:")
		wifi.configuredNetworks.forEachIndexed { i, it ->
			L.v("$i: SSID=${it.SSID} id=${it.networkId}")
		}

		bus.post(wifi.scanResults)

		val best = getBetterSpot(context)
		if (best != null) {
			val config = wifi.configuredNetworks.firstOrNull { best.SSID == it.SSID.drop(1).dropLast(1) }
			if (config != null) {
				if (wifi.enableNetwork(config.networkId, true)) {
					if (Prefs.toastsEnabled)
						toast("Connected to: ${best.SSID}")
					bus.post(best)
				} else
					if (Prefs.toastsEnabled)
						toast("Failed to connect to ${best.SSID}")
			} else {
				L.e("skipped. unconfigured network: ${best.SSID}")
			}
		}
	}

	private fun getBetterSpot(ctx: Context): ScanResult? {
		val currSpot = wifi.connectionInfo.ssid
		L.v("curr spot=$currSpot")

		val best = wifi.scanResults.maxBy { it.level } ?: return null

		// check thresholds
		val minThreshold = Prefs.minLevelThreshold
		val deltaThreshold = Prefs.deltaLevelThreshold
		val newLevel = WifiManager.calculateSignalLevel(best.level, MAX_LEVEL)
		val oldLevel = WifiManager.calculateSignalLevel(wifi.connectionInfo.rssi, MAX_LEVEL)
		if (oldLevel < minThreshold && deltaThreshold < newLevel - oldLevel) {
			L.i("best spot: ${best.SSID}")
			return best
		}

		return null
	}

	private fun isConfiguredNetwork(scan: ScanResult): Boolean = wifi.configuredNetworks.any { scan.BSSID == it.BSSID }
}