package com.example.heroesandbandits.Items

import android.util.Log.d
import com.example.heroesandbandits.Models.Character
import com.example.heroesandbandits.Models.FavoriteCharacter
import com.example.heroesandbandits.R
import com.example.heroesandbandits.Utils.StitchCon
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.fragment_search_item.view.*
import org.bson.Document

class FavoriteItem(val character: FavoriteCharacter) : Item<GroupieViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.fragment_search_item
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

        d("___", "${StitchCon.userData?.characters!!.find {it == character.id}}")

        val favoriteButton = viewHolder.itemView.favorite_button
        val favouriteChecked = StitchCon.userData?.characters!!.find {
            val item = it as Document
            item["id"] == character.id
        }

        favoriteButton.isChecked = favouriteChecked != null

        d("___", "Inne i bind FavouriteItem")

        viewHolder.itemView.resultItemText.setText(character.name)
        Picasso.get().load(character.thumbnail)
            .error(R.drawable.cat)
            .into(viewHolder.itemView.resultItemAvatar)
    }
}