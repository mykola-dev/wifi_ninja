package ds.wifimagicswitcher.ui

import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.CheckBox
import android.widget.TextView
import butterknife.Bind
import butterknife.ButterKnife
import butterknife.OnClick
import com.jakewharton.rxbinding.view.RxView
import de.greenrobot.event.EventBus
import de.greenrobot.event.Subscribe
import ds.wifimagicswitcher.App
import ds.wifimagicswitcher.R
import ds.wifimagicswitcher.prefs.Prefs
import ds.wifimagicswitcher.ui.view.ExpandablePanel
import ds.wifimagicswitcher.utils.crouton
import ds.wifimagicswitcher.utils.onChange
import ds.wifimagicswitcher.utils.plus
import ds.wifimagicswitcher.utils.post
import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar
import uy.kohesive.injekt.injectLazy

class MainActivity : AppCompatActivity() {

	val app by injectLazy<App>()
	val bus by injectLazy<EventBus>()
	val wifi by injectLazy<WifiManager>()

	@Bind(R.id.fab) lateinit var fab: FloatingActionButton
	@Bind(R.id.toolbar) lateinit var toolbar: Toolbar
	@Bind(R.id.min_threshold) lateinit var minThresholdSeekbar: DiscreteSeekBar
	@Bind(R.id.delta_threshold) lateinit var deltaThresholdSeekbar: DiscreteSeekBar
	@Bind(R.id.enable_toasts) lateinit var enableToastsCheck: CheckBox
	@Bind(R.id.expand_panel) lateinit var expandPanel: ExpandablePanel
	@Bind(R.id.log) lateinit var log: TextView

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		ButterKnife.bind(this)
		setSupportActionBar(toolbar)

	}

	override fun onStart() {
		super.onStart()
		initUI()
	}

	override fun onResume() {
		super.onResume()
		bus.register(this)
	}

	override fun onPause() {
		super.onPause()
		bus.unregister(this)
	}

	private fun initUI() {
		toggleFab(Prefs.serviceEnabled)
		supportActionBar.subtitle = "[${wifi.connectionInfo.ssid.drop(1).dropLast(1)}]"
		minThresholdSeekbar.progress = Prefs.minLevelThreshold
		deltaThresholdSeekbar.progress = Prefs.deltaLevelThreshold
		enableToastsCheck.isChecked = Prefs.toastsEnabled
		minThresholdSeekbar.onChange { Prefs.minLevelThreshold = it }
		deltaThresholdSeekbar.onChange { Prefs.deltaLevelThreshold = it }
		enableToastsCheck.setOnCheckedChangeListener { v, it -> Prefs.toastsEnabled = it }
	}


	private fun addLogMessage(line: String) {
		log.text += line
	}

	@OnClick(R.id.fab)
	fun onFabClick() {
		val state = !Prefs.serviceEnabled
		Prefs.serviceEnabled = state
		toggleFab(state)
		crouton(if (state) "Service Enabled" else "Service Disabled")
	}

	fun toggleFab(enable: Boolean) {
		fab.setImageResource(if (enable) R.drawable.ic_wifi_on else R.drawable.ic_wifi_off)
	}

	@Subscribe
	fun onWifiResultsEvent(results: List<ScanResult>) {
		addLogMessage("=== Scan Results ===\n")
		for (r in results) {
			addLogMessage("[${r.SSID}]: ${WifiManager.calculateSignalLevel(r.level, 100)}%\n")
		}
		addLogMessage("\n")
	}

	@Subscribe
	fun onBestWifiEvent(result: ScanResult) {
		crouton("New Best Wifi found!")
		supportActionBar.subtitle = "[${result.SSID}]"
		addLogMessage("SWITCHED TO [${result.SSID}]")
	}

	override fun onCreateOptionsMenu(menu: Menu): Boolean {
		menuInflater.inflate(R.menu.menu_main, menu)
		return true
	}
	
	
	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		val id = item.itemId
		
		when (id) {
			R.id.force_scan -> wifi.startScan()
			R.id.recommended -> {
				Prefs.deltaLevelThreshold = Prefs.DEFAULT_DELTA_TRESH
				Prefs.minLevelThreshold = Prefs.DEFAULT_MIN_TRESH
				Prefs.toastsEnabled = true
				Prefs.serviceEnabled = true
				initUI()
			}
		}

		return super.onOptionsItemSelected(item)
	}

}
