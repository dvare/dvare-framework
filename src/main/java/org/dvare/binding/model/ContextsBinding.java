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


    public void addContext(String name, Class types) {
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
        if (contexts.containsKey(name)) {
            contexts.remove(name);
        }

    }


    public Map<String, TypeBinding> getContexts() {
        return contexts;
    }

    public void setContexts(Map<String, TypeBinding> contexts) {
        this.contexts = contexts;
    }
}
