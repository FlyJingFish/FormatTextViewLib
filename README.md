# FormatTextViewLib
多数app登陆首页都需要显示用户协议和隐私政策并且需要能够点击，遇到需要翻译多个国家语言的，多个TextView拼接会导致语序不对，而且换行也是个问题


第一步. 根目录build.gradle
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}

第二步. 需要引用的build.gradle

dependencies {
	implementation 'com.github.FlyJingFish:FormatTextViewDemo:v1.0'
}
