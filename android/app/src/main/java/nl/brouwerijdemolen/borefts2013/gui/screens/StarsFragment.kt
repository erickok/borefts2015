package nl.brouwerijdemolen.borefts2013.gui.screens

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_list.*
import nl.brouwerijdemolen.borefts2013.R
import nl.brouwerijdemolen.borefts2013.api.Beer
import nl.brouwerijdemolen.borefts2013.ext.isVisible
import nl.brouwerijdemolen.borefts2013.ext.observeNonNull
import org.koin.android.architecture.ext.viewModel

class StarsFragment : Fragment() {

    private val viewModel: StarsViewModel by viewModel()
    private lateinit var beersListAdapter: BeersListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        beersListAdapter = BeersListAdapter(false) { openBeer(it) }
        the_list.adapter = beersListAdapter
        viewModel.state.observeNonNull(this) {
            loading_progress.isVisible = it == StarsUiModel.Loading
            error_text.isVisible = it === StarsUiModel.Failure
            error_text.setText(R.string.info_nostars)
            the_list.isVisible = it is StarsUiModel.Success
            if (it is StarsUiModel.Success) {
                beersListAdapter.submitList(it.beers)
            }
        }
    }

    private fun openBeer(beer: Beer) {
        startActivity(BeerActivity(requireContext(), beer))
    }

}
