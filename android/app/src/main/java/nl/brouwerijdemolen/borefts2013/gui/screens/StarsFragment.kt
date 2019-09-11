package nl.brouwerijdemolen.borefts2013.gui.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_list.*
import nl.brouwerijdemolen.borefts2013.R
import nl.brouwerijdemolen.borefts2013.ext.isVisible
import nl.brouwerijdemolen.borefts2013.ext.observeNonNull
import org.koin.androidx.viewmodel.ext.android.viewModel

class StarsFragment : Fragment() {

    private val viewModel: StarsViewModel by viewModel()
    private lateinit var beersListAdapter: BeersListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        beersListAdapter = BeersListAdapter(false, viewModel::openBeer)
        the_list.adapter = beersListAdapter
        error_text.setText(R.string.info_nostars)
        viewModel.state.observeNonNull(this) {
            loading_progress.isVisible = it == StarsUiModel.Loading
            the_list.isVisible = it is StarsUiModel.Success
            error_text.isVisible = it == StarsUiModel.Failure || it == StarsUiModel.Empty
            if (it is StarsUiModel.Success) {
                beersListAdapter.submitList(it.beers)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.refresh()
    }

}
