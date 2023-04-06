package bali.rahul.ovale.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import bali.rahul.ovale.R
import bali.rahul.ovale.ui.collections.CollectionsFragment
import bali.rahul.ovale.ui.favourites.FavouritesFragment
import bali.rahul.ovale.ui.home.HomeFragment


internal class ViewPagerAdapter(
    fragmentManager: FragmentManager, lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    private val fragments = listOf(
        HomeFragment(), CollectionsFragment(), FavouritesFragment()
    )

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]

}

class OvaleOnPageChangeCallback : ViewPager2.OnPageChangeCallback() {
    override fun onPageSelected(position: Int) {
        // Handle page selected event
        super.onPageSelected(position)
        when (position) {
            0 -> R.id.navigation_home
            1 -> R.id.navigation_collections
            2 -> R.id.navigation_favourites
        }
    }
}
