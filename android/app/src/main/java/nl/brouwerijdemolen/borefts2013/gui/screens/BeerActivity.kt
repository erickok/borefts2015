package nl.brouwerijdemolen.borefts2013.gui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_beer.abv_text
import kotlinx.android.synthetic.main.activity_beer.abv_view
import kotlinx.android.synthetic.main.activity_beer.acidity_view
import kotlinx.android.synthetic.main.activity_beer.beer_name_text
import kotlinx.android.synthetic.main.activity_beer.bitterness_view
import kotlinx.android.synthetic.main.activity_beer.brewer_button
import kotlinx.android.synthetic.main.activity_beer.google_button
import kotlinx.android.synthetic.main.activity_beer.serving_text
import kotlinx.android.synthetic.main.activity_beer.style_button
import kotlinx.android.synthetic.main.activity_beer.sweetness_view
import kotlinx.android.synthetic.main.activity_beer.tags_layout
import kotlinx.android.synthetic.main.activity_beer.title_toolbar
import kotlinx.android.synthetic.main.activity_beer.tostyle_text
import kotlinx.android.synthetic.main.activity_beer.untappd_button
import nl.brouwerijdemolen.borefts2013.R
import nl.brouwerijdemolen.borefts2013.api.Beer
import nl.brouwerijdemolen.borefts2013.ext.arg
import nl.brouwerijdemolen.borefts2013.ext.isVisible
import nl.brouwerijdemolen.borefts2013.ext.observeNonNull
import nl.brouwerijdemolen.borefts2013.ext.startLink
import nl.brouwerijdemolen.borefts2013.gui.abvIndication
import nl.brouwerijdemolen.borefts2013.gui.abvText
import nl.brouwerijdemolen.borefts2013.gui.acidityIndication
import nl.brouwerijdemolen.borefts2013.gui.bitternessIndication
import nl.brouwerijdemolen.borefts2013.gui.components.getMolenString
import nl.brouwerijdemolen.borefts2013.gui.hasAbv
import nl.brouwerijdemolen.borefts2013.gui.hasFlavourIndication
import nl.brouwerijdemolen.borefts2013.gui.servingText
import nl.brouwerijdemolen.borefts2013.gui.sweetnessIndication
import org.koin.android.architecture.ext.viewModel
import org.koin.android.ext.android.get

class BeerActivity : AppCompatActivity() {

    private val beerViewModel: BeerViewModel by viewModel(parameters = { mapOf(KEY_BEER to arg(KEY_BEER)) })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_beer)
        setupToolbar()

        beerViewModel.state.observeNonNull(this) {
            with(it) {
                beer_name_text.text = getMolenString(beer.name)
                brewer_button.text = beer.brewer.name
                style_button.text = beer.style.name
                abv_text.isVisible = beer.hasAbv
                abv_text.text = beer.abvText(get())
                serving_text.text = beer.servingText(get())
                abv_view.value = beer.abvIndication
                bitterness_view.value = beer.bitternessIndication
                sweetness_view.value = beer.sweetnessIndication
                acidity_view.value = beer.acidityIndication
                tostyle_text.isVisible = beer.hasFlavourIndication
                tags_layout
                untappd_button.setOnClickListener {
                    if (beer.untappdId <= 0) {
                        Toast.makeText(this@BeerActivity, R.string.error_notcoupled, Toast.LENGTH_LONG).show()
                    } else {
                        startLink(
                                Uri.parse("untappd://beer/${beer.untappdId}"),
                                Uri.parse("https://untappd.com/qr/beer/${beer.untappdId}"))
                    }
                }
                google_button.setOnClickListener {
                    startLink(Uri.parse("http://www.google.com/search?q=" + Uri.encode(beer.brewer.name + " " + beer.name)))
                }
            }
        }
    }

    private fun setupToolbar() {
        title_toolbar.setNavigationIcon(R.drawable.ic_back)
        title_toolbar.setNavigationOnClickListener { finish() }
        title_toolbar.inflateMenu(R.menu.activity_beer)
        title_toolbar.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.action_locate -> TODO()
                R.id.action_star_on -> TODO()
                R.id.action_star_off -> TODO()
            }
            true
        }
    }

    companion object {
        const val KEY_BEER = "beer"

        operator fun invoke(context: Context, beer: Beer): Intent =
                Intent(context, BeerActivity::class.java).putExtra(KEY_BEER, beer)
    }

}
