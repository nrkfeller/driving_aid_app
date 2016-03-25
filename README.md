# driving_aid_app

You will need to copy/paste the following to the END of your "build.gradle(Module:app)" file under the "gradle" tab in the project:


	repositories {
	    maven { url "https://jitpack.io" }
	}

	dependencies {
	    compile 'com.github.PhilJay:MPAndroidChart:v2.2.3'
	}



**For example**

	dependencies {
	    compile fileTree(dir: 'libs', include: ['*.jar'])
	    testCompile 'junit:junit:4.12'
	    compile 'com.android.support:appcompat-v7:23.2.0'
	    compile 'com.android.support:design:23.2.0'
	}

	//COPY-PASTE THE ABOVE HERE

	repositories {
	    maven { url "https://jitpack.io" }
	}

	dependencies {
	    compile 'com.github.PhilJay:MPAndroidChart:v2.2.3'
	}


Then sync your gradle file (it should ask you at the top of the gradle file that you have open).