package dev.mrshawn.kcore.core.commands

import dev.mrshawn.kcore.frameworks.commands.KCommand
import dev.mrshawn.kcore.utils.Chat
import dev.mrshawn.kcore.utils.PlayerUtils
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object FlyCommand: KCommand(
	arrayOf("fly"),
	"Toggle flight",
	"/fly [<on / off>] [<target>]"
) {

	init {
		addTabCompletions(arrayOf("on", "off", "@players"))
	}

	override fun execute(sender: CommandSender, args: Array<String>) {
		if (args.isEmpty()) {
			if (sender is Player) {
				toggleFlight(sender)
				Chat.tell(sender, "&aFlight ${if (sender.isFlying) "enabled" else "disabled"}")
			} else {
				Chat.tell(sender, "&cYou must specify a target")
			}
		}

		var toggle: Boolean? = null
		var target: Player? = null

		if (args.isNotEmpty()) {
			toggle = when (args[0].lowercase()) {
				"on" -> true
				"off" -> false
				else -> null
			}
		}

		if (args.size >= 2) {
			if (PlayerUtils.isPlayer(args[1])) target = PlayerUtils.getPlayer(args[1])
			else if (sender is Player) target = sender
			else Chat.tell(sender, "&cYou must specify a target")
		}

		if (target != null) {
			if (toggle != null) target.isFlying = toggle else toggleFlight(target)
			Chat.tell(sender, "&aFlight ${if (target.isFlying) "enabled" else "disabled"} for &e${target.name}")
		}
	}

	private fun toggleFlight(player: Player) {
		player.isFlying = !player.isFlying
	}

}
