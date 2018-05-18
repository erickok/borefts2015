package nl.brouwerijdemolen.borefts2013.gui.components

import android.util.Log
import arrow.core.Failure

fun Failure<*>.log() = Log.w("Try", this.exception.toString())
