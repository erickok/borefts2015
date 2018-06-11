package nl.brouwerijdemolen.borefts2013.test

import arrow.core.Try
import org.assertj.core.api.ObjectAssert

fun <ACTUAL : Try<*>> ObjectAssert<ACTUAL>.isSuccess() = this.isInstanceOf(Try.Success::class.java)
