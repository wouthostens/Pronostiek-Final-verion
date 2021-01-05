package be.vives.pronostiekappwouthostens.classes

data class Matches(
    var matchID:Long, var statusMatch: String,
    var leagueID:Long, var seasonId:Long,
    var homeTeamID:Long, var homeTeamName:String,
    val homeTeamLogo:String, var awayTeamID:Long,
    var awayTeamName:String, val awayTeamLogo:String,
    var homeScore:Long, var awayScore:Long,
    var dateFrom:String, var dateTo:String, var datummatch:String
    , var isselected:Boolean, var Pronostiekid: String , var pronostiekresultaat:String??, var pronostiekUser:String??
) {
}