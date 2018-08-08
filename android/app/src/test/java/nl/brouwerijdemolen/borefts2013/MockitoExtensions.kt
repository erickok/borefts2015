package nl.brouwerijdemolen.borefts2013

import org.mockito.Mockito

inline fun <reified T> mock() = Mockito.mock(T::class.java)
