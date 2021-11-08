package dev.mrshawn.kcore.frameworks.commands

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin

class KCommandManager(private val plugin: JavaPlugin): CommandExecutor {

	private val commands = mutableMapOf<String, KCommand>()

	fun registerCommand(command: KCommand) {
		commands[command.aliases.first()] = command

		plugin.getCommand(command.aliases.first())?.setExecutor(this)
	}

	private fun matchBaseCommand(command: String): KCommand? {
		return commands.values.firstOrNull { it.aliases.contains(command) }
	}

	private fun getHighestSubCommand(baseCommand: KCommand, args: Array<String>): Pair<KCommand, Int> {
		var command = baseCommand
		var argIndex = 0

		for (arg in args.withIndex()) {
			val subCommand = command.subcommands.firstOrNull { it.aliases.contains(arg.value) }

			if (subCommand != null) {
				command = subCommand
				argIndex = arg.index + 1
			}
		}
		return Pair(command, argIndex)
	}

	override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<String>): Boolean {

		val baseCommand = matchBaseCommand(label) ?: return false
		val (command, argIndex) = getHighestSubCommand(baseCommand, args)

		if (command.canExecute(sender, command.requirePlayer)) {
			command.execute(sender, args.copyOfRange(argIndex, args.size))
		}

		return false
	}

}
