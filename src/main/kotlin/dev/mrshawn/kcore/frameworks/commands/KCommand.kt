package dev.mrshawn.kcore.frameworks.commands

import dev.mrshawn.kcore.utils.Chat
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

abstract class KCommand(val aliases: Array<String>, private val description: String?, private val usage: String?, private val permission: String = "", val requirePlayer: Boolean = false) {

	constructor(aliases: Array<String>, description: String?, usage: String?, permission: String, requirePlayer: Boolean, subcommands: Array<KCommand>) : this(aliases, description, usage, permission, requirePlayer) {
		this.addSubcommands(subcommands)
	}

	val subcommands = mutableListOf<KCommand>()

	fun addSubcommands(subcommands: Array<KCommand>) = subcommands.forEach { this.subcommands.add(it) }

	fun canExecute(sender: CommandSender, requirePlayer: Boolean = false): Boolean {
		return if (requirePlayer && sender !is Player) {
			false
		} else permission.isEmpty() || sender.hasPermission(permission)
	}

	abstract fun execute(sender: CommandSender, args: Array<String>)

	fun sendDescription(sender: CommandSender) = Chat.tell(sender, description ?: "")
	fun sendUsage(sender: CommandSender) = Chat.tell(sender, usage ?: "")

}
