package Model


import com.google.gson.annotations.SerializedName

data class CountrySummary(
        @SerializedName("Countries")
        val countries: List<CountryX>,
        @SerializedName("Date")
        val date: String,
        @SerializedName("Global")
        val global: Global
)