package nl.brouwerijdemolen.borefts2013.gui.screens

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_brewer.beers_list
import kotlinx.android.synthetic.main.activity_brewer.logo_image
import kotlinx.android.synthetic.main.activity_brewer.origin_text
import kotlinx.android.synthetic.main.activity_brewer.title_text
import kotlinx.android.synthetic.main.activity_brewer.title_toolbar
import kotlinx.android.synthetic.main.activity_brewer.weblink_text
import nl.brouwerijdemolen.borefts2013.R
import nl.brouwerijdemolen.borefts2013.api.Beer
import nl.brouwerijdemolen.borefts2013.api.Brewer
import nl.brouwerijdemolen.borefts2013.ext.arg
import nl.brouwerijdemolen.borefts2013.ext.observeNonNull
import nl.brouwerijdemolen.borefts2013.gui.components.getMolenString
import nl.brouwerijdemolen.borefts2013.gui.location
import nl.brouwerijdemolen.borefts2013.gui.logoBitmap
import org.koin.android.architecture.ext.viewModel
import org.koin.android.ext.android.get

class BrewerActivity : AppCompatActivity() {

    private val brewerViewModel: BrewerViewModel by viewModel(parameters = { mapOf(KEY_BREWER to arg(KEY_BREWER)) })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_brewer)
        setupToolbar()

        brewerViewModel.state.observeNonNull(this) {
            title_text.text = this.getMolenString(it.brewer.name)
            origin_text.text = it.brewer.location(get())
            weblink_text.text = it.brewer.website
            logo_image.setImageBitmap(it.brewer.logoBitmap(get()))
            beers_list.adapter = BeersListAdapter(true, ::openBeer).apply { submitList(it.beers) }
        }
    }

    private fun openBeer(beer: Beer) {
        startActivity(BeerActivity(this, beer))
    }

    private fun setupToolbar() {
        title_toolbar.setNavigationIcon(R.drawable.ic_back)
        title_toolbar.setNavigationOnClickListener { finish() }
    }

    companion object {
        const val KEY_BREWER = "brewer"

        operator fun invoke(context: Context, brewer: Brewer): Intent =
                Intent(context, BrewerActivity::class.java).putExtra(KEY_BREWER, brewer)
    }

}
