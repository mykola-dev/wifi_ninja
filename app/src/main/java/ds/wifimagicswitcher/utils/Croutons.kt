package ds.wifimagicswitcher.utils

import android.app.Activity
import de.keyboardsurfer.android.widget.crouton.Crouton
import de.keyboardsurfer.android.widget.crouton.Style
import ds.wifimagicswitcher.App
import ds.wifimagicswitcher.R

private val defaultStyle =Style.Builder().setBackgroundColorValue(App.instance.resources.getColor(R.color.crouton_blue)).build()

fun Activity.crouton(text: String, style: Style = defaultStyle) = Crouton.makeText(this, text, style).show()


