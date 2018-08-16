package nl.brouwerijdemolen.borefts2013.gui.screens

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.whenever
import nl.brouwerijdemolen.borefts2013.gui.Navigator
import nl.brouwerijdemolen.borefts2013.gui.TestWithKoin
import nl.brouwerijdemolen.borefts2013.gui.components.StarPersistence
import nl.brouwerijdemolen.borefts2013.gui.dummyBeer
import nl.brouwerijdemolen.borefts2013.test.Register
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.koin.standalone.inject
import org.koin.test.KoinTest
import org.mockito.Mockito.verify

class StarsViewModelTest : KoinTest {

    @get:Rule var rule: TestRule = InstantTaskExecutorRule()

    private val stylesViewModel: StarsViewModel by inject()
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
    fun `initialization emits starred beers state`() {
        whenever(mockStarPersistence.allStars).thenReturn(hashSetOf(1))
        val register = Register<StarsUiModel>()
        stylesViewModel.state.observeForever(register)
        assertThat(register.valueCount).isEqualTo(1)
        assertThat(register.lastValue).isEqualTo(StarsUiModel.Loading)

        stylesViewModel.refresh()
        assertThat(register.valueCount).isEqualTo(2)
        assertThat(register.lastValue).isInstanceOf(StarsUiModel.Success::class.java)
        val result = (register.lastValue as StarsUiModel.Success)
        assertThat(result.beers).isEqualTo(listOf(dummyBeer))
    }

    @Test
    fun `opening a brewer starts navigation`() {
        stylesViewModel.openBeer(dummyBeer)
        verify(mockNavigator).openBeer(dummyBeer)
    }

}
