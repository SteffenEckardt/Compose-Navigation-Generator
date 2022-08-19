package de.se.cng.extensions

internal fun Collection<Any>.deepEquals(other: Collection<Any>) = this.containsAll(other) && other.containsAll(this)
