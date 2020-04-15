package org.dvare.binding.data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
public class InstancesBinding {
    private Map<String, Object> instances = new HashMap<>();

    public InstancesBinding() {

    }

    public InstancesBinding(Map<String, Object> instances) {
        if (instances != null) {
            this.instances = instances;
        }
    }

    public void addInstance(String name, Object instance) {
        if (name != null) {
            instances.put(name, instance);
        }

    }


    public void addInstanceItem(String instanceName, String itemName, Object value) {
        if (instanceName != null) {
            Object instance = instances.get(instanceName);
            if (instance == null) {
                instance = new DataRow();
            }
            if (instance instanceof DataRow) {
                DataRow dataRow = (DataRow) instance;
                dataRow.addData(itemName, value);
                instances.put(instanceName, instance);

            }
        }

    }

    public Object getInstance(String name) {
        if (instances.containsKey(name)) {
            return instances.get(name);
        }
        return null;
    }

    public void removeInstance(String name) {
        instances.remove(name);
    }

    public void removeInstanceItem(String instanceName, String itemName) {
        if (instanceName != null) {
            Object instance = instances.get(instanceName);
            if (instance != null) {
                DataRow dataRow = (DataRow) instance;
                dataRow.getData().remove(itemName);
                instances.put(instanceName, instance);
            }
        }
    }

    public Map<String, Object> getInstances() {
        return instances;
    }

    public void setInstances(Map<String, Object> instances) {
        this.instances = instances;
    }


}
