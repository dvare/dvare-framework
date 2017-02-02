package org.dvare.binding.data;

import java.util.HashMap;
import java.util.Map;


public class InstancesBinding {
    private Map<String, Object> instances = new HashMap<>();


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

    public Object getInstance(String name) {
        if (instances.containsKey(name)) {
            return instances.get(name);
        }
        return null;
    }

    public Map<String, Object> getInstances() {
        return instances;
    }

    public void setInstances(Map<String, Object> instances) {
        this.instances = instances;
    }
}
