package dev.mrshawn.kcore.frameworks.commands

import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class BukkitCommand(command: KCommand, private val commandManager: KCommandManager): Command(
	command.aliases.first(),
	command.description ?: "",
	command.usage ?: "",
	command.aliases.toMutableList()
) {

	override fun execute(sender: CommandSender, label: String, args: Array<String>): Boolean {
        return commandManager.executeCommand(sender, label, args)
    }

}