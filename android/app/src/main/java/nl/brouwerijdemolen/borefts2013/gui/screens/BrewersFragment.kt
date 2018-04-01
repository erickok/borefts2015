package nl.brouwerijdemolen.borefts2013.gui.screens

import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.fragment_list.error_text
import kotlinx.android.synthetic.main.fragment_list.loading_progress
import kotlinx.android.synthetic.main.fragment_list.the_list
import kotlinx.android.synthetic.main.list_item_brewer.logo_image
import kotlinx.android.synthetic.main.list_item_brewer.name_text
import kotlinx.android.synthetic.main.list_item_brewer.origin_text
import nl.brouwerijdemolen.borefts2013.R
import nl.brouwerijdemolen.borefts2013.api.Brewer
import nl.brouwerijdemolen.borefts2013.ext.isVisible
import nl.brouwerijdemolen.borefts2013.ext.observeNonNull
import org.koin.android.architecture.ext.viewModel
import java.io.IOException


class BrewersFragment : Fragment() {

    private val viewModel: BrewersViewModel by viewModel()
    private lateinit var brewersListAdapter: BrewersListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        brewersListAdapter = BrewersListAdapter { openBrewer(it) }
        the_list.adapter = brewersListAdapter
        viewModel.state.observeNonNull(this) {
            loading_progress.isVisible = it == BrewersUiModel.Loading
            error_text.isVisible = it is BrewersUiModel.Failure
            the_list.isVisible = it is BrewersUiModel.Success
            if (it is BrewersUiModel.Success) {
                brewersListAdapter.submitList(it.brewers)
            }
        }
    }

    private fun openBrewer(brewer: Brewer) {
        startActivity(BrewerActivity(requireContext(), brewer))
    }

}

class BrewersListAdapter(private val brewerClicked: (Brewer) -> Unit) : ListAdapter<Brewer, BrewersListAdapter.BrewersViewHolder>(BrewersDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BrewersViewHolder {
        return BrewersViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_brewer, parent, false))
    }

    override fun onBindViewHolder(holder: BrewersViewHolder, position: Int) {
        holder.bind(getItem(position), brewerClicked)
    }

    object BrewersDiffCallback : DiffUtil.ItemCallback<Brewer>() {
        override fun areItemsTheSame(oldItem: Brewer?, newItem: Brewer?) = oldItem?.id == newItem?.id
        override fun areContentsTheSame(oldItem: Brewer?, newItem: Brewer?) = oldItem == newItem

    }

    class BrewersViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bind(brewer: Brewer, brewerClicked: (Brewer) -> Unit) {
            name_text.text = brewer.name
            origin_text.text = containerView.resources.getString(R.string.info_origin, brewer.city, brewer.country)
            try {
                logo_image.setImageBitmap(BitmapFactory.decodeStream(containerView.resources.assets.open("images/" + brewer.logoUrl)))
            } catch (e: IOException) {
                // Should never happen, as the brewer logo always exists locally
                Log.e("BrewersViewHolder", "Missing brewer icon drawable", e)
                logo_image.setImageDrawable(null)
            }
            containerView.setOnClickListener { brewerClicked(brewer) }
        }

    }

}
