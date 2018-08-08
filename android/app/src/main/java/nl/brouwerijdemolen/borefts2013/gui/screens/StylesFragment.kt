package nl.brouwerijdemolen.borefts2013.gui.screens

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.fragment_list.error_text
import kotlinx.android.synthetic.main.fragment_list.loading_progress
import kotlinx.android.synthetic.main.fragment_list.the_list
import kotlinx.android.synthetic.main.list_item_style.color_view
import kotlinx.android.synthetic.main.list_item_style.name_text
import nl.brouwerijdemolen.borefts2013.R
import nl.brouwerijdemolen.borefts2013.api.Style
import nl.brouwerijdemolen.borefts2013.ext.isVisible
import nl.brouwerijdemolen.borefts2013.ext.observeNonNull
import nl.brouwerijdemolen.borefts2013.gui.getColorResource
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.standalone.KoinComponent
import org.koin.standalone.get

class StylesFragment : Fragment() {

    private val viewModel: StylesViewModel by viewModel { parametersOf(requireActivity()) }
    private lateinit var stylesListAdapter: StylesListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        stylesListAdapter = StylesListAdapter(viewModel::openStyle)
        the_list.adapter = stylesListAdapter
        viewModel.state.observeNonNull(this) {
            loading_progress.isVisible = it == StylesUiModel.Loading
            error_text.isVisible = it === StylesUiModel.Failure
            the_list.isVisible = it is StylesUiModel.Success
            if (it is StylesUiModel.Success) {
                stylesListAdapter.submitList(it.styles)
            }
        }
    }

}

class StylesListAdapter(
        private val styleClicked: (Style) -> Unit) : ListAdapter<Style, StylesListAdapter.StyleViewHolder>(StylesDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StyleViewHolder {
        return StyleViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_style, parent, false))
    }

    override fun onBindViewHolder(holder: StyleViewHolder, position: Int) {
        holder.bind(getItem(position), styleClicked)
    }

    object StylesDiffCallback : DiffUtil.ItemCallback<Style>() {
        override fun areItemsTheSame(oldItem: Style?, newItem: Style?) = oldItem?.id == newItem?.id
        override fun areContentsTheSame(oldItem: Style?, newItem: Style?) = oldItem == newItem

    }

    class StyleViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer, KoinComponent {

        fun bind(style: Style, styleClicked: (Style) -> Unit) {
            name_text.text = style.name
            color_view.setBackgroundColor(style.getColorResource(get()))
            containerView.setOnClickListener { styleClicked(style) }
        }

    }

}
