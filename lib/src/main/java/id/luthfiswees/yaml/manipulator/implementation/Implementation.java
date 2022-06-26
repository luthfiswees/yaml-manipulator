package id.luthfiswees.yaml.manipulator.implementation;

import org.yaml.snakeyaml.Yaml;

import java.util.HashMap;
import java.util.Map;
import java.lang.NullPointerException;

public class Implementation {

    static String DEFAULT_KEYSET_SEPARATOR=":::";

    /*
     * Implementation for adding key and value to yaml
     * @param yaml, the yaml object in a form of Map to be manipulated
     * @param keyset, the set of key where you want to put your values
     * @param value, the value you want to put in the key
     * @return yaml, already modified yaml where the key and value already added
    */
    public static Map<String, Object> addKey(Map<String, Object> yaml, String keyset, Object value) {
        String[] keys = keyset.split(DEFAULT_KEYSET_SEPARATOR);
        Map<String, Object> pointer = yaml;

        // Navigate every key in keyset
        for (int index=0; index < keys.length; index++) {
            String key = keys[index];
            Object obj = pointer.get(key);

            // If key we fetch have no value
            if (obj == null) {
                // if last key, add the value
                if (index == keys.length-1) pointer.put(key, value);
                // if not last key, create new map and move our pointer there
                else {
                    Map<String, Object> temp = new HashMap<String, Object>();
                    temp.put(keys[index+1], null);
                    pointer.put(key, temp);
                    pointer = temp;
                }
            } 
            // If key we fetch have values
            else {
                // if not last key, we can navigate to next key if it's a map
                if (!(index == keys.length-1)) {
                    // Check if it's a map object, if yes then navigate
                    if (obj instanceof Map) {
                        Map<String, Object> temp = (Map) obj;
                        pointer = temp;
                    }
                    // If it's have other values than Map, do not navigate there. since we do not want to override preexisting value
                }
            }
        }

        return yaml;
    }

    /*
     * Implementation for deleting key and value from yaml
     * @param yaml, the yaml object in a form of Map to be manipulated
     * @param keyset, the set of key where you want to remove
     * @return yaml, already modified yaml where the key and value already removed
    */
    public static Map<String, Object> deleteKey(Map<String, Object> yaml, String keyset) {
        String[] keys = keyset.split(DEFAULT_KEYSET_SEPARATOR);
        Map<String, Object> pointer = yaml;
        boolean isClean = false;

        // Navigate every key in keyset
        for (int index=0; index < keys.length; index++) {
            String key = keys[index];

            // Do something if pointer contains targeted key
            if (pointer.containsKey(key)) {
                // If last key, delete the key
                if (index == keys.length-1) {
                    pointer.remove(key);
                    // If there are no other keys in this depth, mark the isClean boolean as true.
                    // If isClean is true. The parent key will be deleted also to prevent weird yaml (only key without value)
                    if (pointer.size() <= 0) isClean = true;
                    break;
                }
                // If not the last key, move the pointer to the next key
                else {
                    pointer = (Map) pointer.get(key);
                }
            }
        }

        // If isClean true, delete the parent also to prevent weird yaml configurations (key only without value)
        if (isClean) {
            // Generate parent keyset
            String tempKeyset = "";
            for (int i=0; i<keys.length-1; i++) {
                tempKeyset += keys[i];
                // If not the last key (for parent key), add separator between them
                if (i != keys.length-2) tempKeyset += DEFAULT_KEYSET_SEPARATOR;
            }

            // Call the delKey recursively for the parent key
            return deleteKey(yaml, tempKeyset);
        } 
        // If isClean false, means in this depth there are still other nodes. So the generated yaml will still make sense. Ignore and return the yaml
        else {
            return yaml;
        }
    }
}