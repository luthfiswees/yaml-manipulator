/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package id.luthfiswees.yaml.manipulator;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.yaml.snakeyaml.Yaml;

import java.util.Map;
import java.util.HashMap;
import java.io.File;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

class YamlManipulatorTest {

    String CUSTOM_SEPARATOR = "<>";

    @Test void addKey_twoParams() {
        Map<String, Object> yaml = loadTestYamlResource("sample_normal.yaml");
        yaml = YamlManipulator.addKey(yaml, "customer:::name:::last.name", "Putra");

        Map<String, Object> temp = (Map) yaml.get("customer");
        assertNotNull(temp);
        assertTrue(temp instanceof Map);
        temp = (Map) temp.get("name");
        assertNotNull(temp);
        assertTrue(temp instanceof Map);
        assertEquals(temp.get("last.name"), "Putra");
    }

    @Test void addKey_threeParams() {
        Map<String, Object> yaml = loadTestYamlResource("sample_normal.yaml");
        yaml = YamlManipulator.addKey(yaml, "customer<>name<>last.name", "Putra", CUSTOM_SEPARATOR);

        Map<String, Object> temp = (Map) yaml.get("customer");
        assertNotNull(temp);
        assertTrue(temp instanceof Map);
        temp = (Map) temp.get("name");
        assertNotNull(temp);
        assertTrue(temp instanceof Map);
        assertEquals(temp.get("last.name"), "Putra");
    }

    @Test void deleteKey_twoParams() {
        Map<String, Object> yaml = loadTestYamlResource("sample_normal.yaml");
        yaml = YamlManipulator.deleteKey(yaml, "customer:::address:::province:::name");

        Map<String, Object> temp = (Map) yaml.get("customer");
        assertNotNull(temp);
        assertTrue(temp instanceof Map);
        temp = (Map) temp.get("address");
        assertNotNull(temp);
        assertTrue(temp instanceof Map);
        temp = (Map) temp.get("province");
        assertNotNull(temp);
        assertTrue(temp instanceof Map);
        assertNull(temp.get("name"));
    }

    @Test void deleteKey_threeParams() {
        Map<String, Object> yaml = loadTestYamlResource("sample_normal.yaml");
        yaml = YamlManipulator.deleteKey(yaml, "customer<>address<>province<>name", CUSTOM_SEPARATOR);

        Map<String, Object> temp = (Map) yaml.get("customer");
        assertNotNull(temp);
        assertTrue(temp instanceof Map);
        temp = (Map) temp.get("address");
        assertNotNull(temp);
        assertTrue(temp instanceof Map);
        temp = (Map) temp.get("province");
        assertNotNull(temp);
        assertTrue(temp instanceof Map);
        assertNull(temp.get("name"));
    }

    // Helper to load yaml file from resource directory
    private static Map<String, Object> loadTestYamlResource(String filename) {
        try {
            Yaml yaml = new Yaml();
            InputStream inputStream = new FileInputStream(
                new File(id.luthfiswees.yaml.manipulator.YamlManipulatorTest.class.getClassLoader().getResource(filename).getFile())
            );
            return yaml.load(inputStream);
        } catch (FileNotFoundException e) {
            System.out.println("Test failed. Test resource not found");
            return new HashMap<String, Object>();
        }
    }
}
