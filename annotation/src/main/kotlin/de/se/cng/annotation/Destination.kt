package de.se.cng.annotation

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.FUNCTION)
annotation class Destination(val name: String = "", val isHome: Boolean = true)
