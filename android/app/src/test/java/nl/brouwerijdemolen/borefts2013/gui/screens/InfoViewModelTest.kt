package nl.brouwerijdemolen.borefts2013.gui.screens

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import nl.brouwerijdemolen.borefts2013.gui.Navigator
import nl.brouwerijdemolen.borefts2013.gui.TestWithKoin
import nl.brouwerijdemolen.borefts2013.gui.dummyBrewers
import nl.brouwerijdemolen.borefts2013.gui.dummyPois
import nl.brouwerijdemolen.borefts2013.test.Register
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.koin.standalone.inject
import org.koin.test.KoinTest
import org.mockito.Mockito.verify

class InfoViewModelTest : KoinTest {

    @get:Rule var rule: TestRule = InstantTaskExecutorRule()

    private val infoViewModel: InfoViewModel by inject()
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
        val register = Register<InfoUiModel>()
        infoViewModel.state.observeForever(register)
        assertThat(register.values[0]).isInstanceOf(InfoUiModel.Success::class.java)
        val result = (register.values[0] as InfoUiModel.Success)
        assertThat(result.brewers).isEqualTo(dummyBrewers.brewers)
        assertThat(result.areas).isEqualTo(dummyPois.areas)
        assertThat(result.pois).isEqualTo(dummyPois.pois)
    }

    @Test
    fun `opening map starts navigation`() {
        infoViewModel.openMap()
        verify(mockNavigator).openMap()
    }

    @Test
    fun `locating a poi starts navigation`() {
        infoViewModel.openMap("poiId")
        verify(mockNavigator).openMapForPoi("poiId")
    }

}
