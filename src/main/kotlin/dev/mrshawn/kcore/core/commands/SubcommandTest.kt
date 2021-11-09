package dev.mrshawn.kcore.core.commands

import dev.mrshawn.kcore.frameworks.commands.KCommand
import dev.mrshawn.kcore.utils.Chat
import org.bukkit.command.CommandSender

object SubcommandTest: KCommand(
	arrayOf("sub"),
	"Test subcommand",
	"/test sub"
) {

	init {
		addTabCompletion("@players")
		addTabCompletion("completion")
	}

	override fun execute(sender: CommandSender, args: Array<String>) {
		Chat.tell(sender, "&aSubcommand test, args:")
		args.forEach { Chat.tell(sender, "&a$it") }
	}

}