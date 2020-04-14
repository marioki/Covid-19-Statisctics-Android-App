package com.mariokirven.covidscore

import Model.CountryItem
import android.content.Context
import android.content.Intent

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.country_item_layout.view.*
import java.text.SimpleDateFormat

import java.util.*
import kotlin.collections.ArrayList


class MyAdapter(private val exampleList: List<CountryItem>) : RecyclerView.Adapter<MyAdapter.ItemViewHolder>(), Filterable {

    var countryFilterList = exampleList


//Filter Function

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    countryFilterList = exampleList
                } else {
                    val resultList = ArrayList<CountryItem>()
                    for (item in exampleList) {
                        if (item.country.toString().toLowerCase().contains(charSearch.toLowerCase(Locale.ROOT))) {
                            val newCountryItem = CountryItem(item.active, item.cases
                                    , item.casesPerOneMillion, item.country, item.countryInfo
                                    , item.critical, item.deaths, item.deathsPerOneMillion
                                    , item.recovered, item.tests, item.testsPerOneMillion
                                    , item.todayCases, item.todayDeaths, item.updated)
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
                countryFilterList = results?.values as ArrayList<CountryItem>
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

        holder.countryName.text = currentItem.country
        holder.cases.text = currentItem.cases.toString()
        holder.deaths.text = currentItem.deaths.toString()

        holder.date.text = getActualDate(currentItem.updated)

        getCountryFlag(currentItem.countryInfo.iso2.toString(), holder.countryFlag)





        //Detectar el click en el item

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context

            goToCountryDetails(context
                    , currentItem.active.toString()
                    , currentItem.cases.toString()
                    , currentItem.casesPerOneMillion.toString()
                    , currentItem.country
                    , currentItem.countryInfo.flag
                    , currentItem.countryInfo.id.toString()
                    , currentItem.countryInfo.iso2.toString()
                    , currentItem.countryInfo.iso3.toString()
                    , currentItem.countryInfo.lat.toString()
                    , currentItem.countryInfo.long.toString()
                    , currentItem.critical.toString()
                    , currentItem.deaths.toString()
                    , currentItem.deathsPerOneMillion.toString()
                    , currentItem.recovered.toString()
                    , currentItem.tests.toString()
                    , currentItem.testsPerOneMillion.toString()
                    , currentItem.todayCases.toString()
                    , currentItem.todayDeaths.toString()
                    , currentItem.updated.toString())


        }


    }// End Of On Bind View Holder

    private fun getActualDate(miliTime:Long): String {
        val itemLong = (miliTime / 1000) as Long

        val d = Date(itemLong * 1000L)
        val itemDateStr: String = SimpleDateFormat("dd-MMM HH:mm").format(d)
        return itemDateStr
    }


    private fun getCountryFlag(countryCode: String, flagView: ImageView) {

        val imageUrl = "https://www.countryflags.io/$countryCode/flat/64.png"
        //Loading image using Picasso
        Picasso.get().load(imageUrl).into(flagView)
    }


    private fun goToCountryDetails(context: Context
                                   , active: String
                                   , cases: String
                                   , casesPerOneMillion: String
                                   , country: String
                                   , countryInfoFlag: String
                                   , countryInfoId: String
                                   , countryInfoIso2: String
                                   , countryInfoIso3: String
                                   , countryInfoLat: String
                                   , countryInfoLong: String
                                   , critical: String
                                   , deaths: String
                                   , deathsPerOneMillion: String
                                   , recovered: String
                                   , tests: String
                                   , testsPerOneMillion: String
                                   , todayCases: String
                                   , todayDeaths: String
                                   , updated: String) {
        val myIntent: Intent = Intent(context, CountryDetails::class.java).apply {
            putExtra("active", active)
            putExtra("cases", cases)
            putExtra("casesPerMillion", casesPerOneMillion)
            putExtra("country", country)
            putExtra("countryInfoFlag", countryInfoFlag)
            putExtra("countryInfoId", countryInfoId)
            putExtra("countryInfoIso2", countryInfoIso2)
            putExtra("countryInfoIso3", countryInfoIso3)
            putExtra("countryInfoLat", countryInfoLat)
            putExtra("countryInfoLong", countryInfoLong)
            putExtra("critical", critical)
            putExtra("deaths", deaths)
            putExtra("deathsPerOneMillion", deathsPerOneMillion)
            putExtra("recovered", recovered)
            putExtra("tests", tests)
            putExtra("testsPerOneMillion", testsPerOneMillion)
            putExtra("todayCases", todayCases)
            putExtra("todayDeaths", todayDeaths)
            putExtra("updated", updated)

        }
        context.startActivity(myIntent)

    }


    // La clase Recycler View Pide implementar el metodo Get item count
    override fun getItemCount(): Int {
        return countryFilterList.size
    }


    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val countryName: TextView = itemView.country_name_textView
        val cases: TextView = itemView.cases_textView
        val deaths: TextView = itemView.deaths_list_info
        val date: TextView = itemView.date_info_textView
        val countryFlag: ImageView = itemView.country_flag_view



    }


}


