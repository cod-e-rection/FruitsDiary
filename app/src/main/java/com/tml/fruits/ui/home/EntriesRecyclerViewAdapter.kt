package com.tml.fruits.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tml.fruits.R
import com.tml.fruits.model.EntriesModel
import com.tml.fruits.model.FruitsModel
import kotlin.collections.ArrayList


class EntriesRecyclerViewAdapter(var dataSet: ArrayList<EntriesModel>, var fruits: ArrayList<FruitsModel>) : RecyclerView.Adapter<EntriesRecyclerViewAdapter.ViewHolder>() {

    lateinit var onInteractionEntryInterface: OnInteractionEntryInterface

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntriesRecyclerViewAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.fragment_entry_list_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: EntriesRecyclerViewAdapter.ViewHolder, position: Int) {

        if (dataSet.size > 0) {
            val itemModel = dataSet[position]
            val date = itemModel.date

            holder.entryId.text = "ID: " + itemModel.id.toString()

            val vitaminsNr = getVitamins(itemModel = itemModel)
            holder.entriesDate.text = "Date: $date \nFruits Number: ${dataSet[position].fruit?.size ?: 0} •  Nr of Vitamins: $vitaminsNr"

            // view entry details
            holder.entriesLinearLayout.setOnClickListener {
                onInteractionEntryInterface.onClickEntry(entryID = dataSet[position].id ?: -1)
            }
            // delete entry
            holder.deleteImage.setOnClickListener {
                onInteractionEntryInterface.removeEntry(entryID = dataSet[position].id ?: -1)
            }

        }

    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

        var entriesLinearLayout: LinearLayout = itemView.findViewById(R.id.entriesLinearLayout)
        var entryId: TextView = itemView.findViewById(R.id.entryId)
        var entriesDate: TextView = itemView.findViewById(R.id.entriesDate)
        var deleteImage: ImageView = itemView.findViewById(R.id.deleteImage)

        init {
            itemView.tag = this
        }

    }

    /**
     * function used in order to get the vitamins calculation
     */
    private fun getVitamins(itemModel: EntriesModel) : Int {
        var vitamins = 0
        itemModel.fruit?.forEach {
            fruits.forEach { fruit ->
                if (it.fruitId == fruit.id) {
                    vitamins += it.amount * fruit.vitamins
                }
            }

        }

        return vitamins
    }

}

/**
 * OnInteractionEntryInterface used in order to remove entry
 * or the other function to view details
 */

interface OnInteractionEntryInterface {
    fun removeEntry(entryID: Int?)
    fun onClickEntry(entryID: Int?)
}