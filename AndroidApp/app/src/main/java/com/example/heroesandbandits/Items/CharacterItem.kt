package com.example.heroesandbandits.Items

import android.util.Log
import com.example.heroesandbandits.Models.Character
import com.example.heroesandbandits.R
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.fragment_search_character.view.*

class CharacterItem(val character: Character) : Item<GroupieViewHolder>() {
    override fun getLayout(): Int {
        Log.d("__", "hej från heroItemClass")
        return R.layout.fragment_search_character
    }


    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        Log.d("__", "hej från heroItemMetod")
        viewHolder.itemView.resultItemText.setText(character.name)

        Picasso.get().load("https://news.artnet.com/app/news-upload/2019/01/Cat-Photog-Feat-256x256.jpg").placeholder(R.drawable.cat)
            .error(R.drawable.cat)
            .into(viewHolder.itemView.resultItemAvatar)
    }

}