package com.example.heroesandbandits.Items

import android.util.Log
import android.widget.Toast
import com.example.heroesandbandits.Models.Series
import com.example.heroesandbandits.MyApplication
import com.example.heroesandbandits.R
import com.example.heroesandbandits.Utils.StitchCon
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.fragment_search_item.view.*

class SeriesItem(val series: Series) : Item<GroupieViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.fragment_search_item
    }


    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        var path = series.thumbnail.path
        path = path.substring(0, 4) + "s" + path.substring(4, path.length)
        val imageUrl = path + "/standard_medium." + series.thumbnail.extension
        viewHolder.itemView.resultItemText.setText(series.title)
        Picasso.get().load(imageUrl)
            .error(R.drawable.cat)
            .into(viewHolder.itemView.resultItemAvatar)

        val favoriteButton = viewHolder.itemView.favorite_button

        val favouriteChecked = StitchCon.userData?.series!!.find {
            it == series.id
        }

        favoriteButton.isChecked = favouriteChecked != null

        favoriteButton.setOnClickListener {
            if (favoriteButton.isChecked) {
                Log.d("___favorite", "${series.title} ${favoriteButton.isChecked}")
                StitchCon.addSeriesToFavourites(series)?.addOnCompleteListener {
                    if (it.isSuccessful) {
                        StitchCon.userData!!.series.add(series.id)
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
                StitchCon.removeSeriesFromFavourite(series)?.addOnCompleteListener {
                    if (it.isSuccessful) {
                        Log.d("___", "removed from favourites, insertId: ${it.result}")
                        StitchCon.userData!!.characters.remove(series.id)
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