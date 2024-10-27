package uk.minersonline.cube_tide.services;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.service.IClassBytecodeProvider;
import org.spongepowered.asm.service.ILegacyClassTransformer;
import org.spongepowered.asm.transformers.MixinClassReader;
import org.spongepowered.asm.util.perf.Profiler;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

public class ModClassBytecodeProvider implements IClassBytecodeProvider {

    private final ReentrantLock lock = new ReentrantLock();
    private final Set<String> excludedTransformers = new HashSet<>();
    private final List<ILegacyClassTransformer> transformers;

    public ModClassBytecodeProvider(List<ILegacyClassTransformer> transformers) {
        this.transformers = transformers;
    }

    @Override
    public ClassNode getClassNode(String name) throws ClassNotFoundException, IOException {
        return this.getClassNode(name, false);
    }

    @Override
    public ClassNode getClassNode(String name, boolean runTransformers) throws ClassNotFoundException, IOException {
        return this.getClassNode(name, runTransformers, 0);
    }

    @Override
    public ClassNode getClassNode(String className, boolean runTransformers, int flags) throws ClassNotFoundException, IOException {
        return this.getClassNode(className, this.getClassBytes(className, runTransformers), flags);
    }

    private ClassNode getClassNode(String className, byte[] classBytes, int flags) {
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new MixinClassReader(classBytes, className);
        classReader.accept(classNode, flags);
        return classNode;
    }

    public byte[] getClassBytes(String className, boolean runTransformers) throws ClassNotFoundException, IOException {
        String transformedName = className.replace('/', '.');
        String name = unmapClassName(transformedName);

        Profiler profiler = Profiler.getProfiler("mixin");
        Profiler.Section loadTime = profiler.begin(Profiler.ROOT, "class.load");
        byte[] classBytes = loadClassBytes(name);
        loadTime.end();

        if (runTransformers) {
            Profiler.Section transformTime = profiler.begin(Profiler.ROOT, "class.transform");
            classBytes = this.applyTransformers(name, transformedName, classBytes, profiler);
            transformTime.end();
        }

        if (classBytes == null) {
            throw new ClassNotFoundException(String.format("The specified class '%s' was not found", transformedName));
        }

        return classBytes;
    }

    private byte[] applyTransformers(String name, String transformedName, byte[] basicClass, Profiler profiler) {
        for (ILegacyClassTransformer transformer : transformers) {
            if (excludedTransformers.contains(transformer.getName())) {
                continue;
            }

            lock.lock();
            try {
                int pos = transformer.getName().lastIndexOf('.');
                String simpleName = transformer.getName().substring(pos + 1);
                Profiler.Section transformTime = profiler.begin(Profiler.FINE, simpleName.toLowerCase(Locale.ROOT));
                transformTime.setInfo(transformer.getName());

                byte[] transformedBytes = transformer.transformClassBytes(name, transformedName, basicClass);
                if (transformedBytes != null) {
                    basicClass = transformedBytes;
                }

                transformTime.end();
            } finally {
                lock.unlock();
            }
        }

        return basicClass;
    }

    private String unmapClassName(String className) {
        // A simple placeholder that reverses transformations; modify as needed
        if (className.startsWith("mapped/")) {
            return className.substring(7);
        }
        return className;
    }

    private byte[] loadClassBytes(String name) throws IOException, ClassNotFoundException {
        try {
            Class<?> clazz = Class.forName(name);
            String resourcePath = name.replace('.', '/') + ".class";
            try (var inputStream = clazz.getClassLoader().getResourceAsStream(resourcePath)) {
                if (inputStream == null) {
                    throw new ClassNotFoundException("Class " + name + " could not be loaded.");
                }
                return inputStream.readAllBytes();
            }
        } catch (ClassNotFoundException e) {
            throw new ClassNotFoundException("Could not find class " + name, e);
        }
    }

    private void addTransformerExclusion(String transformerName) {
        excludedTransformers.add(transformerName);
    }
}
