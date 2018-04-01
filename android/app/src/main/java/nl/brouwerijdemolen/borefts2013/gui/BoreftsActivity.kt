package nl.brouwerijdemolen.borefts2013.gui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.tabs_tablayout
import kotlinx.android.synthetic.main.activity_main.tabs_viewpager
import kotlinx.android.synthetic.main.activity_main.title_text
import nl.brouwerijdemolen.borefts2013.R
import nl.brouwerijdemolen.borefts2013.gui.components.getMolenString
import nl.brouwerijdemolen.borefts2013.gui.screens.BafFragment
import nl.brouwerijdemolen.borefts2013.gui.screens.InfoFragment

class BoreftsActivity : AppCompatActivity() {

    private val infoFragment = InfoFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        title_text.text = this.getMolenString(title_text.text)
        setupViewPager()
    }

    private fun setupViewPager() {
        tabs_viewpager.adapter = TabsAdapter()
        tabs_tablayout.setupWithViewPager(tabs_viewpager)
    }

    inner class TabsAdapter : FragmentStatePagerAdapter(supportFragmentManager) {

        override fun getCount() = 6

        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> infoFragment
                else -> BafFragment()
            }
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return when (position) {
                0 -> getString(R.string.action_info)
                1 -> getString(R.string.action_brewers)
                2 -> getString(R.string.action_styles)
                3 -> getString(R.string.action_stars)
                4 -> getString(R.string.action_twitter)
                else -> getString(R.string.info_baf)
            }
        }

    }

}
