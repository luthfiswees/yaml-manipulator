# Yaml Manipulator
![example workflow](https://github.com/luthfiswees/yaml-manipulator/actions/workflows/gradle.yml/badge.svg)

Yaml manipulator library, built on top of [Snakeyaml](https://mvnrepository.com/artifact/org.yaml/snakeyaml)

## Installation

### Apache Maven
```
<dependency>
  <groupId>id.luthfiswees</groupId>
  <artifactId>yaml-manipulator</artifactId>
  <version>0.0.3</version>
</dependency>
```

### Gradle 
```
implementation 'id.luthfiswees:yaml-manipulator:0.0.3'
```

### Others
For the resource of the installation guide, you can also refer it [here](https://search.maven.org/artifact/id.luthfiswees/yaml-manipulator/0.0.3/jar)

## How to use

### Quickstart

```java
......
import org.yaml.snakeyaml.Yaml;
import id.luthfiswees.yaml.manipulator.*;
......
public class Something {
    .....
    void doSomething() {
        /**
            For example, you have yaml read using SnakeYaml that have form like this ->>
            
            item:
              id: 1
              price: 1000
              name: Luwak Coffee
              variant: Black
        */
        yaml = new Yaml();
        InputStream inputStream = this.getClass()
            .getClassLoader()
            .getResourceAsStream("item.yaml");
        Map<String, Object> obj = yaml.load(inputStream);
        
        
        // Then you want to add key "sku" with value "ABC123" under key "item". You can use addKey
        yaml = YamlManipulator.addKey(yaml, "item:::sku", "ABC123");
        /**
            You will have yaml with end result like this ->>
            
            item:
              id: 1
              price: 1000
              name: Luwak Coffee
              variant: Black
              sku: ABC123
        */
        
        // Then you want to delete key "variant". You can use deleteKey
        yaml = YamlManipulator.deleteKey(yaml, "item:::variant");
        /**
            You will have yaml with end result like this ->>
            
            item:
              id: 1
              price: 1000
              name: Luwak Coffee
              sku: ABC123
        */
        .....
    }
}
```

### Custom Separator
You can also define your own separator according to your needs
```java
// Using string separator "???"
String separator = "???";
yaml = YamlManipulator.addKey(yaml, "item???sku", "ABC123", separator);
yaml = YamlManipulator.deleteKey(yaml, "item???variant", separator);
```