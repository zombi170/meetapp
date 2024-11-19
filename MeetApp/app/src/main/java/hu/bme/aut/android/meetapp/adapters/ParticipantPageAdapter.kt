package hu.bme.aut.android.meetapp.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import hu.bme.aut.android.meetapp.fragments.ChartFragment
import hu.bme.aut.android.meetapp.fragments.ParticipantListFragment

class ParticipantPageAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle):
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = NUM_PAGES

    override fun createFragment(position: Int): Fragment = when(position){
        0 -> ParticipantListFragment()
        1 -> ChartFragment()
        else -> ParticipantListFragment()
    }

    companion object{
        const val NUM_PAGES = 2
    }
}