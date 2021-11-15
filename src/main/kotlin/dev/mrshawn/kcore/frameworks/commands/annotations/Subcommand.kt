package dev.mrshawn.kcore.frameworks.commands.annotations

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class Subcommand(val aliases: Array<String>)
