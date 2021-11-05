package com.tml.fruits.ui.entry

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tml.fruits.R
import com.tml.fruits.model.FruitModel
import java.util.ArrayList

/**
 * Created By Elio Lako
 */

// Adapter for the view when we have entry data
class EnteriesEnteredAdapter (var fruitData: ArrayList<FruitModel>) : RecyclerView.Adapter<EnteriesEnteredAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EnteriesEnteredAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.fragment_entered_entry_item_list, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: EnteriesEnteredAdapter.ViewHolder, position: Int) {

        holder.fruitNumber.text = fruitData[position].fruitType ?: ""
        holder.amountNumber.text = fruitData[position].amount.toString()
    }

    override fun getItemCount(): Int {
        return fruitData.size
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

        var fruitNumber: TextView = itemView.findViewById(R.id.fruitNumber)
        var amountNumber: TextView = itemView.findViewById(R.id.amountNumber)

        init {
            itemView.tag = this
        }

    }


}