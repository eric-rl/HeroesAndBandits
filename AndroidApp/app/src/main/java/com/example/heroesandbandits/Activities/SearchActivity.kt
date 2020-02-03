package com.example.heroesandbandits.Activities

import android.os.Bundle
import android.util.Log.d
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.heroesandbandits.Fragments.FavoritesFragment
import com.example.heroesandbandits.Fragments.SearchFragment
import com.example.heroesandbandits.R
import com.google.android.material.bottomnavigation.BottomNavigationView


class SearchActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        openFragment(SearchFragment.newInstance())
        val bottomNavigation: BottomNavigationView = findViewById(R.id.navigationView)
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

    }

    private fun openFragment(nextFragment: Fragment) {
        var fromFrag = supportFragmentManager.findFragmentById(R.id.container)
        d("frag", "current frag $fromFrag")
        d("frag", "switching to $nextFragment")
        val transaction = supportFragmentManager.beginTransaction()


//        if (fromFrag is SearchFragment && nextFragment is MessageFragment || nextFragment is FavoritesFragment) {
//            transaction.setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left)
//        } else if(fromFrag is MessageFragment && nextFragment is SearchFragment){
//            transaction.setCustomAnimations(R.anim.enter_left_to_right, R.anim.exit_left_to_right)
//        } else if(fromFrag is FavoritesFragment && nextFragment is MessageFragment || nextFragment is SearchFragment){
//            transaction.setCustomAnimations(R.anim.enter_left_to_right, R.anim.exit_left_to_right)
//        }




        transaction.replace(R.id.container, nextFragment)
        transaction.addToBackStack(null)
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        transaction.commit()
    }

    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_search -> {
                    val searchFragment =
                        SearchFragment.newInstance()
                    openFragment(searchFragment)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_favorites -> {
                    val favFragment =
                        FavoritesFragment.newInstance()
                       openFragment(favFragment)
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }


}