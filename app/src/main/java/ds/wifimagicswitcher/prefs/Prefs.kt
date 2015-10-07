package ds.wifimagicswitcher.prefs

public object Prefs {

	const val DEFAULT_MIN_TRESH = 40
	const val DEFAULT_DELTA_TRESH = 20

	var serviceEnabled by prefsKey(true)
	var toastsEnabled  by prefsKey(true)
	var minLevelThreshold  by prefsKey(DEFAULT_MIN_TRESH)
	var deltaLevelThreshold  by prefsKey(DEFAULT_DELTA_TRESH)
	var lastTime by prefsKey(0)
}