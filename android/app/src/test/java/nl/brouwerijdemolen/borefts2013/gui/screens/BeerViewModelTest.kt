package nl.brouwerijdemolen.borefts2013.gui.screens

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import nl.brouwerijdemolen.borefts2013.gui.viewModelsModule
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.koin.core.parameter.parametersOf
import org.koin.standalone.StandAloneContext.closeKoin
import org.koin.standalone.StandAloneContext.startKoin
import org.koin.standalone.inject
import org.koin.test.KoinTest
import org.mockito.Mockito

class BeerViewModelTest : KoinTest {

    @get:Rule var rule: TestRule = InstantTaskExecutorRule()

    private val beerViewModel: BeerViewModel by inject { parametersOf(dummyBeer) }

    @Before
    fun `setup dependencies`() {
        startKoin(listOf(testModule, viewModelsModule))
    }

    @After
    fun `clean up dependencies`() {
        closeKoin()
    }

    @Test
    fun `initializes with beer`() {
        lateinit var result: BeerUiModel
        beerViewModel.state.observeForever { model -> result = requireNotNull(model) }
        assertThat(result).isNotNull()
        assertThat(result.beer).isEqualTo(dummyBeer)
        assertThat(result.isStarred).isFalse()
    }

    @Test
    fun `update star`() {
        lateinit var result: BeerUiModel
        beerViewModel.state.observeForever { model -> result = requireNotNull(model) }
        assertThat(result.isStarred).isFalse()
        beerViewModel.updateStar(true)
        assertThat(result.isStarred).isTrue()
    }

    @Test
    fun `opening brewer starts navigation`() {
        beerViewModel.openBrewer()
        Mockito.verify(mockNavigator).openBrewer(dummyBrewer)
    }

    @Test
    fun `opening style starts navigation`() {
        beerViewModel.openStyle()
        Mockito.verify(mockNavigator).openStyle(dummyStyle)
    }

    @Test
    fun `locating opens map to brewer`() {
        beerViewModel.locateBrewer()
        Mockito.verify(mockNavigator).openMapForBrewer(dummyBeer.brewerId)
    }

}
