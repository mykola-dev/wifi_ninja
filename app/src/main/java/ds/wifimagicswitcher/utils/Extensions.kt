package ds.wifimagicswitcher.utils

import android.os.Handler
import android.os.Looper
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.TextUtils
import ds.wifimagicswitcher.App
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

operator fun CharSequence.plus(operand:CharSequence): CharSequence = TextUtils.concat(this,operand)
fun post(runnable: () -> Unit) {
	val h = Handler(Looper.getMainLooper())
	h.post(runnable)
}

fun postDelayed(delay: Long, runnable: () -> Unit) {
	val h = Handler(Looper.getMainLooper())
	h.postDelayed(runnable, delay)
}