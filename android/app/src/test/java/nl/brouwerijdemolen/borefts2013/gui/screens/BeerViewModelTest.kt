package nl.brouwerijdemolen.borefts2013.gui.screens

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import nl.brouwerijdemolen.borefts2013.gui.*
import nl.brouwerijdemolen.borefts2013.gui.components.StarPersistence
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.koin.core.parameter.parametersOf
import org.koin.test.KoinTest
import org.koin.test.inject
import org.mockito.Mockito.verify

class BeerViewModelTest : KoinTest {

    @get:Rule var rule: TestRule = InstantTaskExecutorRule()

    private val beerViewModel: BeerViewModel by inject { parametersOf(dummyBeer) }
    private val mockStarPersistence: StarPersistence by inject()
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
        verify(mockStarPersistence).addStar(dummyBeer.id)

        beerViewModel.updateStar(false)
        assertThat(result.isStarred).isFalse()
        verify(mockStarPersistence).removeStar(dummyBeer.id)
    }

    @Test
    fun `opening brewer starts navigation`() {
        beerViewModel.openBrewer()
        verify(mockNavigator).openBrewer(dummyBrewer)
    }

    @Test
    fun `opening style starts navigation`() {
        beerViewModel.openStyle()
        verify(mockNavigator).openStyle(dummyStyle)
    }

    @Test
    fun `locating opens map to brewer`() {
        beerViewModel.locateBrewer()
        verify(mockNavigator).openMapForBrewer(dummyBeer.brewerId)
    }

}
