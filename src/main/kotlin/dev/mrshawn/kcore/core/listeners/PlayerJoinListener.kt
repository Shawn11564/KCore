package dev.mrshawn.kcore.core.listeners

import dev.mrshawn.kcore.utils.Chat
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

object PlayerJoinListener: Listener {

	@EventHandler
	fun onPlayerJoin(event: PlayerJoinEvent) {
		Chat.tell(event.player, "&aWelcome to the server")
    }

}