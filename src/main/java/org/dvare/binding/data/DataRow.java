package org.dvare.binding.data;


import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
public class DataRow extends LinkedHashMap<String, Object> {


    public DataRow() {
    }

    public DataRow(LinkedHashMap<String, Object> data) {
        super(data);
    }

    public DataRow(Map<String, Object> data) {
        super(data);
    }

    public Object getValue(String name) {
        return get(name);
    }

    public void addData(String name, Object value) {
        put(name, value);
    }

    /*Getter and Setters*/

    public Map<String, Object> getData() {
        return this;
    }

    public void setData(Map<String, Object> data) {
        putAll(data);
    }


}
