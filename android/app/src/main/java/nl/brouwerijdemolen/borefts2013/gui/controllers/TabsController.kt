package nl.brouwerijdemolen.borefts2013.gui.controllers

import android.graphics.Color
import android.support.design.widget.TabLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.support.RouterPagerAdapter
import nl.brouwerijdemolen.borefts2013.R
import nl.brouwerijdemolen.borefts2013.gui.components.getMolenString
import org.jetbrains.anko.*
import org.jetbrains.anko.design.tabLayout
import org.jetbrains.anko.support.v4.viewPager

class TabsController : Controller() {

    private lateinit var tabLayout: TabLayout

    override fun onCreateView(layoutInflater: LayoutInflater, parent: ViewGroup): View {
        return parent.verticalLayout {
            verticalLayout {
                elevation = 4F
                backgroundColor = Color.WHITE
                toolbar {
                    textView {
                        textSize = 20F
                        text = parent.context.getMolenString(R.string.app_name_short)
                    }
                }
                tabLayout = tabLayout {
                    horizontalPadding = dip(5)
                }
            }
            viewPager {
                adapter = TabPageAdapter(this@TabsController)
                tabLayout.setupWithViewPager(this)
            }
        }
    }

    class TabPageAdapter(host: Controller) : RouterPagerAdapter(host) {

        private val tabTitles = listOf("Info", "Brewers")

        private val tabs = listOf(
                InfoController(),
                BrewersController())

        override fun getCount() = 2

        override fun configureRouter(router: Router, position: Int) {
            if (!router.hasRootController()) {
                router.pushController(RouterTransaction.with(tabs[position]))
            }
        }

        override fun getPageTitle(position: Int): CharSequence {
            return tabTitles[position]
        }

    }

}