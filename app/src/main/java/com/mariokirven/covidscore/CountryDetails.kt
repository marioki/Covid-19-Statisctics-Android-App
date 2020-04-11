package com.mariokirven.covidscore

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_country_details.*

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class CountryDetails : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_country_details)


        //Get Country Data From Main Activity
        val countryName:String = intent.getStringExtra("CountryName")
        val countrySlug:String = intent.getStringExtra("CountrySlug")
        val countryCode:String = intent.getStringExtra("CountryCode")
        val newConfirmed:String = intent.getStringExtra("newConfirmed")
        val totalConfirmed:String = intent.getStringExtra("totalConfirmed")
        val newDeaths:String = intent.getStringExtra("newDeaths")
        val totalDeaths:String = intent.getStringExtra("totalDeath")
        val newRecovered:String = intent.getStringExtra("newRecovered")
        val totalRecovered:String = intent.getStringExtra("totalRecovered")
        val date:String = intent.getStringExtra("date")

        //country_flag_imgView

        country_name_textView.text = countryName
        country_code_textView.text = countryCode

        new_confirmed_textView.text = newConfirmed
        total_confirmed_textView.text = totalConfirmed

        new_deaths__textView.text = newDeaths
        total_deaths_textView.text = totalDeaths

        new_recovered_textView.text = newRecovered
        total_recovered_textView.text = totalRecovered


    }


}
