#Android Studio / Gradle / ProGuard not removing classes from test apk

Proguard is not stripping methods/classes from our test apk.


### Setup App and Tests

build.gradle:

```
android {
    compileSdkVersion 25
    buildToolsVersion "25.0.2"
    defaultConfig {
        applicationId "com.example.myapplication"
        minSdkVersion 9
        targetSdkVersion 25
        versionCode 1
        versionName "1.0"
        testInstrumentationRunner "android.support.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        debug {
        }

        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
        }
    }
}
...

```

Build debug test apk:

```
$ ./gradlew app:assembleDebugAndroidTest
```

Using [dex-method-count](https://github.com/mihaip/dex-method-counts), our test apk has 19,933 methods:

```
Read in 19933 method IDs.
<root>: 19933
    <default>: 1
    android: 16046
    	...
    com: 72
        example: 10
            myapplication: 10
                subpackage: 3
                test: 5
   ...
Overall method count: 19933
```

So our subpackage is showing. YEAH!

### Setup Proguard

Update build.gradle:


```
apply plugin: 'com.android.application'

android {
    compileSdkVersion 25
    buildToolsVersion "25.0.2"
    defaultConfig {
		...
    }
    buildTypes {
        debug {
            minifyEnabled true
            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
            testProguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-test-rules.pro'
        }

        release {
            minifyEnabled true
            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
        }
    }
}
```

This will run ProGuard on our debug apk and debug test apk.


Create `proguard-test-rules.pro`:

```
-dontwarn junit.runner.**
-dontwarn junit.framework.**
-dontwarn org.jmock.core.**
-dontwarn org.easymock.**
-dontwarn org.easymock.**
-dontwarn java.beans.**
-dontwarn java.lang.**
-dontwarn javax.lang.**
```

Rebuild debug test apk:

```
$ ./gradlew app:assembleDebugAndroidTest
```

Dex-method count on the debug test apk reveals exact same method count, and our unused `subpackage` is still there:

```
Read in 19933 method IDs.
<root>: 19933
    <default>: 1
    android: 16046
    	...
    com: 72
        example: 10
            myapplication: 10
                subpackage: 3
                test: 5
   ...
Overall method count: 19933
```