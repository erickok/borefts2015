package nl.brouwerijdemolen.borefts2013.gui.screens

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_brewer.title_toolbar
import kotlinx.android.synthetic.main.activity_style.abv_view
import kotlinx.android.synthetic.main.activity_style.acidity_view
import kotlinx.android.synthetic.main.activity_style.beers_list
import kotlinx.android.synthetic.main.activity_style.bitterness_view
import kotlinx.android.synthetic.main.activity_style.color_view
import kotlinx.android.synthetic.main.activity_style.sweetness_view
import kotlinx.android.synthetic.main.activity_style.title_text
import nl.brouwerijdemolen.borefts2013.R
import nl.brouwerijdemolen.borefts2013.api.Beer
import nl.brouwerijdemolen.borefts2013.api.Style
import nl.brouwerijdemolen.borefts2013.ext.arg
import nl.brouwerijdemolen.borefts2013.ext.observeNonNull
import nl.brouwerijdemolen.borefts2013.gui.components.getMolenString
import nl.brouwerijdemolen.borefts2013.gui.getColorResource
import org.koin.android.architecture.ext.viewModel
import org.koin.android.ext.android.get

class StyleActivity : AppCompatActivity() {

    private val styleViewModel: StyleViewModel by viewModel(parameters = { mapOf(KEY_STYLE to arg(KEY_STYLE)) })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_style)
        setupToolbar()

        styleViewModel.state.observeNonNull(this) {
            title_text.text = this.getMolenString(it.style.name)
            color_view.setBackgroundColor(it.style.getColorResource(get()))
            abv_view.value = it.style.abv
            bitterness_view.value = it.style.bitterness
            sweetness_view.value = it.style.sweetness
            acidity_view.value = it.style.abv
            beers_list.adapter = BeersListAdapter(false, ::openBeer).apply { submitList(it.beers) }
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
        const val KEY_STYLE = "style"

        operator fun invoke(context: Context, style: Style): Intent =
                Intent(context, StyleActivity::class.java).putExtra(KEY_STYLE, style)
    }

}
