package main.java.algorithm.ExclusionStrategy;

import com.google.gson.*;

public class JSONExclusionStrategy implements ExclusionStrategy {
    public boolean shouldSkipClass(Class<?> clazz) {
        return clazz.getAnnotation(Exclude.class) != null;
    }
 
    public boolean shouldSkipField(FieldAttributes f) {
        return f.getAnnotation(Exclude.class) != null;
    }
}