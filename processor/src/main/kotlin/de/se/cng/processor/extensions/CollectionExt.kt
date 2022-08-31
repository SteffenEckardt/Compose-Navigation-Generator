package de.se.cng.processor.extensions

import de.se.cng.processor.generator.ParameterType
import de.se.cng.processor.models.NavigationParameter

internal fun Collection<Any>.deepEquals(other: Collection<Any>) = this.containsAll(other) && other.containsAll(this)

internal fun Collection<NavigationParameter>.filterNavHostController() = this.filterNot { it.type is ParameterType.NavHostController }

