package bali.rahul.ovale.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPager2Adapter(fragmentActivity: FragmentActivity, fragmentArray: Array<Fragment>) :
    FragmentStateAdapter(fragmentActivity) {

    private var fArray: Array<Fragment> = arrayOf(Fragment())

    init {
        this.fArray = fragmentArray
    }

    override fun getItemCount(): Int {
        return fArray.size
    }

    override fun createFragment(position: Int): Fragment {
        return fArray[position]
    }

}