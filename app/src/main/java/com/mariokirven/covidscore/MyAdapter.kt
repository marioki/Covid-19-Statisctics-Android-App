package com.mariokirven.covidscore

import Model.CountryItem
import Model.CountryX
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.getCodeCacheDir
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.country_item_layout.view.*
import java.util.*
import kotlin.collections.ArrayList

class MyAdapter(private val exampleList: List<CountryX>) : RecyclerView.Adapter<MyAdapter.ItemViewHolder>(), Filterable {

    var countryFilterList = exampleList


//Filter Function

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    countryFilterList = exampleList
                } else {
                    val resultList = ArrayList<CountryX>()
                    for (item in exampleList) {
                        if (item.country.toString().toLowerCase().contains(charSearch.toLowerCase(Locale.ROOT))) {
                            val newCountryX = CountryX(item.country, item.countryCode, item.date,
                                    item.newConfirmed, item.newDeaths, item.newRecovered
                                    , item.slug, item.totalConfirmed, item.totalDeaths, item.totalRecovered)
                            resultList.add(item)
                        }
                    }
                    countryFilterList = resultList
                }

                val filterResults = FilterResults()
                filterResults.values = countryFilterList
                return filterResults

            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                countryFilterList = results?.values as ArrayList<CountryX>
                notifyDataSetChanged()
            }

        }
    }//End of Filter Function

    //On Creat View Holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.country_item_layout,
                parent, false)

        itemView.setOnClickListener {
            hearOnClick(itemView)
        }

        return ItemViewHolder(itemView)


    }//End of On Create View Holder

    private fun hearOnClick(itemView: View?) {

    }


    //On bind View Holder
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        val currentItem = countryFilterList[position]

        holder.textView1.text = currentItem.country
        holder.textView2.text = currentItem.countryCode
        holder.textView3.text = currentItem.newConfirmed.toString()
        holder.textView4.text = currentItem.totalConfirmed.toString()
        holder.textView5.text = currentItem.newDeaths.toString()
        holder.textView6.text = currentItem.totalDeaths.toString()
        holder.textView7.text = currentItem.newRecovered.toString()
        holder.textView8.text = currentItem.totalRecovered.toString()
        holder.textView9.text = currentItem.date

        //Detectar el click en el item

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            Toast.makeText(holder.itemView.context, "Selected: ${currentItem.slug}", Toast.LENGTH_LONG).show()
            goToCountryDetails(currentItem.slug, context)


        }


    }// End Of On Bind View Holder

    private fun goToCountryDetails(slug: String, context: Context) {
        val myIntent: Intent = Intent(context, CountryDetails::class.java)
        context.startActivity(myIntent)

    }


    // La clase Recycler View Pide implementar el metodo Get item count
    override fun getItemCount(): Int {
        return countryFilterList.size
    }


    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //val myItemView = itemView

        val textView1: TextView = itemView.text_view_1_info
        val textView2: TextView = itemView.text_view_2_info
        val textView3: TextView = itemView.text_view_3_info
        val textView4: TextView = itemView.text_view_4_info
        val textView5: TextView = itemView.text_view_5_info
        val textView6: TextView = itemView.text_view_6_info
        val textView7: TextView = itemView.text_view_7_info
        val textView8: TextView = itemView.text_view_8_info
        val textView9: TextView = itemView.text_view_9_info


    }


}


