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
			heal(sender)
			Chat.tell(sender, "&aYou have been healed!")
		} else {
			sendUsage(sender)
		}

		if (args.isNotEmpty()) {
            val target = PlayerUtils.getPlayer(args[0])
            if (target != null) {
                heal(target)
                Chat.tell(sender, "&aYou have healed &e${target.name}&a!")
            } else {
                Chat.tell(sender, "&cPlayer not found!")
            }
        }

	}

	private fun heal(target: Player) {
		target.health = target.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.value ?: 20.0
        target.foodLevel = 20
        target.fireTicks = 0
		target.activePotionEffects.forEach {
            target.removePotionEffect(it.type)
        }
	}

}
