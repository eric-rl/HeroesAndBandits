package com.example.heroesandbandits.Items

import android.util.Log
import com.example.heroesandbandits.Models.Hero
import com.example.heroesandbandits.R
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.fragment_hero.view.*

class HeroItem(private val hero: Hero): Item<GroupieViewHolder>() {
    override fun getLayout(): Int {
        Log.d("__", "hej från heroItemClass")
        return R.layout.fragment_hero
    }


    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        Log.d("__", "hej från heroItemMetod")
        viewHolder.itemView.heroName.text = hero.name
    }

}