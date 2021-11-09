package dev.mrshawn.kcore.frameworks.commands

import org.bukkit.Bukkit
import org.bukkit.command.CommandSender

class CompletionHandler {

	private val completions = mutableMapOf<String, (executor: CommandSender) -> Array<String>>()

	init {
		registerCompletion("@nothing") { emptyArray() }
		registerCompletion("@players", completion = {
            Bukkit.getOnlinePlayers().map { it.name }.toTypedArray()
        })
		registerCompletion("@worlds", completion = {
            Bukkit.getWorlds().map { it.name }.toTypedArray()
        })
	}

	fun registerCompletion(key: String, completion: (executor: CommandSender) -> Array<String>) {
		completions[key] = completion
	}

	private fun getCompletion(key: String, executor: CommandSender): Array<String> {
		return completions[key]?.invoke(executor) ?: arrayOf(key)
	}

	fun processCompletions(completions: Array<String>, executor: CommandSender): MutableList<String> {
		val result = mutableListOf<String>()
		completions.forEach { result.plus(getCompletion(it, executor)) }
		return result
	}

}