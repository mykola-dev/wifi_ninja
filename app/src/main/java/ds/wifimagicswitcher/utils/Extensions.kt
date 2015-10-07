package ds.wifimagicswitcher.utils

import android.widget.SeekBar
import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar

fun DiscreteSeekBar.onChange(f: (progress: Int) -> Unit) {
	this.setOnProgressChangeListener(object : DiscreteSeekBar.OnProgressChangeListener {
		override fun onProgressChanged(seekBar: DiscreteSeekBar?, value: Int, fromUser: Boolean) {
			if (fromUser)
				f(value)
		}

		override fun onStartTrackingTouch(seekBar: DiscreteSeekBar?) {
		}

		override fun onStopTrackingTouch(seekBar: DiscreteSeekBar?) {
		}
	})
}