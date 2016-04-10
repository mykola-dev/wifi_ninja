package ds.wifimagicswitcher.bindings

import android.databinding.BindingAdapter
import android.widget.ImageView
import ds.wifimagicswitcher.R
import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar

object BindingAdapters {

	@BindingAdapter("onChange")
	@JvmStatic fun setOnProgressChanged(view: DiscreteSeekBar, l: onChangeProgressListener?) {
		if (l == null) {
			view.setOnProgressChangeListener(null)
		} else {
			view.setOnProgressChangeListener(object : DiscreteSeekBar.OnProgressChangeListener {

				override fun onProgressChanged(seekBar: DiscreteSeekBar, value: Int, fromUser: Boolean) {
					if (fromUser)
						l.onChangeProgress(value)
				}

				override fun onStartTrackingTouch(seekBar: DiscreteSeekBar) {
				}

				override fun onStopTrackingTouch(seekBar: DiscreteSeekBar) {
				}
			})
		}
	}

	interface onChangeProgressListener {
		fun onChangeProgress(progress: Int)
	}

	@BindingAdapter("check")
	@JvmStatic fun setChecked(view: ImageView, checked: Boolean) {
		view.setImageResource(if (checked) R.drawable.ic_wifi_on else R.drawable.ic_wifi_off)
	}
}
