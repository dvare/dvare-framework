package org.dvare.binding.model;


import org.dvare.expression.datatype.DataType;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
public class TypeBinding extends LinkedHashMap<String, Object> {

    public TypeBinding() {
    }

    public TypeBinding(Map<String, Object> types) {
        if (types != null) {
            putAll(types);
        }
    }

    public Object getDataType(String name) {
        if (get(name) instanceof String) {
            return DataType.valueOf((String) get(name));
        }
        return get(name);
    }

    public void addTypes(String name, Object type) {
        put(name, type);
    }


    /*Getter and Setters*/

    public Map<String, Object> getTypes() {
        return this;
    }

    public void setTypes(Map<String, Object> types) {
        if (types != null) {
            putAll(types);
        }
    }


}
