package com.example.heroesandbandits.Items

import android.util.Log
import android.util.Log.d
import com.example.heroesandbandits.Models.FavoriteCharacter
import com.example.heroesandbandits.R
import com.example.heroesandbandits.Utils.StitchCon
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.fragment_search_item.view.*
import org.bson.Document

class FavoriteCharacterItem(val character: FavoriteCharacter) : Item<GroupieViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.fragment_search_item
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

        d("___", "${StitchCon.userData?.characters!!.find { it == character.id }}")

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

        favoriteButton.setOnClickListener {
            if (favoriteButton.isChecked) {
                StitchCon.addCharacterToFavourites(character)?.addOnCompleteListener {
                    if (it.isSuccessful) {
                        StitchCon.userData!!.characters.add(
                            Document()
                                .append("id", character.id)
                                .append("thumbnail", character.thumbnail)
                                .append("name", character.name)
                        )
                    } else {
                        Log.e("___", "Error adding to favourites", it.exception);
                    }
                }
            } else {
                StitchCon.removeCharacterFromFavourites(character)?.addOnCompleteListener {
                    if (it.isSuccessful) {
                        StitchCon.userData!!.characters.remove(
                            Document()
                                .append("id", character.id)
                                .append("thumbnail", character.thumbnail)
                                .append("name", character.name)
                        )
                        StitchCon.userData!!.characters.remove(character.id)

                    } else {
                        Log.e("___", "Error removing from favourites", it.exception);
                    }
                }
            }
        }
    }


}