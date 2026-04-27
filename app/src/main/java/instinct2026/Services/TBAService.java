package instinct2026.Services;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import instinct2026.API.TBAAPI;

public class TBAService {

    public static double getConsistency(int teamNum) throws Exception {

        JsonObject latestEvent = TBAAPI.getLatestEvent(teamNum);

        if (latestEvent == null) {
            return 0;
        }

        String latestEventKey = latestEvent.get("key").getAsString();

        JsonArray matches = TBAAPI.getMatchesForTeamAtEvent(teamNum, latestEventKey);

        if (matches == null || matches.size() == 0) {
            return 0;
        }

        double totalDeviation = 0;
        int countedMatches = 0;

        for (int i = 0; i < matches.size(); i++) {
            JsonObject match = matches.get(i).getAsJsonObject();

            double expected = TBAAPI.getExpectedMatchScore(match, teamNum);
            double actual = TBAAPI.getActualMatchScore(match, teamNum);

            if (expected <= 0) {
                continue;
            }

            double deviation = Math.abs(actual - expected) / expected;

            totalDeviation += deviation;
            countedMatches++;
        }

        if (countedMatches == 0) {
            return 0;
        }

        double avgDeviation = totalDeviation / countedMatches;
        double consistency = 1.0 - avgDeviation;

        return Math.max(0, consistency * 100);
    }

}