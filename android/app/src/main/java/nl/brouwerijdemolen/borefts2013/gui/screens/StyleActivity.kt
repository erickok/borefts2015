package nl.brouwerijdemolen.borefts2013.gui.screens

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import nl.brouwerijdemolen.borefts2013.R
import nl.brouwerijdemolen.borefts2013.api.Style
import nl.brouwerijdemolen.borefts2013.ext.arg
import nl.brouwerijdemolen.borefts2013.ext.observeNonNull
import org.koin.android.architecture.ext.viewModel

class StyleActivity : AppCompatActivity() {

    private val styleViewModel: StyleViewModel by viewModel(parameters = {
        mapOf(KEY_STYLE to arg(KEY_STYLE)) }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_list)
        styleViewModel.state.observeNonNull(this) {
            // TODO Create headed list
        }
    }

    companion object {
        const val KEY_STYLE = "style"

        operator fun invoke(context: Context, style: Style): Intent =
                Intent(context, StyleActivity::class.java).putExtra(KEY_STYLE, style)
    }

}
