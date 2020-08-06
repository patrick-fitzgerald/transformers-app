package com.example

import android.app.Application
import com.example.di.appModules
import com.example.util.TimberDebugTree
import com.example.util.TimberReleaseTree
import com.google.android.gms.security.ProviderInstaller
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber
import javax.net.ssl.SSLContext

open class App : Application() {
    override fun onCreate() {
        super.onCreate()
        initTimber()
        updateAndroidSecurityProvider()
        initKoin()
    }

    // Force TLS v1.2 for Android 4.0 devices that don't have it enabled by default
    // https://stackoverflow.com/questions/29916962/javax-net-ssl-sslhandshakeexception-javax-net-ssl-sslprotocolexception-ssl-han
    private fun updateAndroidSecurityProvider() {
        try {
            // Google Play will install latest OpenSSL
            ProviderInstaller.installIfNeeded(applicationContext)
            val sslContext = SSLContext.getInstance("TLSv1.2")
            sslContext.init(null, null, null)
            sslContext.createSSLEngine()
        } catch (e: Exception) {
            Timber.e("updateAndroidSecurityProvider error: $e")
        }
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(TimberDebugTree())
        } else {
            Timber.plant(TimberReleaseTree())
        }
    }

    open fun initKoin() {
        startKoin {
            // Android context
            androidContext(this@App)
            // logger
            androidLogger()
            // modules
            modules(appModules)
        }
    }
}
