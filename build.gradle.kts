plugins {
    id("com.android.application") version "8.5.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.24" apply false
    id("com.google.devtools.ksp") version "1.9.24-1.0.20" apply false
    id("com.google.gms.google-services") version "4.4.2" apply false
}

val localAppData = System.getenv("LOCALAPPDATA")
if (!localAppData.isNullOrBlank()) {
    rootProject.layout.buildDirectory.set(file("$localAppData/SaiMetalFurniture/build/root"))
    subprojects {
        layout.buildDirectory.set(file("$localAppData/SaiMetalFurniture/build/$name"))
    }
}
