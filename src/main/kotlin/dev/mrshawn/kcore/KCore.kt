package dev.mrshawn.kcore

import dev.mrshawn.kcore.core.commands.TestCommand
import dev.mrshawn.kcore.core.listeners.PlayerJoinListener
import dev.mrshawn.kcore.frameworks.commands.KCommandManager
import dev.mrshawn.kcore.utils.Chat
import org.bukkit.plugin.PluginManager
import org.bukkit.plugin.java.JavaPlugin

class KCore : JavaPlugin() {

	override fun onEnable() {

		Chat.log("&aKCore loading...")

		registerCommands()
		registerListeners()

		Chat.log("&aKCore loaded!")
	}

	override fun onDisable() {
		// Plugin shutdown logic
	}

	private fun registerCommands() {
		val commandManager = KCommandManager(this)

		commandManager.registerCommand(TestCommand)
	}

	private fun registerListeners() {
		val pm = server.pluginManager

		pm.registerEvents(PlayerJoinListener, this)
	}

}
