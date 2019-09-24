package nl.brouwerijdemolen.borefts2013.gui

import org.koin.core.KoinComponent
import org.koin.core.get
import org.koin.core.qualifier.Qualifier
import kotlin.coroutines.CoroutineContext

object CoroutineScope : KoinComponent {
    object IO: Qualifier
    object UI: Qualifier
    val io: CoroutineContext = get(IO)
    val ui: CoroutineContext = get(UI)
}
