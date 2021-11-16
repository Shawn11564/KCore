package dev.mrshawn.kcore.frameworks.commands

import dev.mrshawn.kcore.utils.Chat
import org.bukkit.Bukkit
import org.bukkit.command.*
import org.bukkit.plugin.SimplePluginManager
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.util.StringUtil
import java.lang.reflect.Field
import java.util.*

class KCommandManager(private val plugin: JavaPlugin): TabCompleter {

	private var commandMap: CommandMap? = null
	private val completionHandler = CompletionHandler()
	private val commands = mutableMapOf<String, KCommand>()

	init {
		// Get the command map
		try {
			// TODO: Use kotlin reflection
			val field = SimplePluginManager::class.java.getDeclaredField("commandMap")
			field.isAccessible = true
			commandMap = field[Bukkit.getPluginManager()] as CommandMap
		} catch (ex: Exception) {
			Chat.log("&cFailed to get command map")
			ex.printStackTrace()
		}
	}

	fun registerCommand(command: KCommand) {
        val commandName = command.aliases.first()
		commands[commandName] = command

		// register command to command map
		commandMap?.register(commandName, BukkitCommand(command, this))
		plugin.getCommand(commandName)?.tabCompleter = this
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

	fun executeCommand(sender: CommandSender, label: String, args: Array<String>): Boolean {
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
