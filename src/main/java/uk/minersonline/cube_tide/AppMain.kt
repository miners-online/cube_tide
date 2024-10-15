package uk.minersonline.cube_tide

import net.bytebuddy.ByteBuddy
import net.bytebuddy.agent.ByteBuddyAgent
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy
import net.bytebuddy.implementation.MethodDelegation
import net.bytebuddy.matcher.ElementMatchers
import net.minecraft.client.ClientBrandRetriever


open class AppMain {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            ByteBuddyAgent.install()

            // Modify Minecraft classes before Main is loaded
            modifyMinecraftClasses()

            // Setup Minecraft arguments
            val args2 = mutableListOf(*args)
            args2.add("--accessToken")
            args2.add("0")
            args2.add("--version")
            args2.add("1.20.1")
            args2.add("--gameDir")
            args2.add("./run")

            // Load the modified Main class (this is necessary to ensure modifications take effect)
            loadMinecraftMainClass(args2.toTypedArray())
        }

        // Function to modify any necessary classes
        private fun modifyMinecraftClasses() {
            ByteBuddy()
                .redefine(ClientBrandRetriever::class.java)
                .method(ElementMatchers.named("getClientModName")) // Match the static method to override
                .intercept(MethodDelegation.to(ClientBrandRetrieverInterceptor::class.java)) // Delegate to your interceptor
                .make()
                .load(
                    AppMain::class.java.classLoader,
                    ClassReloadingStrategy.fromInstalledAgent()
                )
        }

        // Function to load Minecraft's Main class using ByteBuddy
        private fun loadMinecraftMainClass(mainArgs: Array<String>) {
            val mainClass = ByteBuddy()
                .redefine(net.minecraft.client.main.Main::class.java)  // Ensure Main is loaded with ByteBuddy
                .make()
                .load(AppMain::class.java.classLoader, ClassReloadingStrategy.fromInstalledAgent())  // Load Main using ByteBuddy
                .loaded

            // Invoke the main method in the modified Main class
            val mainMethod = mainClass.getMethod("main", Array<String>::class.java)
            mainMethod.invoke(null, mainArgs)
        }
    }
}