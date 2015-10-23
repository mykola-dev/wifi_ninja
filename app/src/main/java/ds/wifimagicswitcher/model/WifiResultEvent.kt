package ds.wifimagicswitcher.model

import android.net.wifi.ScanResult

data class WifiResultEvent(
	val action: String,
	val results: List<ScanResult>
)