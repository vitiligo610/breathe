package com.vitiligo.breathe.data.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class BreatheApiQualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class BreatheApiTenantSecretQualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class LocationIqApiQualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class LocationIqApiKeyQualifier