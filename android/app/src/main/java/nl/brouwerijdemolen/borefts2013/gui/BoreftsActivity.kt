package nl.brouwerijdemolen.borefts2013.gui

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import nl.brouwerijdemolen.borefts2013.R
import nl.brouwerijdemolen.borefts2013.ext.startLink
import nl.brouwerijdemolen.borefts2013.gui.components.AppRater
import nl.brouwerijdemolen.borefts2013.gui.components.Exporter
import nl.brouwerijdemolen.borefts2013.gui.components.getMolenString
import nl.brouwerijdemolen.borefts2013.gui.screens.*
import org.koin.android.ext.android.inject
import java.io.File

class BoreftsActivity : AppCompatActivity() {

    private val appRater: AppRater by inject()
    private val infoFragment by lazy { InfoFragment() }
    private val brewersFragment by lazy { BrewersFragment() }
    private val stylesFragment by lazy { StylesFragment() }
    private val starsFragment by lazy { StarsFragment() }
    private val twitterFragment by lazy { TwitterFragment() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupViewPager()
        setupToolbar()
        hitAppRater()
    }

    private fun setupToolbar() {
        title_text.text = getMolenString(title_text.text)
        title_toobar.inflateMenu(R.menu.activity_start)
        // Only show refresh button on Twitter page
        tabs_viewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            val refreshMenuItem = title_toobar.menu.findItem(R.id.action_refresh)
            override fun onPageScrollStateChanged(state: Int) = Unit
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) = Unit
            override fun onPageSelected(position: Int) {
                refreshMenuItem.isVisible = position == 4
            }
        })
        // Handle toolbar menu clicks
        title_toobar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_refresh -> twitterFragment.refreshFeed()
                R.id.action_export -> prepareBeersExport()
                R.id.action_sendcorrection -> prepareCorrectionEmail()
                R.id.action_about -> AboutFragment().show(supportFragmentManager, "about")
            }
            true
        }
    }

    private fun setupViewPager() {
        tabs_viewpager.adapter = TabsAdapter()
        tabs_tablayout.setupWithViewPager(tabs_viewpager)
    }

    private fun hitAppRater() {
        appRater.hit()
        if (appRater.shouldShow()) {
            Snackbar.make(tabs_viewpager, R.string.rate_title, 5_000).apply {
                setActionTextColor(ContextCompat.getColor(this@BoreftsActivity, R.color.yellow))
                setAction(R.string.rate_confirm) {
                    appRater.block()
                    startLink(Uri.parse("market://details?id=nl.brouwerijdemolen.borefts2013"))
                }
                show()
            }
        }
    }

    private fun prepareBeersExport() = GlobalScope.launch {
        val csv = File(cacheDir, "export/borefts2019_export.csv")
        Exporter().writeTo(csv)
        val content = FileProvider.getUriForFile(this@BoreftsActivity, "nl.brouwerijdemolen.borefts2013.fileprovider", csv)
        val shareIntent = Intent(Intent.ACTION_SEND)
                .setDataAndType(content, "text/csv")
                .setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                .putExtra(Intent.EXTRA_STREAM, content)
        startActivity(Intent.createChooser(shareIntent, getString(R.string.action_export)))

    }

    private fun prepareCorrectionEmail() {
        val startEmail = Intent(Intent.ACTION_SEND).apply {
            type = "message/rfc822"
            putExtra(Intent.EXTRA_EMAIL, arrayOf("borefts2019@2312.nl"))
            putExtra(Intent.EXTRA_SUBJECT, "Borefts 2019 Android app correction")
        }
        try {
            startActivity(startEmail)
        } catch (e: ActivityNotFoundException) {
            // Ignore; normal devices always have an app to send emails, but at least do not crash
        }

    }

    inner class TabsAdapter : FragmentStatePagerAdapter(supportFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        override fun getCount() = 5

        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> infoFragment
                1 -> brewersFragment
                2 -> stylesFragment
                3 -> starsFragment
                else -> twitterFragment
            }
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return when (position) {
                0 -> getString(R.string.action_info)
                1 -> getString(R.string.action_brewers)
                2 -> getString(R.string.action_styles)
                3 -> getString(R.string.action_stars)
                else -> getString(R.string.action_twitter)
            }
        }

    }

}
