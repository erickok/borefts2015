package nl.brouwerijdemolen.borefts2013.gui.screens

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import nl.brouwerijdemolen.borefts2013.gui.Navigator
import nl.brouwerijdemolen.borefts2013.gui.TestWithKoin
import nl.brouwerijdemolen.borefts2013.gui.dummyBeer
import nl.brouwerijdemolen.borefts2013.gui.dummyStyle
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.koin.core.parameter.parametersOf
import org.koin.test.KoinTest
import org.koin.test.inject
import org.mockito.Mockito.verify

class StyleViewModelTest : KoinTest {

    @get:Rule var rule: TestRule = InstantTaskExecutorRule()

    private val styleViewModel: StyleViewModel by inject { parametersOf(dummyStyle) }
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
    fun `initializes with style`() {
        lateinit var result: StyleUiModel
        styleViewModel.state.observeForever { model -> result = requireNotNull(model) }
        assertThat(result).isNotNull()
        assertThat(result.style).isEqualTo(dummyStyle)
        assertThat(result.beers).isNotNull()
        assertThat(result.beers[0]).isEqualTo(dummyBeer)
    }

    @Test
    fun `opening a beer starts navigation`() {
        styleViewModel.openBeer(dummyBeer)
        verify(mockNavigator).openBeer(dummyBeer)
    }

}
