package dev.mrshawn.kcore.utils

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender

object Chat {

	fun tell(toWhom: CommandSender, message: String) = toWhom.sendMessage(colorize(message))

	fun tell(toWhom: CommandSender, messages: Array<String>) {
		for (message in messages)
			tell(toWhom, message)
	}

	fun log(message: String) = Bukkit.getConsoleSender().sendMessage(colorize(message))

	fun broadcast(message: String) = Bukkit.broadcastMessage(colorize(message))

	fun clearChat() {
		for (i in 0 .. 100)
            Bukkit.broadcastMessage(" ")
	}

	fun colorize(message: String): String = ChatColor.translateAlternateColorCodes('&', message)

}
