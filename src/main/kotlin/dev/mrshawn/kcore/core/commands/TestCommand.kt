package dev.mrshawn.kcore.core.commands

import dev.mrshawn.kcore.frameworks.commands.KCommand
import dev.mrshawn.kcore.utils.Chat
import org.bukkit.command.CommandSender

object TestCommand: KCommand(
	arrayOf("test"),
	"Simple test command of the command framework",
	"/test <args>"
) {

	override fun execute(sender: CommandSender, args: Array<String>) {
		Chat.tell(sender, "&aTest command executed!")
	}

}