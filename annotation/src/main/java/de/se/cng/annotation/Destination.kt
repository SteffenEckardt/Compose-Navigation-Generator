package de.se.cng.annotation

/**
 * TODO
 */
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.FUNCTION)
annotation class Destination(val name: String = "", val isHome: Boolean = true)
