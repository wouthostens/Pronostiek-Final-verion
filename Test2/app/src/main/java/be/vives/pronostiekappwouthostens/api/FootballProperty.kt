package be.vives.pronostiekappwouthostens.api
import com.squareup.moshi.Json


data class FootballProperty(
    val query: Query?,
    val data: List<Datum>?
){

     data class Datum (
         @Json(name = "match_id")
         val matchID: Long,

         @Json(name = "status_code")
         val statusCode: Long,

         val status: String,

         @Json(name = "match_start")
         val matchStart: String,

         val minute: Long? = null,

         @Json(name = "league_id")
         val leagueID: Long,

         @Json(name = "season_id")
         val seasonID: Long,

         val stage: Stage,
         val group: Any? = null,
         val round: Round,

         @Json(name = "referee_id")
         val refereeID: Long? = null,

         @Json(name = "home_team")
         val homeTeam: Team,

         @Json(name = "away_team")
         val awayTeam: Team,

         val stats: Stats,
         val venue: Venue
     )

     data class Team (
         @Json(name = "team_id")
         val teamID: Long,

         val name: String,

         @Json(name = "short_code")
         val shortCode: String,

         val logo: String,
         val country: Country
     )

     data class Country (
         @Json(name = "country_id")
         val countryID: Long,

         val name: String,

         @Json(name = "country_code")
         val countryCode: String,

         val continent: String
     )

     data class Round (
         @Json(name = "round_id")
         val roundID: Long,

         val name: String,

         @Json(name = "is_current")
         val isCurrent: Any? = null
     )

     data class Stage (
         @Json(name = "stage_id")
         val stageID: Long,

         val name: String
     )

     data class Stats (
         @Json(name = "home_score")
         val homeScore: Long,

         @Json(name = "away_score")
         val awayScore: Long,

         @Json(name = "ht_score")
         val htScore: String? = null,

         @Json(name = "ft_score")
         val ftScore: String? = null,

         @Json(name = "et_score")
         val etScore: Any? = null,

         @Json(name = "ps_score")
         val psScore: Any? = null
     )

     data class Venue (
         @Json(name = "venue_id")
         val venueID: Long,

         val name: String,
         val capacity: Long,
         val city: String,

         @Json(name = "country_id")
         val countryID: Long
     )

     data class Query (
         val apikey: String,

         @Json(name = "season_id")
         val seasonID: String,

         @Json(name = "date_from")
         val dateFrom: String,

         @Json(name = "date_to")
         val dateTo: String
     )

 }