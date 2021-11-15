package dev.mrshawn.kcore.core.commands

import dev.mrshawn.kcore.frameworks.commands.KCommand
import dev.mrshawn.kcore.utils.Chat
import dev.mrshawn.kcore.utils.PlayerUtils
import org.bukkit.GameMode
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object GamemodeCommand : KCommand(
	arrayOf("gamemode", "gm"),
	"Change your, or someone else's, gamemode",
	"/gamemode <c / s / a / sp> [<target>]"
) {

	init {
		addTabCompletions(arrayOf("c", "s", "a", "sp", "@players"))
	}

	override fun execute(sender: CommandSender, args: Array<String>) {
		if (args.isNotEmpty()) {
			val gameMode: GameMode = when (args[0].lowercase()) {
				"creative", "c", "1" -> GameMode.CREATIVE
				"survival", "s", "0" -> GameMode.SURVIVAL
				"adventure", "a", "2" -> GameMode.ADVENTURE
				"spectator", "sp", "3" -> GameMode.SPECTATOR
				else -> {
					Chat.tell(sender, "&cInvalid gamemode!")
					return
				}
			}

			if (args.size >= 2) {
				if (PlayerUtils.isPlayer(args[1])) {
					val target = PlayerUtils.getPlayer(args[1])
					target?.gameMode ?: return
					Chat.tell(sender, "&aSet ${target.name}'s gamemode to ${gameMode.name}")
					return
				} else {
					Chat.tell(sender, "&c${args[1]} is not a valid player!")
					return
				}
			} else {
				if (sender is Player) {
					sender.gameMode = gameMode
					Chat.tell(sender, "&aYour gamemode is now ${gameMode.name}")
					return
				} else {
					Chat.tell(sender, "&cYou must be a player to use this command!")
					return
				}
			}

		} else {
			sendUsage(sender)
		}
	}

}
