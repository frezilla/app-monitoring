package eu.frezilla.tools.appmonitoring.core;

import java.util.HashMap;
import java.util.Map;

public class PoolVariables {
    
    private static class Holder {
        private static final PoolVariables INSTANCE = new PoolVariables();
    }
    
    private final Map<String, Object> pool;
    
    public PoolVariables() {
        pool = new HashMap<>();
    }
    
    public static PoolVariables getDefault() {
        return Holder.INSTANCE;
    }
    
    public Object getObject(String name) {
        return pool.get(name);
    }
    
    public <V> V getObjectOrDefault(String name, V defaultObject) {
        return (V) pool.getOrDefault(name, defaultObject);
    }
    
    public void putObjects(String name, Object object) {
        if (name == null || name.length() == 0) {
            throw new IllegalArgumentException("name ne peut pas être null ou vide");
        }
        pool.put(name, object);
    }
    
    
}
