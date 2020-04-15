package org.dvare.binding.model;


import org.dvare.parser.ExpressionParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
public class ContextsBinding {


    private Map<String, TypeBinding> contexts = new HashMap<>();


    public void addContext(String name, String types) {
        if (name != null) {
            contexts.put(name, ExpressionParser.translate(types));
        }
    }


    public void addContext(String name, Map<String, String> types) {
        if (name != null) {
            contexts.put(name, ExpressionParser.translate(types));
        }
    }


    public void addContext(String name, Class<?> types) {
        if (name != null) {
            contexts.put(name, ExpressionParser.translate(types));
        }
    }


    public void addContext(String name, TypeBinding typeBinding) {
        if (name != null) {
            contexts.put(name, typeBinding);
        }
    }


    public TypeBinding getContext(String name) {
        if (contexts.containsKey(name)) {
            return contexts.get(name);
        }
        return null;
    }


    public List<String> getContextNames() {
        return new ArrayList<>(contexts.keySet());

    }

    public void removeContext(String name) {
        contexts.remove(name);

    }


    public Map<String, TypeBinding> getContexts() {
        return contexts;
    }

    public void setContexts(Map<String, TypeBinding> contexts) {
        this.contexts = contexts;
    }
}
