package nl.brouwerijdemolen.borefts2013.gui.screens

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import nl.brouwerijdemolen.borefts2013.gui.Navigator
import nl.brouwerijdemolen.borefts2013.gui.TestWithKoin
import nl.brouwerijdemolen.borefts2013.gui.dummyStyle
import nl.brouwerijdemolen.borefts2013.test.Register
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.koin.test.KoinTest
import org.koin.test.inject
import org.mockito.Mockito.verify

class StylesViewModelTest : KoinTest {

    @get:Rule var rule: TestRule = InstantTaskExecutorRule()

    private val stylesViewModel: StylesViewModel by inject()
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
        val register = Register<StylesUiModel>()
        stylesViewModel.state.observeForever(register)
        assertThat(register.values[0]).isInstanceOf(StylesUiModel.Success::class.java)
        val result = (register.values[0] as StylesUiModel.Success)
        assertThat(result.styles).isEqualTo(listOf(dummyStyle))
    }

    @Test
    fun `opening a style starts navigation`() {
        stylesViewModel.openStyle(dummyStyle)
        verify(mockNavigator).openStyle(dummyStyle)
    }

}
