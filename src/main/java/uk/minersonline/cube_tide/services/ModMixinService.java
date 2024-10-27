package uk.minersonline.cube_tide.services;

import org.spongepowered.asm.launch.platform.container.IContainerHandle;
import org.spongepowered.asm.service.*;

import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;
import java.util.List;

public class ModMixinService extends MixinServiceAbstract {
    public static final URLClassLoader modClassLoader = new URLClassLoader(new URL[]{});
    public static final ModClassProvider modClassProvider = new ModClassProvider();

    @Override
    public String getName() {
        return "Cube Tide Mixin Service";
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public IClassProvider getClassProvider() {
        return modClassProvider;
    }

    @Override
    public IClassBytecodeProvider getBytecodeProvider() {
        return null;
    }

    @Override
    public ITransformerProvider getTransformerProvider() {
        return null;
    }

    @Override
    public IClassTracker getClassTracker() {
        return null;
    }

    @Override
    public IMixinAuditTrail getAuditTrail() {
        return null;
    }

    @Override
    public Collection<String> getPlatformAgents() {
        return List.of();
    }

    @Override
    public IContainerHandle getPrimaryContainer() {
        return null;
    }

    @Override
    public InputStream getResourceAsStream(String name) {
        return null;
    }
}
