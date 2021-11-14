package dev.mrshawn.kcore.utils

import org.bukkit.command.CommandSender

class ChatMessage(private val toWhom: CommandSender) {

	private val messages = mutableListOf<String>()
	private val replacements = mutableMapOf<String, String>()

	fun addMessage(message: String): ChatMessage {
		messages.add(message)
		return this
	}

	fun addReplacement(key: String, value: String): ChatMessage {
		replacements[key] = value
		return this
	}

	fun send() {
		for (message in messages) {
			for (replacement in replacements) {
                message.replace(replacement.key, replacement.value)
            }
			Chat.tell(toWhom, message)
		}
	}

}
