package uk.minersonline.cube_tide.services;

import org.spongepowered.asm.service.IClassProvider;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ModClassProvider implements IClassProvider {
    public static URL[] getClasspathURLsFromProperty() {
        String classPath = System.getProperty("java.class.path");
        String[] paths = classPath.split(File.pathSeparator);
        List<URL> urlList = new ArrayList<>();

        try {
            for (String path : paths) {
                urlList.add(new File(path).toURI().toURL());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return urlList.toArray(new URL[0]);
    }

    @Override
    public URL[] getClassPath() {
        return getClasspathURLsFromProperty();
    }

    @Override
    public Class<?> findClass(String name) throws ClassNotFoundException {
        return Class.forName(name, true, Thread.currentThread().getContextClassLoader());
    }

    @Override
    public Class<?> findClass(String name, boolean initialize) throws ClassNotFoundException {
        return Class.forName(name, initialize, Thread.currentThread().getContextClassLoader());
    }

    @Override
    public Class<?> findAgentClass(String name, boolean initialize) throws ClassNotFoundException {
        return Class.forName(name, initialize, Thread.currentThread().getContextClassLoader());
    }
}
