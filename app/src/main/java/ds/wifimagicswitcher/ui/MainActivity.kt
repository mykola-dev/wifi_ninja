package ds.wifimagicswitcher.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import cz.kinst.jakub.viewmodelbinding.ViewModelActivity
import ds.wifimagicswitcher.R
import ds.wifimagicswitcher.bindings.viewmodel.MainViewModel
import ds.wifimagicswitcher.databinding.MainActivityBinding

class MainActivity : ViewModelActivity<MainActivityBinding, MainViewModel>() {

	override fun getLayoutResourceId(): Int = R.layout.activity_main

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setSupportActionBar(binding.toolbar)
	}


	override fun onCreateOptionsMenu(menu: Menu): Boolean {
		menuInflater.inflate(R.menu.menu_main, menu)
		return true
	}


	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		when (item.itemId) {
			R.id.force_scan -> viewModel.startScan()
			R.id.recommended -> viewModel.onRecommendedSettings()
		}
		return super.onOptionsItemSelected(item)
	}

}
