package ds.wifimagicswitcher.bindings.viewmodel

import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import android.view.View
import cz.kinst.jakub.viewmodelbinding.ViewModel
import ds.wifimagicswitcher.databinding.MainActivityBinding
import ds.wifimagicswitcher.model.WifiResultEvent
import ds.wifimagicswitcher.prefs.Prefs
import ds.wifimagicswitcher.prefs.prefsBatch
import ds.wifimagicswitcher.utils.crouton
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import uy.kohesive.injekt.injectLazy

class MainViewModel : ViewModel<MainActivityBinding>() {

	var toolbarSubtitle: String? = null
	var log: String? = null
	var minThreshold = 0
	var deltaThreshold = 0
	var enableToasts = false
	var fabEnabled = false

	val bus by injectLazy<EventBus>()
	val wifi by injectLazy<WifiManager>()

	override fun onViewModelCreated() {
		super.onViewModelCreated()
		initUI()
	}

	override fun onViewAttached(firstAttachment: Boolean) {
		super.onViewAttached(firstAttachment)
		bus.register(this)
	}

	override fun onViewDetached(finalDetachment: Boolean) {
		super.onViewDetached(finalDetachment)
		bus.unregister(this)
	}

	fun initUI() {
		fabEnabled = Prefs.serviceEnabled
		toolbarSubtitle = "[${wifi.connectionInfo.ssid.drop(1).dropLast(1)}]"
		minThreshold = Prefs.minLevelThreshold
		deltaThreshold = Prefs.deltaLevelThreshold
		enableToasts = Prefs.toastsEnabled
		notifyChange()
	}

	fun onMinThresholdChange(value: Int) {
		Prefs.minLevelThreshold = value
	}

	fun onDeltaThresholdChange(value: Int) {
		Prefs.deltaLevelThreshold = value
	}

	fun onCheckedEnableToasts(v: View, checked: Boolean) {
		Prefs.toastsEnabled = checked
		initUI()
	}

	fun onFabClick(v: View) {
		fabEnabled = !Prefs.serviceEnabled
		Prefs.serviceEnabled = fabEnabled
		notifyChange()
		activity.crouton(if (fabEnabled) "Service Enabled" else "Service Disabled")
	}

	@Subscribe
	fun onWifiResultsEvent(e: WifiResultEvent) {
		addLogMessage(e.action)
		for (r in e.results) {
			addLogMessage("[${r.SSID}]: ${WifiManager.calculateSignalLevel(r.level, 100)}%")
		}
		addLogMessage("==============================")
	}

	@Subscribe
	fun onBestWifiEvent(result: ScanResult) {
		activity.crouton("New Best Wifi found!")
		toolbarSubtitle = "[${result.SSID}]"
		addLogMessage("SWITCHED TO [${result.SSID}]")
	}

	fun startScan() {
		wifi.startScan()
	}

	fun onRecommendedSettings() {
		prefsBatch {
			Prefs.deltaLevelThreshold = Prefs.DEFAULT_DELTA_TRESH
			Prefs.minLevelThreshold = Prefs.DEFAULT_MIN_TRESH
			Prefs.toastsEnabled = true
			Prefs.serviceEnabled = true
		}
		initUI()

	}

	private fun addLogMessage(line: String) {
		log = "$log$line\n"
	}

}