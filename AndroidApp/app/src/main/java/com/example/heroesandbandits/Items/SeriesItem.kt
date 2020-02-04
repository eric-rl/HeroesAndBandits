package com.example.heroesandbandits.Items

import android.util.Log
import android.widget.Toast
import com.example.heroesandbandits.Models.Series
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

class SeriesItem(val series: Series) : Item<GroupieViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.fragment_search_item
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.resultItemText.text = series.title
        Picasso.get().load(series.getImageUrl())
            .error(R.drawable.cat)
            .into(viewHolder.itemView.resultItemAvatar)

        val favoriteButton = viewHolder.itemView.favorite_button
        val favouriteChecked = StitchCon.userData?.series!!.find {
            val item = it as Document
            item["id"] == series.id
        }

        favoriteButton.isChecked = favouriteChecked != null

        favoriteButton.setOnClickListener {
            if (favoriteButton.isChecked) {
                Log.d("___favorite", "${series.title} ${favoriteButton.isChecked}")
                StitchCon.addSeriesToFavourites(series)?.addOnCompleteListener {
                    if (it.isSuccessful) {
                        getFromApi(series.id)
                    } else {
                        Log.e("___", "Error adding to favourites", it.exception);
                    }
                }
            } else {
                StitchCon.removeSeriesFromFavourite(series)?.addOnCompleteListener {
                    if (it.isSuccessful) {
                        StitchCon.userData!!.series.remove(Document()
                            .append("id", series.id)
                            .append("thumbnail", series.getImageUrl())
                            .append("title", series.title))
                        Log.d("___", "removed from favourites, insertId: ${it.result}")
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
    private fun getFromApi(id:Int) {
        MarvelRetrofit.marvelService.getOneSeries(id)
            .subscribeOn(Schedulers.newThread())
            .subscribe { result, err ->
                if (err?.message != null) {
                    Log.d("___", "Something went wrong: ${err.message}")
                } else {
                    Log.d("___", "$result")
                    StitchCon.userData!!.series.add(
                        Document()
                            .append("id", result.data.results[0].id)
                            .append("thumbnail", result.data.results[0].getImageUrl())
                            .append("title", result.data.results[0].title)
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