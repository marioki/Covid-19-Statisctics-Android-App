package model


import com.google.gson.annotations.SerializedName

data class GlobalStats(
    @SerializedName("active")
    val active: Int,
    @SerializedName("affectedCountries")
    val affectedCountries: Int,
    @SerializedName("cases")
    val cases: Int,
    @SerializedName("casesPerOneMillion")
    val casesPerOneMillion: Int,
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
    val testsPerOneMillion: Double,
    @SerializedName("todayCases")
    val todayCases: Int,
    @SerializedName("todayDeaths")
    val todayDeaths: Int,
    @SerializedName("updated")
    val updated: Long
)