package Model


import com.google.gson.annotations.SerializedName

data class CountryItem(
    @SerializedName("active")
    val active: Int,
    @SerializedName("cases")
    val cases: Int,
    @SerializedName("casesPerOneMillion")
    val casesPerOneMillion: Int,
    @SerializedName("country")
    val country: String,
    @SerializedName("countryInfo")
    val countryInfo: CountryInfo,
    @SerializedName("critical")
    val critical: Int,
    @SerializedName("deaths")
    val deaths: Int,
    @SerializedName("deathsPerOneMillion")
    val deathsPerOneMillion: Int,
    @SerializedName("recovered")
    val recovered: Int,
    @SerializedName("tests")
    val tests: Int,
    @SerializedName("testsPerOneMillion")
    val testsPerOneMillion: Int,
    @SerializedName("todayCases")
    val todayCases: Int,
    @SerializedName("todayDeaths")
    val todayDeaths: Int,
    @SerializedName("updated")
    val updated: Long
)