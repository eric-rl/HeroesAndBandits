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
        return R.layout.fragment_search_character
    }


    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        val char = viewHolder.item as CharacterItem
        var path = character.thumbnail.path
        path = path.substring(0, 4) + "s" + path.substring(4, path.length)
        val imageUrl = path + "/standard_medium." + character.thumbnail.extension
        viewHolder.itemView.resultItemText.setText(character.name)
        Picasso.get().load(imageUrl)
            .error(R.drawable.cat)
            .into(viewHolder.itemView.resultItemAvatar)

//        viewHolder.itemView.favorite_button.isChecked = true

        val favoriteButton = viewHolder.itemView.favorite_button

        favoriteButton.setOnClickListener {
            if(favoriteButton.isChecked){
                Log.d("favorite", "${favoriteButton.isChecked}")
            } else {
                Log.d("favorite", "${favoriteButton.isChecked}")
            }
//            val img =
//            viewHolder.itemView.favorite_button.setImageResource()
        }
    }

}