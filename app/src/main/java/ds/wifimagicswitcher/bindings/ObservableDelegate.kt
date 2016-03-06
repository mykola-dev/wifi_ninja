package ds.wifimagicswitcher.bindings

import android.databinding.BaseObservable
import java.io.Serializable
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

fun <T> observable(): ObservableDelegate<T> = ObservableDelegate()

class ObservableDelegate<T>() : BaseObservable(), Serializable, ReadWriteProperty<Any, T?> {

	private var value: T? = null

	constructor(value: T) : this() {
		this.value = value
	}

	override fun getValue(thisRef: Any, property: KProperty<*>): T? = value

	override fun setValue(thisRef: Any, property: KProperty<*>, value: T?) {
		this.value = value
		notifyChange()
	}


	companion object {
		internal val serialVersionUID = 1L
	}
}