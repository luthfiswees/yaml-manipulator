/*
 * Yaml Manipulator is a library where you can add, update, or delete key in Yaml files
 * @author Luthfi Kurnia Putra
 */
package id.luthfiswees.yaml.manipulator;

import id.luthfiswees.yaml.manipulator.implementation.Implementation;

import java.util.Map;

public class Library {

    /*
     * Add value to provided yaml according to it's keyset
     * @param yaml implemented in Map<String, Object>
     * @param keyset, is the key where you want to put your values
     * @param value, is the value you want to fill in the key
     * @return Map, where yaml is already modified 
    */
    public static Map<String, Object> addKey(Map<String, Object> yaml, String keyset, Object value) {
        return Implementation.addKey(yaml, keyset, value);
    }

    /*
     * Remove value to provided yaml according to it's keyset
     * @param yaml implemented in Map<String, Object>
     * @param keyset, is the key you want to remove
     * @return Map, where yaml is already modified 
    */
    public static Map<String, Object> deleteKey(Map<String, Object> yaml, String keyset) {
        return Implementation.deleteKey(yaml, keyset);
    }
}
