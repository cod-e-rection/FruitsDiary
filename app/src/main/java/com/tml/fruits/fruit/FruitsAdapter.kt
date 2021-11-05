package com.tml.fruits.fruit

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.tml.fruits.R
import com.tml.fruits.api.APIs
import com.tml.fruits.model.FruitsModel
import java.util.ArrayList


class FruitsAdapter(val ctx: Context, var dataSet: ArrayList<FruitsModel>) : RecyclerView.Adapter<FruitsAdapter.ViewHolder>() {


    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.home_fragment_adapter, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {

        viewHolder.fruitId.text = dataSet[i].id.toString()
        viewHolder.fruitType.text = dataSet[i].type ?: ""
        viewHolder.fruitVitamins.text = dataSet[i].vitamins.toString()

        // set fruit imageView
        if (dataSet[i].image != null) {
            Glide.with(ctx)
                .load(APIs.BASIC_URL.value + dataSet[i].image)
                .apply(RequestOptions().placeholder(R.drawable.ic_launcher_background).error(R.drawable.ic_launcher_background))
                .into(viewHolder.fruitImageView)
        }
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

        var fruitImageView: ImageView = itemView.findViewById(R.id.fruitImageView)
        var fruitId: TextView = itemView.findViewById(R.id.fruitId)
        var fruitType: TextView = itemView.findViewById(R.id.fruitType)
        var fruitVitamins: TextView = itemView.findViewById(R.id.fruitVitamins)

        init {
            itemView.tag = this
        }
    }

}
