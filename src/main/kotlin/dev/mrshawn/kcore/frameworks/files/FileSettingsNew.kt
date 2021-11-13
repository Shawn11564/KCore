package dev.mrshawn.kcore.frameworks.files

import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import kotlin.reflect.KClass

class FileSettings(val file: File, val enumClass: KClass<out Enum<*>>) {

	val config = YamlConfiguration.loadConfiguration(file)
	val settings: MutableMap<Enum<*>, Any> = HashMap()

	fun loadSettings() {
		for (value in getEnumValues(enumClass)) {
			settings.put(value, config.get(value.))
		}
	}

}

fun getEnumValues(enumClass: KClass<out Enum<*>>): Array<out Enum<*>> = enumClass.java.enumConstants
