object Versions {
    const val firebase = "29.0.4"
    const val firebaseAnalytics = "20.0.2"
    const val recyclerView = "1.2.1"
    const val navigation = "2.3.5"
    const val dagger = "2.38.1"
    const val activity = "1.4.1"
    const val fragment = "1.4.0"
    const val coroutinesCore = "1.5.2"
    const val lifecycle = "2.4.1"
    const val room = "2.3.0"
    const val androidXCore = "1.7.0"
    const val androidXAppcompat = "1.4.0"
    const val material = "1.4.0"
    const val constraintLayout = "2.1.2"
    const val junit = "4.13.2"
    const val testExtJunit = "1.1.3"
    const val testEspresso = "3.4.0"
    const val workVersion = "2.7.1"
}

object WorkManager {
    const val workRuntime = "androidx.work:work-runtime-ktx:" + Versions.workVersion
}

object Firebase {
    const val firebaseBom = "com.google.firebase:firebase-bom:" + Versions.firebase
    const val firebaseAuth = "com.google.firebase:firebase-auth-ktx"
    const val firebaseFirestore = "com.google.firebase:firebase-firestore-ktx"
    const val firebaseAnalytics =
        "com.google.firebase:firebase-analytics:" + Versions.firebaseAnalytics
}

object RecyclerView {
    const val recyclerview = "androidx.recyclerview:recyclerview:" + Versions.recyclerView
}

object Navigation {
    const val navigationFragmentKtx =
        "androidx.navigation:navigation-fragment-ktx:" + Versions.navigation
    const val navigationUiKtx = "androidx.navigation:navigation-ui-ktx:" + Versions.navigation
    const val navigationFeaturesSupport = "androidx.navigation:navigation-dynamic-features-fragment:" +  Versions.navigation
}

object Dagger {
    const val dagger = "com.google.dagger:dagger:" + Versions.dagger
    const val compiler = "com.google.dagger:dagger-compiler:" + Versions.dagger
}

object Activity {
    const val activityKtx = "androidx.fragment:fragment-ktx:" + Versions.activity
}

object Fragment {
    const val fragmentKtx = "androidx.activity:activity-ktx:" + Versions.fragment
}

object Coroutines {
    const val coroutinesCore =
        "org.jetbrains.kotlinx:kotlinx-coroutines-core:" + Versions.coroutinesCore
}

object ArchComponents {
    const val viewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:" + Versions.lifecycle
    const val liveData = "androidx.lifecycle:lifecycle-livedata-ktx:" + Versions.lifecycle
    const val lifecycleCompiler = "androidx.lifecycle:lifecycle-compiler:" + Versions.lifecycle
    const val lifecycleRuntime = "androidx.lifecycle:lifecycle-runtime-ktx:" + Versions.lifecycle
}

object Room {
    const val roomRuntime = "androidx.room:room-runtime:" + Versions.room
    const val compiler = "androidx.room:room-compiler:" + Versions.room
    const val roomKtx = "androidx.room:room-ktx:" + Versions.room
}

object AndroidX {
    const val core = "androidx.core:core-ktx:" + Versions.androidXCore
    const val appcompat = "androidx.appcompat:appcompat:" + Versions.androidXAppcompat
    const val rooKtx = "androidx.room:room-ktx:" + Versions.androidXCore
    const val constraintLayout = "androidx.constraintlayout:constraintlayout:" + Versions.constraintLayout
}

object Material {
    const val material = "com.google.android.material:material:" + Versions.material
}

object Tests {
    const val junit = "junit:junit:" + Versions.junit
    const val testExtJunit = "androidx.test.ext:junit:" + Versions.testExtJunit
    const val testEspresso = "androidx.test.ext:junit:" + Versions.testEspresso
}
