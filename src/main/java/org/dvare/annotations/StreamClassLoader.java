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
package org.dvare.annotations;

public class StreamClassLoader {/*extends ClassLoader {

    private Map<String, byte[]> classes = new HashMap<>();
    private Map<String, Class<?>> definedClasses = new HashMap<>();

    public StreamClassLoader(InputStream stream) throws IOException {
        super(StreamClassLoader.class.getClassLoader());

        JarInputStream is = new JarInputStream(stream);
        JarEntry entry = is.getNextJarEntry();
        while (entry != null) {
            if (entry.getName().contains(".class")) {
                String className = entry.getName().replace(".class", "").replace('/', '.');
                byte [] classByte = bufferrizeStream(is);
                classes.put(className, classByte);
            }
            entry = is.getNextJarEntry();
        }
    }

    public Class<?> loadClass(String className) throws ClassNotFoundException {
        try {
            return super.loadClass(className);
        } catch (ClassNotFoundException e) {
            return findClass(className);
        }
    }


    public Class<?> findClass(String className) throws ClassNotFoundException {

        Class<?> result;
        result = definedClasses.get(className);
        if (result == null) {
            byte[] classByte = classes.remove(className);
            if (classByte == null)
                throw new ClassNotFoundException();
            result = defineClass(className, classByte, 0, classByte.length, null);
            definedClasses.put(className, result);
        }
        return result;
    }

    private byte[] bufferrizeStream(InputStream is) throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        int nextValue = is.read();
        while (-1 != nextValue) {
            byteStream.write(nextValue);
            nextValue = is.read();
        }
        return byteStream.toByteArray();
    }*/

}