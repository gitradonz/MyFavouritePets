package edu.pablorios.myfavouritepetsv2

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class ViewPagerAdapter(FragmentManager: FragmentManager) :
    FragmentStatePagerAdapter(
        FragmentManager,
        BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
    ) {

    private val mFragmentList = ArrayList<Fragment>()
    private val mFragmentTitleList = ArrayList<String>()

    // Obtenemos la posicion del fragmento
    override fun getItem(position: Int): Fragment {
        return mFragmentList[position]
    }

    // Obtenemos el total de fragmentos
    override fun getCount(): Int {
        return mFragmentList.size
    }

    // Obtenemos el titulo de un fragmento concreto
    override fun getPageTitle(position: Int): CharSequence {
        return mFragmentTitleList[position]
    }

    // Añadimos el fragmento a nuestra lista con su título
    fun addFragment(fragment: Fragment, title: String) {
        mFragmentList.add(fragment)
        mFragmentTitleList.add(title)
    }
}