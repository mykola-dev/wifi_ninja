package ds.wifimagicswitcher.bindings

import android.databinding.ObservableField
import java.io.Serializable
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

fun <T> observable(v: T? = null): ObservableDelegate<T> = ObservableDelegate(v)

class ObservableDelegate<T>() : ObservableField<T>(), Serializable, ReadWriteProperty<Any, T?> {

	var value: T? = null

	constructor(value: T?) : this() {
		set(value)
	}

	override fun getValue(thisRef: Any, property: KProperty<*>): T? = get()

	override fun setValue(thisRef: Any, property: KProperty<*>, value: T?) {
		set(value)
	}
}