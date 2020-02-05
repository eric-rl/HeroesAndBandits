package com.example.heroesandbandits.Items

import android.util.Log
import android.util.Log.d
import android.widget.Toast
import com.example.heroesandbandits.Models.Character
import com.example.heroesandbandits.Models.FavoriteCharacter
import com.example.heroesandbandits.Models.FavoriteSeries
import com.example.heroesandbandits.MyApplication
import com.example.heroesandbandits.R
import com.example.heroesandbandits.Utils.StitchCon
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.fragment_search_item.view.*
import org.bson.Document

class FavoriteSeriesItem(val series: FavoriteSeries) : Item<GroupieViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.fragment_search_item
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        val favoriteButton = viewHolder.itemView.favorite_button
        val favouriteChecked = StitchCon.userData?.series!!.find {
            val item = it as Document
            item["id"] == series.id
        }

        favoriteButton.isChecked = favouriteChecked != null

        viewHolder.itemView.resultItemText.setText(series.title)
        Picasso.get().load(series.thumbnail)
            .error(R.drawable.cat)
            .into(viewHolder.itemView.resultItemAvatar)

        favoriteButton.setOnClickListener {
            if (favoriteButton.isChecked) {
                StitchCon.addSeriesToFavourites(series)?.addOnCompleteListener {
                    if (it.isSuccessful) {
                        StitchCon.userData!!.series.add(Document()
                            .append("id", series.id)
                            .append("thumbnail", series.thumbnail)
                            .append("title", series.title))
                    } else {
                        Log.e("___", "Error adding to favourites", it.exception);
                    }
                }
            } else {
                StitchCon.removeSeriesFromFavourite(series)?.addOnCompleteListener {
                    if (it.isSuccessful) {
                        StitchCon.userData!!.series.remove(Document()
                            .append("id", series.id)
                            .append("thumbnail", series.thumbnail)
                            .append("title", series.title))
                        StitchCon.userData!!.characters.remove(series.id)
                        
                    } else {
                        Log.e("___", "Error removing from favourites", it.exception);
                    }
                }
            }
        }
    }
}