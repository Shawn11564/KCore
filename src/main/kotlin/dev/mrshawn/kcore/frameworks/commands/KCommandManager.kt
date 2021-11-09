package dev.mrshawn.kcore.frameworks.commands

import org.bukkit.command.*
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.util.StringUtil
import java.util.*

class KCommandManager(private val plugin: JavaPlugin): TabExecutor{

	private val completionHandler = CompletionHandler()
	private val commands = mutableMapOf<String, KCommand>()

	fun registerCommand(command: KCommand) {
		commands[command.aliases.first()] = command

		plugin.getCommand(command.aliases.first())?.setExecutor(this)
		plugin.getCommand(command.aliases.first())?.tabCompleter = this
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

	private fun matchSubcommand(baseCommand: KCommand, arg: String): KCommand? {
        return baseCommand.subcommands.firstOrNull { it.aliases.contains(arg) }
    }

	private fun getTabCompletion(sender: CommandSender, baseCommand: KCommand, args: Array<String>): MutableList<String> {
		val command = getHighestSubCommand(baseCommand, args)
		val completion: Array<String> = emptyArray()
		completion.plus(command.first.tabCompletion)

		return completionHandler.processCompletions(completion, sender)
	}

	// convert Array<out String> to Array<String>
	private fun toArray(args: Array<out String>): Array<String> = args.map { it }.toTypedArray()

	private fun sortResults(arg: String, results: MutableList<String>): MutableList<String> {
        val completions = mutableListOf<String>()
		StringUtil.copyPartialMatches(arg, results, completions)
		completions.sort()
		return completions
    }

	override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<String>): Boolean {
		val baseCommand = matchBaseCommand(label.removePrefix("kcore:")) ?: return false
		val (command, argIndex) = getHighestSubCommand(baseCommand, args)

		if (command.canExecute(sender, command.requirePlayer))
			command.execute(sender, args.copyOfRange(argIndex, args.size))

		return false
	}

	override fun onTabComplete(sender: CommandSender, cmd: Command, alias: String, args: Array<out String>): MutableList<String> {
		return mutableListOf()
		//return sortResults(args[args.size - 1], getTabCompletion(sender,  matchBaseCommand(alias.removePrefix("kcore:")) ?: return mutableListOf(), toArray(args)))
	}

}
