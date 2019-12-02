package World16.Utils;

import java.util.HashMap;
import java.util.Map;

public class ERamTag {

    //Maps
    private Map<String, Map<String, String>> tagsMap;
    //...
    //Lists
    //...

    public ERamTag(SetListMap setListMap) {
        this.tagsMap = setListMap.getTagsMap();
    }

    public void addTag(String key, String tag, String string) {
        tagsMap.computeIfAbsent(key, k -> new HashMap<>());

        tagsMap.get(key).put(tag, string);
    }

    public String getTag(String key, String tag) {
        tagsMap.computeIfAbsent(key, k -> new HashMap<>());

        return tagsMap.get(key).get(tag);
    }

    public void removeTag(String key, String tag) {
        tagsMap.computeIfAbsent(key, k -> new HashMap<>());

        tagsMap.get(key).remove(tag);
    }

    public void removeEverything(String key) {
        tagsMap.remove(key);
    }

    public void removeEverything() {
        tagsMap.clear();
    }
}