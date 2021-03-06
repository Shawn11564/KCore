package dev.mrshawn.kcore.frameworks.commands

import dev.mrshawn.kcore.utils.Chat
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

abstract class KCommand(val aliases: Array<String>, val description: String?, val usage: String?, private val permission: String = "", val requirePlayer: Boolean = false) {

	constructor(aliases: Array<String>, description: String?, usage: String?, permission: String, requirePlayer: Boolean, subcommands: Array<KCommand>) : this(aliases, description, usage, permission, requirePlayer) {
		this.addSubcommands(subcommands)
	}

	val subcommands = mutableListOf<KCommand>()

	val tabCompletion = mutableListOf<String>()

	fun addSubcommand(subcommand: KCommand) = addSubcommands(arrayOf(subcommand))
	fun addSubcommands(subcommands: Array<KCommand>) {
		this.subcommands.addAll(subcommands)
		subcommands.forEach { subcommand ->
			this.tabCompletion.addAll(subcommand.aliases)
		}
	}

	fun addTabCompletions(completion: Array<String>) {
		this.tabCompletion.addAll(completion)
	}

	fun canExecute(sender: CommandSender, requirePlayer: Boolean = false): Boolean {
		return if (requirePlayer && sender !is Player) {
			false
		} else permission.isEmpty() || sender.hasPermission(permission)
	}

	abstract fun execute(sender: CommandSender, args: Array<String>)

	fun sendDescription(sender: CommandSender) = Chat.tell(sender, description ?: "")
	fun sendUsage(sender: CommandSender) = Chat.tell(sender, usage ?: "")

}
