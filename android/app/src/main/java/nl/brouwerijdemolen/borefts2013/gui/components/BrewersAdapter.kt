package nl.brouwerijdemolen.borefts2013.gui.components

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup

class LiveDataAdapter<T, V : View>(
        lifecycleOwner: LifecycleOwner,
        private val data: LiveData<List<T>>,
        private val viewBuilder: (ViewGroup) -> V,
        private val viewBinder: (V, T) -> Unit
) : RecyclerView.Adapter<LiveDataViewHolder<V>>() {

    init {
        data.observe(lifecycleOwner, Observer { notifyDataSetChanged() })
    }

    override fun getItemCount() = data.value?.size ?: 0

    override fun onCreateViewHolder(parent: ViewGroup, type: Int): LiveDataViewHolder<V> {
        return LiveDataViewHolder(viewBuilder(parent))
    }

    override fun onBindViewHolder(viewHolder: LiveDataViewHolder<V>, position: Int) {
        data.value?.get(position)?.let {
            viewBinder(viewHolder.typedItemView, it)
        }
    }

}

class LiveDataViewHolder<out V : View>(
        val typedItemView: V) : RecyclerView.ViewHolder(typedItemView)
