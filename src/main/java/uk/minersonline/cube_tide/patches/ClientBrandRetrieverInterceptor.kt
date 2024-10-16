package uk.minersonline.cube_tide.patches

class ClientBrandRetrieverInterceptor {
    companion object {
        @JvmStatic
        fun getClientModName(): String {
            return "cubetide" // Your custom implementation
        }
    }
}