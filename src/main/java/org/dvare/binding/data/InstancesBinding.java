/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2016-2017 DVARE (Data Validation and Aggregation Rule Engine)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Sogiftware.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
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
        if (instances.containsKey(name)) {
            instances.remove(name);
        }
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
