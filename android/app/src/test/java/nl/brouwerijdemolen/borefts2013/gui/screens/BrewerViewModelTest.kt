package nl.brouwerijdemolen.borefts2013.gui.screens

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import nl.brouwerijdemolen.borefts2013.gui.Navigator
import nl.brouwerijdemolen.borefts2013.gui.TestWithKoin
import nl.brouwerijdemolen.borefts2013.gui.dummyBeer
import nl.brouwerijdemolen.borefts2013.gui.dummyBrewer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.koin.core.parameter.parametersOf
import org.koin.standalone.inject
import org.koin.test.KoinTest
import org.mockito.Mockito

class BrewerViewModelTest : KoinTest {

    @get:Rule var rule: TestRule = InstantTaskExecutorRule()

    private val beerViewModel: BrewerViewModel by inject { parametersOf(dummyBrewer) }
    private val mockNavigator: Navigator by inject()

    @Before
    fun `setup dependencies`() {
        TestWithKoin.startKoin()
    }

    @After
    fun `clean up dependencies`() {
        TestWithKoin.stopKoin()
    }

    @Test
    fun `initializes with brewer`() {
        lateinit var result: BrewerUiModel
        beerViewModel.state.observeForever { model -> result = requireNotNull(model) }
        assertThat(result).isNotNull()
        assertThat(result.brewer).isEqualTo(dummyBrewer)
        assertThat(result.beers).isNotNull()
        assertThat(result.beers[0]).isEqualTo(dummyBeer)
    }

    @Test
    fun `opening a beer starts navigation`() {
        beerViewModel.openBeer(dummyBeer)
        Mockito.verify(mockNavigator).openBeer(dummyBeer)
    }

}
