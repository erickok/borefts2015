package nl.brouwerijdemolen.borefts2013.gui.screens

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import nl.brouwerijdemolen.borefts2013.gui.Navigator
import nl.brouwerijdemolen.borefts2013.gui.TestWithKoin
import nl.brouwerijdemolen.borefts2013.gui.dummyBrewer
import nl.brouwerijdemolen.borefts2013.gui.dummyBrewers
import nl.brouwerijdemolen.borefts2013.test.Register
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.koin.standalone.inject
import org.koin.test.KoinTest
import org.mockito.Mockito.verify

class BrewersViewModelTest : KoinTest {

    @get:Rule var rule: TestRule = InstantTaskExecutorRule()

    private val brewersViewModel: BrewersViewModel by inject()
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
    fun `initialization emits styles state`() {
        val register = Register<BrewersUiModel>()
        brewersViewModel.state.observeForever(register)
        assertThat(register.values[0]).isInstanceOf(BrewersUiModel.Success::class.java)
        val result = (register.values[0] as BrewersUiModel.Success)
        assertThat(result.brewers).isEqualTo(dummyBrewers.brewers)
    }

    @Test
    fun `opening a brewer starts navigation`() {
        brewersViewModel.openBrewer(dummyBrewer)
        verify(mockNavigator).openBrewer(dummyBrewer)
    }

}
