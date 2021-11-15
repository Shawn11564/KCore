package dev.mrshawn.kcore.core.commands

import dev.mrshawn.kcore.frameworks.commands.KCommand
import dev.mrshawn.kcore.utils.Chat
import dev.mrshawn.kcore.utils.PlayerUtils
import org.bukkit.attribute.Attribute
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object HealCommand: KCommand(
	arrayOf("heal"),
	"Heals you or your target",
	"/heal [<target>]"
) {

	override fun execute(sender: CommandSender, args: Array<String>) {
		if (args.isEmpty() && sender is Player) {
			sender.health = sender.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.value ?: 20.0
			Chat.tell(sender, "&aYou have been healed!")
		} else {
			sendUsage(sender)
		}

		if (args.isNotEmpty()) {
            val target = PlayerUtils.getPlayer(args[1])
            if (target != null) {
                target.health = target.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.value ?: 20.0
                Chat.tell(sender, "&aYou have healed &e${target.name}&a!")
            } else {
                Chat.tell(sender, "&cPlayer not found!")
            }
        }

	}

}
