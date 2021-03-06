package nl.brouwerijdemolen.borefts2013.gui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_beer.*
import nl.brouwerijdemolen.borefts2013.R
import nl.brouwerijdemolen.borefts2013.api.Beer
import nl.brouwerijdemolen.borefts2013.ext.*
import nl.brouwerijdemolen.borefts2013.gui.*
import nl.brouwerijdemolen.borefts2013.gui.components.getMolenString
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.util.*

class BeerActivity : AppCompatActivity() {

    private val beerViewModel: BeerViewModel by viewModel { parametersOf(arg(KEY_ARGS)) }

    private lateinit var actionStarOn: MenuItem
    private lateinit var actionStarOff: MenuItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_beer)
        setupToolbar()

        beerViewModel.state.observeNonNull(this) {
            with(it) {
                beer_name_text.text = getMolenString(beer.fullName)
                brewer_button.text = beer.brewer?.name
                brewer_button.setOnClickListener { beerViewModel.openBrewer() }
                style_button.text = beer.style?.name
                style_button.setOnClickListener { beerViewModel.openStyle() }
                abv_text.isVisible = beer.hasAbv
                abv_text.text = beer.abvText(get())
                serving_text.text = beer.servingText(get()).toUpperCase(Locale.getDefault())
                abv_view.value = beer.abvIndication
                color_view.setBackgroundColor(beer.colorIndicationResource(get()))
                bitterness_view.value = beer.bitternessIndication
                sweetness_view.value = beer.sweetnessIndication
                acidity_view.value = beer.acidityIndication
                tostyle_text.isVisible = !beer.hasFlavourIndication
                val customTags = beer.tags?.split(',').orEmpty()
                val tags = if (beer.oakAged) listOf("barrel aged") + customTags else customTags
                tags_layout.isVisible = tags.isNotEmpty()
                tags_layout.removeAllViews()
                tags.forEach { tag ->
                    if (tag.isNotBlank()) {
                        layoutInflater.inflate(R.layout.widget_label, tags_layout, true)
                        (tags_layout.getChildAt(tags_layout.childCount - 1) as TextView).apply {
                            this.text = tag.toUpperCase(Locale.getDefault())
                        }
                    }
                }
                untappd_button.setOnClickListener {
                    if (beer.untappdId.isNullOrEmpty() || beer.untappdId == "-1") {
                        Toast.makeText(this@BeerActivity, R.string.error_notcoupled, Toast.LENGTH_LONG).show()
                    } else {
                        startLink(
                                Uri.parse("untappd://beer/${beer.untappdId}"),
                                Uri.parse("https://untappd.com/qr/beer/${beer.untappdId}"))
                    }
                }
                google_button.setOnClickListener {
                    startLink(Uri.parse("http://www.google.com/search?q=" + Uri.encode(beer.brewer?.name + " " + beer.name)))
                }
                actionStarOn.isVisible = isStarred
                actionStarOff.isVisible = !isStarred
            }
        }
    }

    private fun setupToolbar() {
        title_toolbar.setNavigationIcon(R.drawable.ic_back)
        title_toolbar.setNavigationOnClickListener { finish() }
        title_toolbar.inflateMenu(R.menu.activity_beer)
        actionStarOn = title_toolbar.menu.findItem(R.id.action_star_on)
        actionStarOff = title_toolbar.menu.findItem(R.id.action_star_off)
        title_toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_locate -> beerViewModel.locateBrewer()
                R.id.action_star_on -> beerViewModel.updateStar(false)
                R.id.action_star_off -> beerViewModel.updateStar(true)
            }
            true
        }
    }

    companion object {
        operator fun invoke(context: Context, beer: Beer): Intent =
                Intent(context, BeerActivity::class.java).putExtra(KEY_ARGS, beer)
    }

}
