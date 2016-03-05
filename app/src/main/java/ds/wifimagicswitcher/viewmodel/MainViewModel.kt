package ds.wifimagicswitcher.viewmodel

import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.databinding.ObservableInt
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import android.view.View
import cz.kinst.jakub.viewmodelbinding.ViewModel
import ds.wifimagicswitcher.App
import ds.wifimagicswitcher.R
import ds.wifimagicswitcher.databinding.MainActivityBinding
import ds.wifimagicswitcher.model.WifiResultEvent
import ds.wifimagicswitcher.prefs.Prefs
import ds.wifimagicswitcher.prefs.prefsBatch
import ds.wifimagicswitcher.utils.crouton
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import uy.kohesive.injekt.injectLazy

class MainViewModel : ViewModel<MainActivityBinding>() {

	var toolbarSubtitle = ObservableField<String>()
	var log = ObservableField<String>()
	var minThreshold = ObservableInt()
	var deltaThreshold = ObservableInt()
	var enableToasts = ObservableBoolean()

	val app by injectLazy<App>()
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
		toggleFab(Prefs.serviceEnabled)
		toolbarSubtitle.set("[${wifi.connectionInfo.ssid.drop(1).dropLast(1)}]")
		minThreshold.set(Prefs.minLevelThreshold)
		deltaThreshold.set(Prefs.deltaLevelThreshold)
		enableToasts.set(Prefs.toastsEnabled)
	}

	fun onMinThresholdChange(value: Int) {
		Prefs.minLevelThreshold = value
	}

	fun onDeltaThresholdChange(value: Int) {
		Prefs.deltaLevelThreshold = value
	}

	fun onCheckedEnableToasts(v: View, checked: Boolean) {
		Prefs.toastsEnabled = checked
	}

	private fun addLogMessage(line: String) {
		log.set("${log.get()}$line\n")
	}

	fun onFabClick() {
		val state = !Prefs.serviceEnabled
		Prefs.serviceEnabled = state
		toggleFab(state)
		activity.crouton(if (state) "Service Enabled" else "Service Disabled")
	}

	private fun toggleFab(enable: Boolean) {
		binding.fab.setImageResource(if (enable) R.drawable.ic_wifi_on else R.drawable.ic_wifi_off)
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
		toolbarSubtitle.set("[${result.SSID}]")
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
}