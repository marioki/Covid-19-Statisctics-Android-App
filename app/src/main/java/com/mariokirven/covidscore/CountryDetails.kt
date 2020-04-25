package com.mariokirven.covidscore

import Interfaz.CovApi
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
import com.anychart.core.cartesian.series.Line
import com.anychart.data.Mapping
import com.anychart.data.Set
import com.anychart.enums.*
import com.anychart.graphics.vector.Stroke
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_country_details.*
import model.CountryDeathsHistoryItem
import model.CountryHistoryItem
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
        getCountryCasesHistData(countryInfoIso2, any_chart_view_column)
        getCountryDeathsHistData(countryInfoIso2, any_chart_view_column)

        //country_flag_imgView
        getCountryFlag(countryInfoIso2.toLowerCase(Locale.ROOT))

        //Set infromation into the text views
        setCountryStats(country, countryInfoIso2, cases, critical, deaths, recovered)

        //Generate pie Chart
        val anyChartView = any_chart_view_pie
        setCharts(any_chart_view_pie, active, critical, deaths, recovered, cases)


    }


    private fun getCountryDeathsHistData(countryCode: String, lineDeathsAnyChartView: AnyChartView) {
        val retrofit = Retrofit.Builder()
                .baseUrl("https://api.covid19api.com/total/dayone/country/$countryCode/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        val myCovApi = retrofit.create(CovApi::class.java)
        val call = myCovApi.countryDeathsHistoricalAll

        call.enqueue(object : Callback<java.util.ArrayList<CountryDeathsHistoryItem>?> {
            @RequiresApi(api = Build.VERSION_CODES.N)
            override fun onResponse(call: Call<java.util.ArrayList<CountryDeathsHistoryItem>?>, response:
            Response<java.util.ArrayList<CountryDeathsHistoryItem>?>) {
                val dateList = ArrayList<String>()
                val numberOfCases = ArrayList<Int>()
//                val numberOfDeaths = ArrayList<Int>()
//                val numberOfRecovered = ArrayList<Int>()
//                val numberOfActive = ArrayList<Int>()

                val myCountryDeathHistoryArrayList = response.body()

                myCountryDeathHistoryArrayList?.forEach { countryHistoryItem: CountryDeathsHistoryItem ->
                    dateList.add(countryHistoryItem.date)
                    numberOfCases.add(countryHistoryItem.cases)
//                    numberOfDeaths.add(countryHistoryItem.deaths)
//                    numberOfRecovered.add(countryHistoryItem.recovered)
//                    numberOfActive.add(countryHistoryItem.active)
                }

                if (myCountryDeathHistoryArrayList != null) {
                    setDeathsLineChart(lineDeathsAnyChartView, myCountryDeathHistoryArrayList)
                }

                Log.e("myCode", "WE are Inside History OnResponse  " + response.code())
                //Log.e("My Array",  dateList[0] + dateList[1])

            }

            override fun onFailure(call: Call<java.util.ArrayList<CountryDeathsHistoryItem>?>, t: Throwable) {
                Log.e("myCode", "WE are Inside History Onfailure " + t.message)
            }
        })


    }

    private fun setDeathsLineChart(lineDeathsAnyChartView: AnyChartView
                                   , myCountryDeathHistoryArrayList: java.util.ArrayList<CountryDeathsHistoryItem>) {

        APIlib.getInstance().setActiveAnyChartView(any_chart_view_deaths_line);

        val anyChartView = findViewById<AnyChartView>(R.id.any_chart_view_deaths_line)
        anyChartView.setProgressBar(findViewById(R.id.deathLineIndeterminateBar))

        val cartesian = AnyChart.line()

        cartesian.animation(true)

        cartesian.padding(10.0, 20.0, 5.0, 20.0)

        cartesian.crosshair().enabled(true)
        cartesian.crosshair()
                .yLabel(true) // TODO ystroke
                .yStroke(null as Stroke?, null, null, null as String?, null as String?)

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT)

        cartesian.title("Total Deaths")

        cartesian.yAxis(0).title("Number of Deaths")
        cartesian.xAxis(0).labels().padding(5.0, 5.0, 5.0, 5.0)

        val seriesData: MutableList<DataEntry> = ArrayList()


        myCountryDeathHistoryArrayList?.forEach { countryHistoryDeathsItem: CountryDeathsHistoryItem ->
            seriesData.add(ValueDataEntry(countryHistoryDeathsItem.date, countryHistoryDeathsItem.cases))
        }

        //seriesData.add(CustomDataEntry("1986", 3.6, 2.3, 2.8))


        val set = Set.instantiate()
        set.data(seriesData)
        val series1Mapping: Mapping = set.mapAs("{ x: 'x', value: 'value' }")
//        val series2Mapping: Mapping = set.mapAs("{ x: 'x', value: 'value2' }")
//        val series3Mapping: Mapping = set.mapAs("{ x: 'x', value: 'value3' }")


        val series1: Line = cartesian.line(series1Mapping)
        series1.name("Brandy")
        series1.hovered().markers().enabled(true)
        series1.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4.0)
        series1.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5.0)
                .offsetY(5.0)

//        val series2: Line = cartesian.line(series2Mapping)
//        series2.name("Whiskey")
//        series2.hovered().markers().enabled(true)
//        series2.hovered().markers()
//                .type(MarkerType.CIRCLE)
//                .size(4.0)
//        series2.tooltip()
//                .position("right")
//                .anchor(Anchor.LEFT_CENTER)
//                .offsetX(5.0)
//                .offsetY(5.0)
//
//        val series3: Line = cartesian.line(series3Mapping)
//        series3.name("Tequila")
//        series3.hovered().markers().enabled(true)
//        series3.hovered().markers()
//                .type(MarkerType.CIRCLE)
//                .size(4.0)
//        series3.tooltip()
//                .position("right")
//                .anchor(Anchor.LEFT_CENTER)
//                .offsetX(5.0)
//                .offsetY(5.0)
//
//        cartesian.legend().enabled(true)
//        cartesian.legend().fontSize(13.0)
//        cartesian.legend().padding(0.0, 0.0, 10.0, 0.0)
//
        anyChartView.setChart(cartesian)


    }


    private class CustomDataEntry internal constructor(x: String?, value: Number?, value2: Number?, value3: Number?) : ValueDataEntry(x, value) {
        init {
            setValue("value2", value2)
            setValue("value3", value3)
        }
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


    private fun getCountryCasesHistData(countryCode: String, columnAnyChartView: AnyChartView) {
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
//                val numberOfDeaths = ArrayList<Int>()
//                val numberOfRecovered = ArrayList<Int>()
//                val numberOfActive = ArrayList<Int>()

                val myCountryHistoryArrayList = response.body()

                myCountryHistoryArrayList?.forEach { countryHistoryItem: CountryHistoryItem ->
                    dateList.add(countryHistoryItem.date)
                    numberOfCases.add(countryHistoryItem.cases)
//                    numberOfDeaths.add(countryHistoryItem.deaths)
//                    numberOfRecovered.add(countryHistoryItem.recovered)
//                    numberOfActive.add(countryHistoryItem.active)
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
        pie.labels().fontColor("Black")

        val yellow = "#D4AC0D"
        val red = "#BA4A00"
        val green = "#28B463"
        val black = "#273746"

        pie.palette(arrayOf(yellow, red, black, green))

        pie.palette().items()

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

        columnAnyChartView.setProgressBar(findViewById(R.id.collumnIndeterminateBar));

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
        val imageUrl = "https://corona.lmao.ninja/assets/img/flags/$countryCode.png"
        //Loading image using Picasso
        Picasso.get().load(imageUrl).into(country_flag_imgView)

    }


}
