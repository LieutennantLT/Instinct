package instinct2026.Services;

import instinct2026.API.StatboticsAPI;
import java.util.HashMap;
import java.util.Map;

public class EPAService {

    private final Map<Integer, Double> cache = new HashMap<>();
    private final Map<Integer, Double> backupCache = new HashMap<>();

    public double getEPA(int team) throws Exception {
        if (cache.containsKey(team)) {
            return cache.get(team);
        }

        double unitless = StatboticsAPI.getUnitlessEPA(team);
        double epa = StatboticsAPI.getEPA(unitless);

        cache.put(team, epa);
        return epa;
    }

    public void clearCache() {
        backupCache.clear();
        backupCache.putAll(cache);
        cache.clear();
    }

    public void retrieveBackupCache(){
        cache.clear();
        cache.putAll(backupCache);
    }

    public Map<Integer, Double> getCacheSnapshot() {
        return new HashMap<>(cache);
    }

    public Map<Integer, Double> getBackupSnapshot() {
        return new HashMap<>(backupCache);
    }
}