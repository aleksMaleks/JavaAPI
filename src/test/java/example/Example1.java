package example;

import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Example1 {

    public static void main(String[] args) {

// {"name":"name1","components":[{"name":"name2"},{"name":"name3"}]}
        Map<String, String> component1 = new HashMap<>();
        component1.put("name", "name2");
        Map<String, String> component2 = new HashMap<>();
        component2.put("name", "name3");

        List<Map<String, String>> components = new ArrayList<>();
        components.add(component1);
        components.add(component2);

        Map<String, Object> map = new HashMap<>();
        map.put("name", "name1");
        map.put("components", components);

        System.out.println(new JSONObject(map));
    }
}
