package com.mariokirven.covidscore
import Model.CountryItem
import Model.CountryX
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.country_item_layout.view.*

class MyAdapter (private val exampleList: List<CountryX>) : RecyclerView.Adapter<MyAdapter.ItemViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.country_item_layout,
                parent, false)

        return ItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val currentItem = exampleList[position]

//        holder.textView1.append(currentItem.country)
//        holder.textView2.append (currentItem.countryCode)
//        holder.textView3.append (currentItem.newConfirmed.toString())
//        holder.textView4.append (currentItem.totalConfirmed.toString())
//        holder.textView5.append (currentItem.newDeaths.toString())
//        holder.textView6.append (currentItem.totalDeaths.toString())
//        holder.textView7.append (currentItem.newRecovered.toString())
//        holder.textView8.append (currentItem.totalRecovered.toString())
//        holder.textView9.append (currentItem.date)

        holder.textView1.text = currentItem.country
        holder.textView2.text = currentItem.countryCode
        holder.textView3.text = currentItem.newConfirmed.toString()
        holder.textView4.text = currentItem.totalConfirmed.toString()
        holder.textView5.text = currentItem.newDeaths.toString()
        holder.textView6.text = currentItem.totalDeaths.toString()
        holder.textView7.text = currentItem.newRecovered.toString()
        holder.textView8.text = currentItem.totalRecovered.toString()
        holder.textView9.text = currentItem.date





//        if (position == 0){
//            holder.textView1.setBackgroundColor(Color.YELLOW)
//        }
    }

    override fun getItemCount() = exampleList.size

    class ItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val textView1: TextView = itemView.text_view_1_info
        val textView2: TextView = itemView.text_view_2_info
        val textView3: TextView = itemView.text_view_3_info
        val textView4: TextView = itemView.text_view_4_info
        val textView5: TextView = itemView.text_view_5_info
        val textView6: TextView = itemView.text_view_6_info
        val textView7: TextView = itemView.text_view_7_info
        val textView8: TextView = itemView.text_view_8_info
        val textView9: TextView = itemView.text_view_9_info
        //val textView10: TextView = itemView.text_view_10_info
    }


}


