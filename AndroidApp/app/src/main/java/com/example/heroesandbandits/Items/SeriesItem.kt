package com.example.heroesandbandits.Items

import com.example.heroesandbandits.Models.Series
import com.example.heroesandbandits.R
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.fragment_search_character.view.*

class SeriesItem(val series: Series) : Item<GroupieViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.fragment_search_character
    }


    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        var path = series.thumbnail.path
        path = path.substring(0, 4) + "s" + path.substring(4, path.length)
        val imageUrl = path + "/standard_medium." + series.thumbnail.extension
        viewHolder.itemView.resultItemText.setText(series.title)
        Picasso.get().load(imageUrl)
            .error(R.drawable.cat)
            .into(viewHolder.itemView.resultItemAvatar)
    }

}