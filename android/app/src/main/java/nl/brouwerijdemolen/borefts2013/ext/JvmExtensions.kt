package nl.brouwerijdemolen.borefts2013.ext

import java.io.File

fun Int?.orZero(): Int = this ?: 0

operator fun File.div(fileName: String) = File(this, fileName)
