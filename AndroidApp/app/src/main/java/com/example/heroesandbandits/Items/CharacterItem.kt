package com.example.heroesandbandits.Items

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import com.example.heroesandbandits.Fragments.DetailFragment
import com.example.heroesandbandits.Models.Character
import com.example.heroesandbandits.MyApplication
import com.example.heroesandbandits.R
import com.example.heroesandbandits.Utils.MarvelRetrofit
import com.example.heroesandbandits.Utils.StitchCon
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_search_item.view.*
import org.bson.Document

class CharacterItem(val character: Character) : Item<GroupieViewHolder>() {


    override fun getLayout(): Int {
        return R.layout.fragment_search_item
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.resultItemText.text = character.name
        Picasso.get().load(character.getImageUrl())
            .error(R.drawable.cat)
            .into(viewHolder.itemView.resultItemAvatar)

        val favoriteButton = viewHolder.itemView.favorite_button
        val favouriteChecked = StitchCon.userData?.characters!!.find {
            val item = it as Document
            item["id"] == character.id
        }

        favoriteButton.isChecked = favouriteChecked != null

        favoriteButton.setOnClickListener {
            if (favoriteButton.isChecked) {
                StitchCon.addCharacterToFavourites(character)?.addOnCompleteListener {
                    if (it.isSuccessful) {
                        getFromApi(character.id)
                        Toast.makeText(
                            MyApplication.context,
                            "Successfully added to favourites, insertId: ${it}", Toast.LENGTH_LONG
                        ).show()
                    } else {
                        Log.e("___", "Error adding to favourites", it.exception);

                    }
                }
            } else {
                StitchCon.removeCharacterFromFavourites(character)?.addOnCompleteListener {
                    if (it.isSuccessful) {
                        StitchCon.userData!!.characters.remove(Document()
                            .append("id", character.id)
                            .append("thumbnail", character.getImageUrl())
                            .append("name", character.name))
                        Log.d("___", "removed from favourites, insertId: ${it.result}")
                        Toast.makeText(
                            MyApplication.context,
                            "Successfully removed from favourites, insertId: ${it}",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        Log.e("___", "Error removing from favourites", it.exception);

                    }
                }
            }
        }
    }

    @SuppressLint("CheckResult")
    private fun getFromApi(id:Int) {
        MarvelRetrofit.marvelService.getOneCharacter(id)
            .subscribeOn(Schedulers.newThread())
            .subscribe { result, err ->
                if (err?.message != null) {
                    Log.d("___", "Something went wrong: ${err.message}")
                } else {
                    Log.d("___", "$result")
                    StitchCon.userData!!.characters.add(
                        Document()
                            .append("id", result.data.results[0].id)
                            .append("thumbnail", result.data.results[0].getImageUrl())
                            .append("name", result.data.results[0].name)
                    )
                    Log.d(
                        "___",
                        "adding to favourites to favourites, insertId: ${result.data.results[0]})}"
                    )
                    Log.d("___", "updated list: ${StitchCon.userData?.favourites}")
                }
            }
    }
}