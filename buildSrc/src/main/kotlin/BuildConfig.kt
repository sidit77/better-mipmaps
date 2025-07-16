object BuildConfig {

    //https://modmuss50.me/fabric.html
    //https://parchmentmc.org/docs/getting-started
    //https://modrinth.com/mod/sodium/versions
    const val MINECRAFT_VERSION: String = "1.21.6"
    const val MOD_VERSION: String = "0.2.1"
    const val FABRIC_LOADER_VERSION: String = "0.16.14"

    const val SODIUM_VERSION: String = "mc1.21.6-0.6.13"
    const val FABRIC_API_VERSION: String = "0.128.0+1.21.6"

    val PARCHMENT_VERSION: String? = "2025.06.29"

    const val BASE_ARCHIVES_NAME: String = "better-mipmaps"



    const val BUILD_VERSION: String = "${MOD_VERSION}+mc${MINECRAFT_VERSION}"

    const val SODIUM_FABRIC_VERSION: String = "${SODIUM_VERSION}-fabric"

    const val COMMON_ARCHIVES_NAME: String = "${BASE_ARCHIVES_NAME}-common"
    const val FABRIC_ARCHIVES_NAME: String = "${BASE_ARCHIVES_NAME}-fabric"
}