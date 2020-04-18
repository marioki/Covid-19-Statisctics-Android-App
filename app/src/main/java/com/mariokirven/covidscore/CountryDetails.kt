package com.mariokirven.covidscore

import Interfaz.CovApi
import Model.CountryHistoryItem
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.anychart.APIlib
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.charts.Cartesian
import com.anychart.core.cartesian.series.Column
import com.anychart.enums.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_country_details.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class CountryDetails : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_country_details)


        //Get Country Data From Main Activity
        val active: String = intent.getStringExtra("active")
        val cases: String = intent.getStringExtra("cases")
        val casesPerMillion: String = intent.getStringExtra("casesPerMillion")
        val country: String = intent.getStringExtra("country")
        val countryInfoFlag: String = intent.getStringExtra("countryInfoFlag")
        val countryInfoId: String = intent.getStringExtra("countryInfoId")
        val countryInfoIso2: String = intent.getStringExtra("countryInfoIso2")
        val countryInfoIso3: String = intent.getStringExtra("countryInfoIso3")
        val countryInfoLat: String = intent.getStringExtra("countryInfoLat")
        val countryInfoLong: String = intent.getStringExtra("countryInfoLong")
        val critical: String = intent.getStringExtra("critical")
        val deaths: String = intent.getStringExtra("deaths")
        val deathsPerOneMillion: String = intent.getStringExtra("deathsPerOneMillion")
        val recovered: String = intent.getStringExtra("recovered")
        val tests: String = intent.getStringExtra("tests")
        val testsPerOneMillion: String = intent.getStringExtra("testsPerOneMillion")
        val todayCases: String = intent.getStringExtra("todayCases")
        val todayDeaths: String = intent.getStringExtra("todayDeaths")
        val updated: String = intent.getStringExtra("updated")

        //Get Country Historical Data
        getCountryHistData(countryInfoIso2, any_chart_view_column)


        //country_flag_imgView
        getCountryFlag(countryInfoIso2.toLowerCase(Locale.ROOT))

        //Set infromation into the text views
        setCountryStats(country, countryInfoIso2, cases, critical, deaths, recovered)

        //Generate pie Chart
        val anyChartView = any_chart_view_pie
        setCharts(any_chart_view_pie, active, critical, deaths, recovered, cases)


    }

    //Return to parent Activity without refreshing it
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id: Int = item.getItemId()
        when (id) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


    private fun getCountryHistData(countryCode: String, columnAnyChartView: AnyChartView) {
        val retrofit = Retrofit.Builder()
                .baseUrl("https://api.covid19api.com/total/dayone/country/$countryCode/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        val myCovApi = retrofit.create(CovApi::class.java)
        val call = myCovApi.countryHistoricalAll

        call.enqueue(object : Callback<java.util.ArrayList<CountryHistoryItem>?> {
            @RequiresApi(api = Build.VERSION_CODES.N)
            override fun onResponse(call: Call<java.util.ArrayList<CountryHistoryItem>?>, response:
            Response<java.util.ArrayList<CountryHistoryItem>?>) {
                val dateList = ArrayList<String>()
                val numberOfCases = ArrayList<Int>()

                val myCountryHistoryArrayList = response.body()

                myCountryHistoryArrayList?.forEach { countryHistoryItem: CountryHistoryItem ->
                    dateList.add(countryHistoryItem.date)
                    numberOfCases.add(countryHistoryItem.cases)
                }

                if (myCountryHistoryArrayList != null) {
                    setColumnChart(columnAnyChartView, myCountryHistoryArrayList)
                }

                Log.e("myCode", "WE are Inside History OnResponse  " + response.code())
                //Log.e("My Array",  dateList[0] + dateList[1])

            }

            override fun onFailure(call: Call<java.util.ArrayList<CountryHistoryItem>?>, t: Throwable) {
                Log.e("myCode", "WE are Inside History Onfailure " + t.message)
            }
        })

    }


    private fun setCharts(anyChartView: AnyChartView, active: String, critical: String, deaths: String, recovered: String, cases: String) {
        anyChartView.setProgressBar(findViewById(R.id.pieIndeterminateBar));
        //Necesario para manipular charts cuando existe mas de una anyChartView en el layout
        APIlib.getInstance().setActiveAnyChartView(anyChartView);
        //Formato de numero del titulo del piechart
        val formattedCases = NumberFormat.getNumberInstance(Locale.getDefault()).format(cases.toDouble())

        //Guardando los valores de string values en variables para mejorar la legibilidad
        val activeLabel = getString(R.string.active_label)
        val criticalLabel = getString(R.string.critical_label)
        val deathsLabel = getString(R.string.deaths_labes)
        val recoveredLabel = getString(R.string.recovered_labels)


        val pie = AnyChart.pie()

        val data: MutableList<DataEntry> = ArrayList()
        data.add(ValueDataEntry(activeLabel, active.toInt()))
        data.add(ValueDataEntry(criticalLabel, critical.toInt()))
        data.add(ValueDataEntry(deathsLabel, deaths.toInt()))
        data.add(ValueDataEntry(recoveredLabel, recovered.toInt()))

        pie.data(data)

        pie.title(getString(R.string.total_cases_tittle_mixed, formattedCases))


        pie.labels().position("outside")

//        pie.legend().title().enabled(true)
//        pie.legend().title()
//                .text("Retail channels")
//                .padding(0.0, 0.0, 10.0, 0.0)

        pie.legend()
                .position("center-bottom")
                .itemsLayout(LegendLayout.HORIZONTAL)
                .align(Align.CENTER).padding(0, 0, 8, 0)


        anyChartView.setChart(pie)


    }


    private fun setColumnChart(columnAnyChartView: AnyChartView, myCountryHistoryArrayList: ArrayList<CountryHistoryItem>) {
        APIlib.getInstance().setActiveAnyChartView(columnAnyChartView);

        columnAnyChartView.setProgressBar(findViewById(R.id.indeterminateBar));

        val cartesian: Cartesian = AnyChart.column()

        val data: MutableList<DataEntry> = ArrayList()

        myCountryHistoryArrayList?.forEach { countryHistoryItem: CountryHistoryItem ->
            data.add(ValueDataEntry(countryHistoryItem.date, countryHistoryItem.cases))
        }


        val column: Column = cartesian.column(data)

        column.tooltip()
                .titleFormat("{%X}")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(0.0)
                .offsetY(5.0)
                .format("{%Value}{groupsSeparator: }")

        cartesian.animation(true)
        cartesian.title(getString(R.string.confirmed_cases_curve))

        cartesian.yScale().minimum(0.0)

        cartesian.yAxis(0).labels().format("{%Value}{groupsSeparator: }")

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT)
        cartesian.interactivity().hoverMode(HoverMode.BY_X)

        cartesian.xAxis(0).title(getString(R.string.time_label))
        cartesian.yAxis(0).title(getString(R.string.number_of_cases_label))

        cartesian.xAxis(0).labels().rotation()

        columnAnyChartView.setChart(cartesian)
    }

    private fun setCountryStats(country: String, countryInfoIso2: String, cases: String, critical: String, deaths: String, recovered: String) {
        country_name_textView.text = country
        country_code_textView.text = countryInfoIso2


        val formattedCases = NumberFormat.getNumberInstance(Locale.getDefault()).format(cases.toDouble())
        val formattedCritical = NumberFormat.getNumberInstance(Locale.getDefault()).format(critical.toDouble())
        val formattedDeaths = NumberFormat.getNumberInstance(Locale.getDefault()).format(deaths.toDouble())
        val formattedRecovered = NumberFormat.getNumberInstance(Locale.getDefault()).format(recovered.toDouble())



        total_confirmed_textView.text = getString(R.string.total_cases, formattedCases)

        critical_textView.text = getString(R.string.critical_info, formattedCritical)


        total_deaths_list_textView.text = getString(R.string.total_deaths_info, formattedDeaths)

        total_recovered_textView.text = getString(R.string.total_recovered_info, formattedRecovered)
    }

    private fun getCountryFlag(countryCode: String) {
        val imageUrl = "https://raw.githubusercontent.com/NovelCOVID/API/master/assets/flags/$countryCode.png"
        //Loading image using Picasso
        Picasso.get().load(imageUrl).into(country_flag_imgView)

    }


}
