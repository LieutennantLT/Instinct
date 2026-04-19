package instinct2026.Services;

import instinct2026.API.StatboticsAPI;
import java.util.HashMap;
import java.util.Map;

public class EPAService {

    private static final Map<Integer, Double> cache = new HashMap<>();
    private static final Map<Integer, Double> backupCache = new HashMap<>();

    public static double getEPA(int team) throws Exception {
        if (cache.containsKey(team)) {
            return cache.get(team);
        }

        double unitless = StatboticsAPI.getUnitlessEPA(team);
        double epa = StatboticsAPI.getEPA(unitless);

        cache.put(team, epa);
        return epa;
    }

    public static void clearCache() {
        backupCache.clear();
        backupCache.putAll(cache);
        cache.clear();
    }

    public static void retrieveBackupCache(){
        cache.clear();
        cache.putAll(backupCache);
    }

    public static Map<Integer, Double> getCacheSnapshot() {
        return new HashMap<>(cache);
    }

    public static Map<Integer, Double> getBackupSnapshot() {
        return new HashMap<>(backupCache);
    }
}