package nl.brouwerijdemolen.borefts2013.gui.screens

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import nl.brouwerijdemolen.borefts2013.api.Beer
import nl.brouwerijdemolen.borefts2013.api.Brewer
import nl.brouwerijdemolen.borefts2013.api.Style
import nl.brouwerijdemolen.borefts2013.gui.Navigator
import nl.brouwerijdemolen.borefts2013.gui.TestWithKoin
import nl.brouwerijdemolen.borefts2013.gui.components.StarPersistence
import nl.brouwerijdemolen.borefts2013.gui.testModule
import nl.brouwerijdemolen.borefts2013.gui.viewModelsModule
import nl.brouwerijdemolen.borefts2013.test.Register
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module.module
import org.koin.standalone.get
import org.koin.test.KoinTest

class ScreenIntegrationTest : KoinTest {

    @get:Rule var rule: TestRule = InstantTaskExecutorRule()

    private val testNavigator: Navigator = mock()
    private val testStarPersistence: StarPersistence = mock()

    @Before
    fun `setup dependencies`() {
        TestWithKoin.startKoin(listOf(testModule, viewModelsModule, module(override = true) {
            // Mocks to control the flow
            single { testNavigator }
            single { testStarPersistence }
        }))
    }

    @After
    fun `clean up dependencies`() {
        TestWithKoin.stopKoin()
    }

    @Test
    fun `info screen allows opening map`() {
        val infoViewModel = get<InfoViewModel>()
        whenever(testNavigator.openMap()).then { `map screen allows opening brewer`() }
        infoViewModel.openMap()
    }

    private fun `map screen allows opening brewer`() {
        val mapViewModel = get<MapViewModel> { parametersOf(MapViewModelArgs()) }
        val mapUiModelRegister = Register<MapUiModel>()
        mapViewModel.state.observeForever(mapUiModelRegister)
        val brewerToOpen = (mapUiModelRegister.lastValue!! as MapUiModel.Success).brewers[0]

        whenever(testNavigator.openBrewer(brewerToOpen)).then { `brewer screen allows opening beer`(it.arguments[0] as Brewer) }
        mapViewModel.openBrewer(brewerToOpen)
    }

    private fun `brewer screen allows opening beer`(brewer: Brewer) {
        val brewerViewModel = get<BrewerViewModel> { parametersOf(brewer) }
        val brewerUiModelRegister = Register<BrewerUiModel>()
        brewerViewModel.state.observeForever(brewerUiModelRegister)
        val beerToOpen = brewerUiModelRegister.lastValue!!.beers[0]

        whenever(testNavigator.openBeer(beerToOpen)).then { `beer screen allows opening style`(it.arguments[0] as Beer) }
        brewerViewModel.openBeer(beerToOpen)
    }

    private fun `beer screen allows opening style`(beer: Beer) {
        val beerViewModel = get<BeerViewModel> { parametersOf(beer) }
        val beerUiModelRegister = Register<BeerUiModel>()
        beerViewModel.state.observeForever(beerUiModelRegister)
        val styleToOpen = beerUiModelRegister.lastValue!!.beer.style!!

        beerViewModel.openStyle()
        verify(testNavigator).openStyle(styleToOpen)
    }

    @Test
    fun `styles screen allows opening style`() {
        val stylesViewModel = get<StylesViewModel>()
        val stylesUiModelRegister = Register<StylesUiModel>()
        stylesViewModel.state.observeForever(stylesUiModelRegister)
        val styleToOpen = (stylesUiModelRegister.lastValue as StylesUiModel.Success).styles[0]

        whenever(testNavigator.openStyle(styleToOpen)).then { `style screen allows opening beer`(it.arguments[0] as Style) }
        stylesViewModel.openStyle(styleToOpen)
    }

    private fun `style screen allows opening beer`(style: Style) {
        val styleViewModel = get<StyleViewModel> { parametersOf(style) }
        val styleUiModelRegister = Register<StyleUiModel>()
        styleViewModel.state.observeForever(styleUiModelRegister)
        val beerToOpen = styleUiModelRegister.lastValue!!.beers[0]

        whenever(testNavigator.openBeer(beerToOpen)).then { `beer screen allows opening brewer`(it.arguments[0] as Beer) }
        styleViewModel.openBeer(beerToOpen)
        verify(testNavigator).openBeer(beerToOpen)
    }

    private fun `beer screen allows opening brewer`(beer: Beer) {
        val beerViewModel = get<BeerViewModel> { parametersOf(beer) }
        val beerUiModelRegister = Register<BeerUiModel>()
        beerViewModel.state.observeForever(beerUiModelRegister)
        val brewerToOpen = beerUiModelRegister.lastValue!!.beer.brewer!!

        beerViewModel.openBrewer()
        verify(testNavigator).openBrewer(brewerToOpen)
    }

    @Test
    fun `brewers screen allows opening brewer`() {
        val brewersViewModel = get<BrewersViewModel>()
        val brewersUiModelRegister = Register<BrewersUiModel>()
        brewersViewModel.state.observeForever(brewersUiModelRegister)
        val brewerToOpen = (brewersUiModelRegister.lastValue as BrewersUiModel.Success).brewers[0]

        whenever(testNavigator.openBrewer(brewerToOpen)).then { `brewer screen allows opening beer`(it.arguments[0] as Brewer) }
        brewersViewModel.openBrewer(brewerToOpen)
    }

    @Test
    fun `stars screen with stars allows opening beer`() {
        // Mock a single starred beer (with id 1)
        whenever(testStarPersistence.allStars).thenReturn(hashSetOf(1))

        val starsViewModel = get<StarsViewModel>()
        val starsUiModelRegister = Register<StarsUiModel>()
        starsViewModel.state.observeForever(starsUiModelRegister)
        starsViewModel.refresh()
        val beerToOpen = (starsUiModelRegister.lastValue as StarsUiModel.Success).beers[0]

        starsViewModel.openBeer(beerToOpen)
        verify(testNavigator).openBeer(beerToOpen)
    }

}
