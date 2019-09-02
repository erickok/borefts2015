package nl.brouwerijdemolen.borefts2013.gui

import org.koin.standalone.KoinComponent
import org.koin.standalone.get
import kotlin.coroutines.CoroutineContext

object CoroutineScope : KoinComponent {
    val ui: CoroutineContext = get("ui")
}
