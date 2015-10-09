package ds.wifimagicswitcher.prefs

import android.content.Context
import android.content.SharedPreferences
import ds.wifimagicswitcher.utils.T
import kotlin.properties.ReadWriteProperty


object KotlinPrefsSetup {
	@Volatile var isBatching = false
	lateinit var prefs: SharedPreferences
	lateinit var edit: SharedPreferences.Editor
	/**
	 * Run it on App start
	 */
	fun init(ctx: Context, name: String) {
		prefs = ctx.getSharedPreferences(name, Context.MODE_PRIVATE)
		edit=prefs.edit()
	}
}

fun prefsBatch(f: () -> Unit) {
	KotlinPrefsSetup.isBatching = true
	f()
	KotlinPrefsSetup.isBatching = false
	KotlinPrefsSetup.edit.apply()
}

fun prefsKey<T>(default: T): PrefsDelegate<T> = PrefsDelegate(default)

class PrefsDelegate<T>(val default: T) : ReadWriteProperty<Any?, T> {

	var value: T = default

	@Suppress("unchecked_cast")
	override fun get(thisRef: Any?, property: PropertyMetadata): T {
		val n = property.name
		val prefs = KotlinPrefsSetup.prefs
		when (value) {
			is String -> return prefs.getString(n, default as String) as T
			is Int -> return prefs.getInt(n, default as Int) as T
			is Long -> return prefs.getLong(n, default as Long) as T
			is Float -> return prefs.getFloat(n, default as Float) as T
			is Boolean -> return prefs.getBoolean(n, default as Boolean) as T
			is Set<*> -> return prefs.getStringSet(n, default as Set<String>) as T
			else -> throw IllegalArgumentException()
		}
	}

	@Suppress("unchecked_cast")
	override fun set(thisRef: Any?, property: PropertyMetadata, value: T) {
		this.value = value
		val n = property.name
		val e = KotlinPrefsSetup.edit
		when (value) {
			is String -> e.putString(n, value)
			is Int -> e.putInt(n, value)
			is Long -> e.putLong(n, value)
			is Float -> e.putFloat(n, value)
			is Boolean -> e.putBoolean(n, value)
			is Set<*> -> e.putStringSet(n, value as Set<String>)
			else -> throw IllegalArgumentException()
		}

		if (!KotlinPrefsSetup.isBatching)
			e.apply()
	}

}

