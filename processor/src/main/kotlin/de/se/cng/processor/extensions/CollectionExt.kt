package de.se.cng.processor.extensions

import de.se.cng.processor.generator.TypeNames
import de.se.cng.processor.models.NavigationParameter

internal fun Collection<Any>.deepEquals(other: Collection<Any>) = this.containsAll(other) && other.containsAll(this)

internal fun Set<NavigationParameter>.filterNavigator() = this.filterNot { it.type.className == TypeNames.Classes.Navigator }.toSet()

