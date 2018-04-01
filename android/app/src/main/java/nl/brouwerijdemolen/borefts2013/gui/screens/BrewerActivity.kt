package nl.brouwerijdemolen.borefts2013.gui.screens

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import nl.brouwerijdemolen.borefts2013.R
import nl.brouwerijdemolen.borefts2013.api.Brewer
import nl.brouwerijdemolen.borefts2013.ext.arg
import nl.brouwerijdemolen.borefts2013.ext.observeNonNull
import org.koin.android.architecture.ext.viewModel

class BrewerActivity : AppCompatActivity() {

    private val styleViewModel: StyleViewModel by viewModel(parameters = { mapOf(KEY_BREWER to arg(KEY_BREWER)) })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_list)
        styleViewModel.state.observeNonNull(this) {
            // TODO Create headed list
        }
    }

    companion object {
        const val KEY_BREWER = "brewer"

        operator fun invoke(context: Context, brewer: Brewer): Intent =
                Intent(context, BrewerActivity::class.java).putExtra(KEY_BREWER, brewer)
    }

}
