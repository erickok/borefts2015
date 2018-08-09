package nl.brouwerijdemolen.borefts2013.gui.screens

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import nl.brouwerijdemolen.borefts2013.gui.Navigator
import nl.brouwerijdemolen.borefts2013.gui.TestWithKoin
import nl.brouwerijdemolen.borefts2013.gui.dummyBrewer
import nl.brouwerijdemolen.borefts2013.gui.dummyBrewers
import nl.brouwerijdemolen.borefts2013.gui.dummyPois
import nl.brouwerijdemolen.borefts2013.test.Register
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.koin.core.parameter.parametersOf
import org.koin.standalone.inject
import org.koin.test.KoinTest
import org.mockito.Mockito.verify

class MapViewModelTest : KoinTest {

    @get:Rule var rule: TestRule = InstantTaskExecutorRule()

    private val infoViewModel: MapViewModel by inject { parametersOf(MapViewModelArgs()) }
    private val infoViewModelWithBrewerFocus: MapViewModel by inject { parametersOf(MapViewModelArgs(focusBrewerId = 2)) }
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
    fun `initialization emits info success state`() {
        val register = Register<MapUiModel>()
        infoViewModel.state.observeForever(register)
        assertThat(register.values[0]).isInstanceOf(MapUiModel.Success::class.java)
        val result = (register.values[0] as MapUiModel.Success)
        assertThat(result.brewers).isEqualTo(dummyBrewers.brewers)
        assertThat(result.areas).isEqualTo(dummyPois.areas)
        assertThat(result.pois).isEqualTo(dummyPois.pois)
        assertThat(result.focusBrewerId).isNull()
        assertThat(result.focusPoiId).isNull()
    }

    @Test
    fun `initialization with brewer focus emits state with focus`() {
        val register = Register<MapUiModel>()
        infoViewModelWithBrewerFocus.state.observeForever(register)
        assertThat(register.values[0]).isInstanceOf(MapUiModel.Success::class.java)
        val result = (register.values[0] as MapUiModel.Success)
        assertThat(result.brewers).isEqualTo(dummyBrewers.brewers)
        assertThat(result.areas).isEqualTo(dummyPois.areas)
        assertThat(result.pois).isEqualTo(dummyPois.pois)
        assertThat(result.focusBrewerId).isEqualTo(2)
        assertThat(result.focusPoiId).isNull()
    }

    @Test
    fun `opening a brewer starts navigation`() {
        infoViewModel.openBrewer(dummyBrewer)
        verify(mockNavigator).openBrewer(dummyBrewer)
    }

}
