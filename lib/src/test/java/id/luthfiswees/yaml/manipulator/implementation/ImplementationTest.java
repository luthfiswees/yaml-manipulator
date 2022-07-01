package id.luthfiswees.yaml.manipulator.implementation;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.yaml.snakeyaml.Yaml;

import java.util.Map;
import java.util.HashMap;
import java.io.File;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

class ImplementationTest {

    // Test if add normal key is successful
    @Test void addKey_addNormalKey() {
        Map<String, Object> yaml = loadTestYamlResource("sample_normal.yaml");
        yaml = Implementation.addKey(yaml, "customer:::name:::last.name", "Putra", Constant.DEFAULT_KEYSET_SEPARATOR);

        Map<String, Object> temp = (Map) yaml.get("customer");
        assertNotNull(temp);
        assertTrue(temp instanceof Map);
        temp = (Map) temp.get("name");
        assertNotNull(temp);
        assertTrue(temp instanceof Map);
        assertEquals(temp.get("last.name"), "Putra");
    }

    // Test add key with entirely new branch of values
    @Test void addKey_addWithMultipleNestedKey() {
        Map<String, Object> yaml = loadTestYamlResource("sample_normal.yaml");
        yaml = Implementation.addKey(yaml, "item:::detail:::name", "Fried Chicken", Constant.DEFAULT_KEYSET_SEPARATOR);

        Map<String, Object> temp = (Map) yaml.get("item");
        assertNotNull(temp);
        assertTrue(temp instanceof Map);
        temp = (Map) temp.get("detail");
        assertNotNull(temp);
        assertTrue(temp instanceof Map);
        assertEquals(temp.get("name"), "Fried Chicken");
    }

    // Test if the key already exist, it will not override the existing value
    @Test void addKey_testIfNotOverwriteValue() {
        Map<String, Object> yaml = loadTestYamlResource("sample_normal.yaml");
        yaml = Implementation.addKey(yaml, "customer:::name:::first.name", "Fried Chicken", Constant.DEFAULT_KEYSET_SEPARATOR);

        Map<String, Object> temp = (Map) yaml.get("customer");
        assertNotNull(temp);
        assertTrue(temp instanceof Map);
        temp = (Map) temp.get("name");
        assertNotNull(temp);
        assertTrue(temp instanceof Map);
        assertEquals(temp.get("first.name"), "Luthfi");
    }

    // Test if value datatype is preserved if added to the yaml (int)
    @Test void addKey_testIfDatatypePreserved_Int() {
        Map<String, Object> yaml = loadTestYamlResource("sample_normal.yaml");
        yaml = Implementation.addKey(yaml, "customer:::name:::age", 15, Constant.DEFAULT_KEYSET_SEPARATOR);

        Map<String, Object> temp = (Map) yaml.get("customer");
        assertNotNull(temp);
        assertTrue(temp instanceof Map);
        temp = (Map) temp.get("name");
        assertNotNull(temp);
        assertTrue(temp instanceof Map);
        assertEquals(temp.get("age"), 15);
        assertTrue(((Integer) temp.get("age")) instanceof Integer);
    }

    // Test if value datatype is preserved if added to the yaml (boolean)
    @Test void addKey_testIfDatatypePreserved_Boolean() {
        Map<String, Object> yaml = loadTestYamlResource("sample_normal.yaml");
        yaml = Implementation.addKey(yaml, "customer:::name:::dead", false, Constant.DEFAULT_KEYSET_SEPARATOR);

        Map<String, Object> temp = (Map) yaml.get("customer");
        assertNotNull(temp);
        assertTrue(temp instanceof Map);
        temp = (Map) temp.get("name");
        assertNotNull(temp);
        assertTrue(temp instanceof Map);
        assertEquals(temp.get("dead"), false);
        assertTrue(((Boolean) temp.get("dead")) instanceof Boolean);
    }

    // Test if value datatype is preserved if added to the yaml (String)
    @Test void addKey_testIfDatatypePreserved_String() {
        Map<String, Object> yaml = loadTestYamlResource("sample_normal.yaml");
        yaml = Implementation.addKey(yaml, "customer:::name:::dead", "no", Constant.DEFAULT_KEYSET_SEPARATOR);

        Map<String, Object> temp = (Map) yaml.get("customer");
        assertNotNull(temp);
        assertTrue(temp instanceof Map);
        temp = (Map) temp.get("name");
        assertNotNull(temp);
        assertTrue(temp instanceof Map);
        assertEquals(temp.get("dead"), "no");
        assertTrue(((String) temp.get("dead")) instanceof String);
    }

    // Test if delete normal key is successful
    @Test void deleteKey_deleteNormalKey() {
        Map<String, Object> yaml = loadTestYamlResource("sample_normal.yaml");
        yaml = Implementation.deleteKey(yaml, "customer:::address:::province:::name", Constant.DEFAULT_KEYSET_SEPARATOR);

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

    // Test if able to delete parent without deleting the same level node
    @Test void deleteKey_deleteKeyAsParentWithoutDeletingSameLevelNode() {
        Map<String, Object> yaml = loadTestYamlResource("sample_normal.yaml");
        yaml = Implementation.deleteKey(yaml, "customer:::address:::province", Constant.DEFAULT_KEYSET_SEPARATOR);

        Map<String, Object> temp = (Map) yaml.get("customer");
        assertNotNull(temp);
        assertTrue(temp instanceof Map);
        temp = (Map) temp.get("address");
        assertNotNull(temp);
        assertTrue(temp instanceof Map);

        assertNull(temp.get("province"));
        assertNotNull(temp.get("street"));
        assertNotNull(temp.get("block"));
        assertNotNull(temp.get("number"));
    }

    // Test if deleteKey will not delete anything when the key is not right
    @Test void deleteKey_notDeleteAnythingIfKeyIsNotValid() {
        Map<String, Object> yaml = loadTestYamlResource("sample_delete.yaml");
        yaml = Implementation.deleteKey(yaml, "customer:::address:::province", Constant.DEFAULT_KEYSET_SEPARATOR);

        Map<String, Object> temp = (Map) yaml.get("consumer");
        assertNotNull(temp);
        assertTrue(temp instanceof Map);
        assertEquals(temp.get("name"), "Luthfi");
        assertEquals(temp.get("age"), 15);
    }

    // Test if deleteKey will not delete anything when the key is not right (using the same tree)
    @Test void deleteKey_notDeleteAnythingIfKeyIsNotValidWithinSameTree() {
        Map<String, Object> yaml = loadTestYamlResource("sample_delete.yaml");
        yaml = Implementation.deleteKey(yaml, "consumer:::address", Constant.DEFAULT_KEYSET_SEPARATOR);

        Map<String, Object> temp = (Map) yaml.get("consumer");
        assertNotNull(temp);
        assertTrue(temp instanceof Map);
        assertEquals(temp.get("name"), "Luthfi");
        assertEquals(temp.get("age"), 15);
    }

    // Helper to load yaml file from resource directory
    private static Map<String, Object> loadTestYamlResource(String filename) {
        try {
            Yaml yaml = new Yaml();
            InputStream inputStream = new FileInputStream(
                new File(id.luthfiswees.yaml.manipulator.implementation.ImplementationTest.class.getClassLoader().getResource(filename).getFile())
            );
            return yaml.load(inputStream);
        } catch (FileNotFoundException e) {
            System.out.println("Test failed. Test resource not found");
            return new HashMap<String, Object>();
        }
    }
}