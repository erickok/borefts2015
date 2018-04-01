package nl.brouwerijdemolen.borefts2013.gui.components

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.Transformations
import android.support.annotation.MainThread

@MainThread
fun <X, Y> LiveData<X>.map(func: (X) -> Y): LiveData<Y> = Transformations.map(this, func)

@MainThread
fun <Y> LiveData<Y>.filter(predicate: (Y) -> Boolean): LiveData<Y> {
    val source = this
    return MediatorLiveData<Y>().apply {
        addSource(source, { x -> x?.takeIf { predicate(x) }?.let { postValue(it) } })
    }
}

//@MainThread
//fun <Y> LiveData<Try<Y>>.filterSuccess(): LiveData<Y> {
//    return this.filter { it is Try.Success }.map { (it as Try.Success).value }
//}

@MainThread
fun <Y> LiveData<List<Y>>.filterList(predicate: (Y) -> Boolean): LiveData<List<Y>> {
    return this.map { it.filter(predicate) }
}
