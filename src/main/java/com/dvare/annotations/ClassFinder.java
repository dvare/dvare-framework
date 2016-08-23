/*The MIT License (MIT)

Copyright (c) 2016 Muhammad Hammad

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Sogiftware.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.*/


package com.dvare.annotations;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ClassFinder {

    private static final char PKG_SEPARATOR = '.';

    private static final char DIR_SEPARATOR = '/';

    private static final String CLASS_FILE_SUFFIX = ".class";

    private static final String BAD_PACKAGE_ERROR = "Unable to get resources from path '%s'. Are you sure the package '%s' exists?";


    public static List<Class<?>> find(String scannedPackage) {
        return findAnnotated(scannedPackage, null);
    }

    public static List<Class<?>> findAnnotated(String scannedPackage, Class annotation) {
        String scannedPath = scannedPackage.replace(PKG_SEPARATOR, DIR_SEPARATOR);
        URL scannedUrl = Thread.currentThread().getContextClassLoader().getResource(scannedPath);

        if (scannedUrl == null) {
            throw new IllegalArgumentException(String.format(BAD_PACKAGE_ERROR, scannedPath, scannedPackage));
        }
        File scannedDir = new File(scannedUrl.getFile());
        List<Class<?>> classes = new ArrayList<>();
        try {
            File[] files = scannedDir.listFiles();
            for (File file : files) {
                if (file != null) {
                    classes.addAll(find(file, scannedPackage, annotation));
                }
            }
        } catch (NullPointerException e) {

            if (classes.isEmpty()) {
                String pathToJar = scannedUrl.getPath().substring(5, scannedUrl.getPath().indexOf(".jar") + 4);
                classes.addAll(findjar(pathToJar, scannedPath, annotation));
            }
        }


        return classes;
    }


    private static List<Class<?>> findjar(String pathToJar, String scannedPath, Class annotation) {
        List<Class<?>> classes = new ArrayList<Class<?>>();

        JarFile jarFile = null;
        try {
            jarFile = new JarFile(pathToJar);

            Enumeration<JarEntry> e = jarFile.entries();
            while (e.hasMoreElements()) {
                JarEntry je = e.nextElement();
                if (je.isDirectory() || !je.getName().endsWith(CLASS_FILE_SUFFIX)) {
                    continue;
                }
                String resource = je.getName();
                int endIndex = resource.length() - CLASS_FILE_SUFFIX.length();
                String path = resource.substring(0, endIndex);
                if (path.contains(scannedPath)) {
                    String className = path.replace(DIR_SEPARATOR, PKG_SEPARATOR);
                    try {
                        Class aClass = Class.forName(className);
                        if (annotation != null) {
                            if (Class.forName(className).isAnnotationPresent(annotation)) {
                                classes.add(aClass);
                            }
                        } else {
                            classes.add(aClass);
                        }
                    } catch (ClassNotFoundException e2) {

                    }
                }

            }

        } catch (IOException e1) {
            e1.printStackTrace();
        }

        return classes;
    }

    private static List<Class<?>> find(File file, String scannedPackage, Class annotation) {
        List<Class<?>> classes = new ArrayList<Class<?>>();
        String resource = scannedPackage + PKG_SEPARATOR + file.getName();
        if (file.isDirectory()) {
            for (File child : file.listFiles()) {
                classes.addAll(find(child, resource, annotation));
            }
        } else if (resource.endsWith(CLASS_FILE_SUFFIX)) {
            int endIndex = resource.length() - CLASS_FILE_SUFFIX.length();
            String className = resource.substring(0, endIndex);
            try {
                Class aClass = Class.forName(className);
                if (annotation != null) {
                    if (Class.forName(className).isAnnotationPresent(annotation)) {
                        classes.add(aClass);
                    }
                } else {
                    classes.add(aClass);
                }
            } catch (ClassNotFoundException e) {

            }
        }
        return classes;
    }

}