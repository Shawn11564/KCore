package dev.mrshawn.kcore.frameworks.files

import org.bukkit.plugin.java.JavaPlugin
import java.util.HashMap
import org.bukkit.configuration.file.YamlConfiguration
import java.util.EnumSet
import java.lang.NoSuchMethodException
import java.lang.SecurityException
import org.bukkit.Bukkit
import java.io.File
import java.lang.IllegalAccessException
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import java.util.ArrayList

class FileSettings {
	private val plugin: JavaPlugin
	private val file: File
	private val values: MutableMap<Enum<*>, Any> = HashMap()

	constructor(plugin: JavaPlugin, file: File, isResource: Boolean) {
		this.plugin = plugin
		this.file = file
		if (isResource) {
			plugin.saveResource(this.file.path, false)
		}
	}

	constructor(plugin: JavaPlugin, filePath: String, isResource: Boolean) {
		this.plugin = plugin
		file = File("$filePath.yml")
		if (isResource) {
			plugin.saveResource(file.path, false)
		}
	}

	fun <E : Enum<E>?> loadSettings(enumClass: Class<E>): FileSettings? {
		return try {
			val config = YamlConfiguration.loadConfiguration(file)
			val eSet = EnumSet.allOf(enumClass)
			val getPath = enumClass.getMethod("getPath")
			var getDefault: Method? = null
			var hasDefaults = true
			try {
				getDefault = enumClass.getMethod("getDefault")
			} catch (e: NoSuchMethodException) {
				hasDefaults = false
			} catch (e: SecurityException) {
				hasDefaults = false
			}
			for (value in eSet) {
				val configPath = getPath.invoke(value) as String
				if (!config.contains(configPath)) {
					if (hasDefaults) {
						config[configPath] = getDefault!!.invoke(value)
					} else {
						continue
					}
				}
				values[value] = config[(getPath.invoke(value) as String)]!!
			}
			this
		} catch (e: NoSuchMethodException) {
			Bukkit.getLogger().severe("Error when loading settings for: $file")
			e.printStackTrace()
			null
		} catch (e: IllegalAccessException) {
			Bukkit.getLogger().severe("Error when loading settings for: $file")
			e.printStackTrace()
			null
		} catch (e: InvocationTargetException) {
			Bukkit.getLogger().severe("Error when loading settings for: $file")
			e.printStackTrace()
			null
		}
	}

	fun getBoolean(value: Enum<*>): Boolean {
		return get(value, Boolean::class.java)
	}

	fun getBoolean(value: Enum<*>, defaultValue: Boolean): Boolean {
		return get(value, Boolean::class.java, defaultValue)
	}

	fun getString(value: Enum<*>): String {
		return get(value, String::class.java)
	}

	fun getString(value: Enum<*>, defaultValue: String): String {
		return get(value, String::class.java, defaultValue)
	}

	fun getInt(value: Enum<*>): Int {
		return get(value, Int::class.java)
	}

	fun getInt(value: Enum<*>, defaultValue: Int): Int {
		return get(value, Int::class.java, defaultValue)
	}

	fun getLong(value: Enum<*>): Long {
		return get(value, Long::class.java)
	}

	fun getLong(value: Enum<*>, defaultValue: Long): Long {
		return get(value, Long::class.java, defaultValue)
	}

	fun getStringList(value: Enum<*>): List<String> {
		val tempList: MutableList<String> = ArrayList()
		for (`val` in get(value, ArrayList::class.java)) {
			tempList.add(`val` as String)
		}
		return tempList
	}

	fun getStringList(value: Enum<*>, defaultValue: List<String?>?): List<String> {
		return if (values.containsKey(value)) getStringList(value) else defaultValue
	}

	operator fun <T> get(value: Enum<*>, clazz: Class<T>): T {
		return clazz.cast(values[value])
	}

	operator fun <T> get(value: Enum<*>, clazz: Class<T>, defaultValue: T): T {
		return if (values.containsKey(value)) get(value, clazz) else clazz.cast(defaultValue)
	}
}