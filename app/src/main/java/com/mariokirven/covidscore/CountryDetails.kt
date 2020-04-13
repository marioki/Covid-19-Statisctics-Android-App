package com.mariokirven.covidscore

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_country_details.*
import java.util.*


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


        //country_flag_imgView
        getCountryFlag(countryInfoIso2.toLowerCase(Locale.ROOT))


        country_name_textView.text = country
        country_code_textView.text = countryInfoIso2

        total_confirmed_textView.text = cases

        total_deaths_textView.text = deaths

        total_recovered_textView.text = recovered


    }

    private fun getCountryFlag(countryCode: String) {
        val imageUrl = "https://raw.githubusercontent.com/NovelCOVID/API/master/assets/flags/$countryCode.png"
        //Loading image using Picasso
        Picasso.get().load(imageUrl).into(country_flag_imgView)

    }

//    private fun getCountryFlag(countryCode:String) {
//        val imageUrl = "https://www.countryflags.io/$countryCode/flat/64.png"
//        //Loading image using Picasso
//        Picasso.get().load(imageUrl).into(country_flag_imgView)
//
//        val retrofit = Retrofit.Builder()
//                .baseUrl("https://restcountries.eu/rest/v2/alpha?codes=$countryCode")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build()
//
//    }


}
