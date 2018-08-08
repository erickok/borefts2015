package nl.brouwerijdemolen.borefts2013.gui

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object CachedSpek : Spek({
    Feature("Cached value") {

        val cached by memoized { Cached<String>(500) }

        Scenario("Starts empty") {
            Given("The cache") {}
            Then("It is empty") {
                assert(cached.value.isEmpty())
            }
        }
    }
})
