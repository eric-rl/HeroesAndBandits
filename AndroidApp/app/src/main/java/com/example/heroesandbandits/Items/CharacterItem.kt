package com.example.heroesandbandits.Items

import android.util.Log
import android.util.Log.d
import android.widget.CheckBox
import android.widget.Toast
import com.example.heroesandbandits.Models.Character
import com.example.heroesandbandits.MyApplication
import com.example.heroesandbandits.R
import com.example.heroesandbandits.Utils.StitchCon
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.fragment_search_character.view.*

class CharacterItem(val character: Character) : Item<GroupieViewHolder>() {


    override fun getLayout(): Int {
        return R.layout.fragment_search_character
    }


    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        val favoriteButton = viewHolder.itemView.favorite_button
//        val favouriteChecked = StitchCon.user?.characters!!.find {
//            it["char_id"] == character.id}
//        if(favouriteChecked != null){
//            favoriteButton.isChecked = true
//        }
        val favouriteChecked = StitchCon.userData?.characters!!.find {
            it == character.id
        }
        if (favouriteChecked != null) {
            favoriteButton.isChecked = true
        } else {
            favoriteButton.isChecked = false
        }

        var path = character.thumbnail.path
        path = path.substring(0, 4) + "s" + path.substring(4, path.length)
        val imageUrl = path + "/standard_medium." + character.thumbnail.extension

        viewHolder.itemView.resultItemText.setText(character.name)
        Picasso.get().load(imageUrl)
            .error(R.drawable.cat)
            .into(viewHolder.itemView.resultItemAvatar)

        favoriteButton.setOnClickListener {
            if (favoriteButton.isChecked) {
                Log.d("___favorite", "${character.name} ${favoriteButton.isChecked}")
                StitchCon.addToFavourites(character)?.addOnCompleteListener {
                    if (it.isSuccessful) {
                        Log.d("___", "adding to favourites to favourites, insertId: $it)}");
                        Toast.makeText(
                            MyApplication.context,
                            "Successfully added to favourites, insertId: ${it}", Toast.LENGTH_LONG
                        ).show()
                    } else {
                        Log.e("___", "Error adding to favourites", it.exception);

                    }
                }
            } else {
                StitchCon.removeFromFavourites(character)?.addOnCompleteListener {
                    if (it.isSuccessful) {
                        Log.d("___", "removed from favourites, insertId: ${it.result}");
                        Toast.makeText(
                            MyApplication.context,
                            "Successfully removed from favourites, insertId: ${it}",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        Log.e("___", "Error removing from favourites", it.exception);

                    }
                    Log.d("___favorite", "${favoriteButton.isChecked}")
                }
            }
        }
    }

}