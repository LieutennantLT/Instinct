package instinct2026.API;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import instinct2026.Services.EPAService;

public class TBAAPI {

    private static final String TEAM_URL = "https://www.thebluealliance.com/api/v3";
    private static final String API_KEY = "j8xNskbHXO6UI7gv7ONRzzU6aGThpmu5tH0tyfaAzP9qp6HbZSjWBpGfgFlHVMGM";

    //Gets the latest event for a team
    public static JsonObject getLatestEvent(int teamNum) throws Exception {
        String teamKey = "frc" + teamNum;

        String url = TEAM_URL + "/team/" + teamKey + "/events/2026/simple";

        JsonArray events = JsonParser.parseString(fetch(url)).getAsJsonArray();

        if(events.size() == 0) {
            return null;
        }

        return events.get(events.size() - 1).getAsJsonObject();
    }

    //Gets all matches for a team at a specific event
    public static JsonArray getMatchesForTeamAtEvent(int teamNum, String eventKey) throws Exception {
        String teamKey = "frc" + teamNum;

        String url = TEAM_URL + "/team/" + teamKey + "/event/" + eventKey + "/matches";

        return JsonParser.parseString(fetch(url)).getAsJsonArray();
    }

    //Gets expected score for any single alliance in one match
    public static double getExpectedMatchScore(JsonObject match, int teamNum) throws Exception{
        String teamKey = "frc" + teamNum;

        JsonObject alliances = match.getAsJsonObject("alliances");
        JsonObject red = alliances.getAsJsonObject("red");
        JsonObject blue = alliances.getAsJsonObject("blue");
        JsonArray allianceTeams;

        if(containsTeam(red.getAsJsonArray("team_keys"), teamKey)){
            allianceTeams = red.getAsJsonArray("team_keys");

        }

        if(containsTeam(blue.getAsJsonArray("team_keys"), teamKey)){
            allianceTeams = blue.getAsJsonArray("team_keys");
        } else {
        return 0;
        }

         double expectedScore = 0;

        for (int i = 0; i < allianceTeams.size(); i++) {
            String key = allianceTeams.get(i).getAsString();
            int allianceTeam = Integer.parseInt(key.replace("frc", ""));

            expectedScore += EPAService.getEPA(allianceTeam);
        }

    return expectedScore;
    }

    //Gets actual match score
    public static double getActualMatchScore(JsonObject match, int teamNum){
        String teamKey = "frc" + teamNum;

        JsonObject scoreBreakdown = match.getAsJsonObject("score_breakdown");

        if(scoreBreakdown == null){
            return 0.0;

        }

        JsonObject alliances = match.getAsJsonObject("alliances");
        JsonObject red = alliances.getAsJsonObject("red");
        JsonObject blue = alliances.getAsJsonObject("blue");

        if(containsTeam(red.getAsJsonArray("team_keys"), teamKey)){
           return red.get("score").getAsDouble();

        }

        if(containsTeam(blue.getAsJsonArray("team_keys"), teamKey)){
            return blue.get("score").getAsDouble();
        } else {
        return 0.0;
        }
    }

    //Fetches data from TBA & converts to String
     private static String fetch(String urlString) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("GET");
        conn.setRequestProperty("X-TBA-Auth-Key", API_KEY);
        conn.setRequestProperty("Accept", "application/json");

        int responseCode = conn.getResponseCode();

        if (responseCode != 200) {
            throw new RuntimeException("TBA HTTP error: " + responseCode);
        }

        BufferedReader in = new BufferedReader(
                new InputStreamReader(conn.getInputStream())
        );

        StringBuilder response = new StringBuilder();
        String inputLine;

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }

        in.close();
        conn.disconnect();

        return response.toString();
    }

    //Helper method for finding team in alliance
    private static boolean containsTeam(JsonArray alliance, String teamKey){
        for(int i = 0; i < alliance.size(); i++){
            if (alliance.get(i).getAsString().equals(teamKey)) {
                return true;

            }
        }
        return false;

    }
}
    

