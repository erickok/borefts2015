package nl.brouwerijdemolen.borefts2013.gui.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import coil.transform.CircleCropTransformation
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.fragment_list.*
import kotlinx.android.synthetic.main.list_item_brewer.*
import nl.brouwerijdemolen.borefts2013.R
import nl.brouwerijdemolen.borefts2013.api.Brewer
import nl.brouwerijdemolen.borefts2013.ext.isVisible
import nl.brouwerijdemolen.borefts2013.ext.observeNonNull
import nl.brouwerijdemolen.borefts2013.gui.location
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.KoinComponent
import org.koin.core.get

class BrewersFragment : Fragment() {

    private val viewModel: BrewersViewModel by viewModel()
    private lateinit var brewersListAdapter: BrewersListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        brewersListAdapter = BrewersListAdapter(viewModel::openBrewer)
        the_list.adapter = brewersListAdapter
        viewModel.state.observeNonNull(this) {
            loading_progress.isVisible = it == BrewersUiModel.Loading
            error_text.isVisible = it === BrewersUiModel.Failure
            the_list.isVisible = it is BrewersUiModel.Success
            if (it is BrewersUiModel.Success) {
                brewersListAdapter.submitList(it.brewers)
            }
        }
        error_text.setOnClickListener { _ -> viewModel.retry() }
    }

}

class BrewersListAdapter(
        private val brewerClicked: (Brewer) -> Unit) : ListAdapter<Brewer, BrewersListAdapter.BrewersViewHolder>(BrewersDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BrewersViewHolder {
        return BrewersViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_brewer, parent, false))
    }

    override fun onBindViewHolder(holder: BrewersViewHolder, position: Int) {
        holder.bind(getItem(position), brewerClicked)
    }

    object BrewersDiffCallback : DiffUtil.ItemCallback<Brewer>() {
        override fun areItemsTheSame(oldItem: Brewer, newItem: Brewer) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Brewer, newItem: Brewer) = oldItem == newItem

    }

    class BrewersViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer, KoinComponent {

        fun bind(brewer: Brewer, brewerClicked: (Brewer) -> Unit) {
            name_text.text = brewer.name
            origin_text.text = brewer.location(get())
            logo_image.load(brewer.logoUrl) {
                transformations(CircleCropTransformation())
            }
            containerView.setOnClickListener { brewerClicked(brewer) }
        }

    }

}
