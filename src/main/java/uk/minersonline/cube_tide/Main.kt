package uk.minersonline.cube_tide

import net.minecraft.launchwrapper.Launch
import net.minecraft.launchwrapper.LaunchClassLoader
import org.spongepowered.asm.launch.MixinBootstrap
import org.spongepowered.asm.mixin.MixinEnvironment
import org.spongepowered.asm.mixin.Mixins
import org.spongepowered.asm.service.MixinService
import java.io.File
import java.lang.reflect.Method
import java.net.URL

object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        // Retrieve the classpath from system properties
        val classpath = System.getProperty("java.class.path")

        // Split the classpath using the system path separator
        val pathSeparator = File.pathSeparator
        val classpathEntries = classpath.split(pathSeparator)

        // Convert each classpath entry to a URL
        val urls: Array<URL> = classpathEntries.map { File(it).toURI().toURL() }.toTypedArray()

        // Initialize LaunchClassLoader with the URLs
        Launch.classLoader = LaunchClassLoader(urls)

        // Initialize the Launch blackboard
        Launch.blackboard = HashMap<String, Any>()

        // Set the context class loader to Launch's class loader
        Thread.currentThread().contextClassLoader = Launch.classLoader

        // Initialize Mixin
//        println(Mixins::class.java.classLoader.resources("/"))
        MixinBootstrap.init()
        MixinEnvironment.getDefaultEnvironment().setSide(MixinEnvironment.Side.CLIENT)
//        Mixins.addConfiguration("main/mixin.json", )
        MixinService.getService().prepare()

        // Prepare additional arguments
        val args2 = mutableListOf(*args)
        args2.add("--accessToken")
        args2.add("0")
        args2.add("--version")
        args2.add("1.20.1")
        args2.add("--gameDir")
        args2.add("./run")

        // Reflection to create instance without constructor
        val launchClass = Launch::class.java

        // Use Unsafe to create instance without calling constructor
        val unsafe = sun.misc.Unsafe::class.java.getDeclaredField("theUnsafe")
        unsafe.isAccessible = true
        val unsafeInstance = unsafe.get(null) as sun.misc.Unsafe
        val launchInstance = unsafeInstance.allocateInstance(launchClass)

        // Access the private `launch(String[] args)` method
        val launchMethod: Method = launchClass.getDeclaredMethod("launch", Array<String>::class.java)
        launchMethod.isAccessible = true

        // Invoke the method on the Launch instance
        launchMethod.invoke(launchInstance, args2.toTypedArray())
    }
}
