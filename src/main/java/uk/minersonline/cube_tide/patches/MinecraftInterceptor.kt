package uk.minersonline.cube_tide.patches

import kotlin.reflect.KCallable


class MinecraftInterceptor {
    companion object {
        @JvmStatic
        fun intercept(zuper: KCallable<*>, method: java.lang.reflect.Method, args: Array<Any?>): Any? {
            val res = zuper.call(*args)
            System.err.println(res)
            return "$res (Cube Tide)"
        }
    }
}