package nl.brouwerijdemolen.borefts2013.gui.screens

import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.list_item_beer.abv_text
import kotlinx.android.synthetic.main.list_item_beer.name_text
import kotlinx.android.synthetic.main.list_item_beer.style_view
import kotlinx.android.synthetic.main.list_item_beer.stylebrewer_text
import nl.brouwerijdemolen.borefts2013.R
import nl.brouwerijdemolen.borefts2013.api.Beer
import nl.brouwerijdemolen.borefts2013.ext.isVisible
import nl.brouwerijdemolen.borefts2013.gui.abvText
import nl.brouwerijdemolen.borefts2013.gui.colorIndicationResource
import nl.brouwerijdemolen.borefts2013.gui.fullName
import nl.brouwerijdemolen.borefts2013.gui.hasAbv
import org.koin.standalone.KoinComponent
import org.koin.standalone.get

class BeersListAdapter(
        private val showStyle: Boolean,
        private val beerClicked: (Beer) -> Unit) : ListAdapter<Beer, BeersListAdapter.BeerViewHolder>(BeersDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BeerViewHolder {
        return BeerViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_beer, parent, false))
    }

    override fun onBindViewHolder(holder: BeerViewHolder, position: Int) {
        holder.bind(getItem(position), showStyle, beerClicked)
    }

    object BeersDiffCallback : DiffUtil.ItemCallback<Beer>() {
        override fun areItemsTheSame(oldItem: Beer, newItem: Beer) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Beer, newItem: Beer) = oldItem == newItem
    }

    class BeerViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer, KoinComponent {

        fun bind(beer: Beer, showStyle: Boolean, beerClicked: (Beer) -> Unit) {
            name_text.text = beer.fullName
            stylebrewer_text.text = if (showStyle) beer.style?.name else beer.brewer?.name
            abv_text.isVisible = beer.hasAbv
            abv_text.text = beer.abvText(get())
            style_view.setBackgroundColor(beer.colorIndicationResource(get()))
            containerView.setOnClickListener { beerClicked(beer) }
        }

    }

}
