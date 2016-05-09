apply plugin: 'com.android.application'

android {
    compileSdkVersion rootProject.ext.compileSdkVersion
    buildToolsVersion rootProject.ext.buildToolsVersion

    defaultConfig {
        applicationId "com.icenler.test"
        minSdkVersion rootProject.ext.minSdkVersion
        targetSdkVersion rootProject.ext.targetSdkVersion
        versionCode 1
        versionName "1.0"

        testInstrumentationRunner "android.support.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
        }
    }

    packagingOptions {
        exclude 'LICENSE.txt'
    }
}

dependencies {
    compile fileTree(dir: 'libs', include: ['*.jar'])
    testCompile 'junit:junit:4.12'

    compile "com.android.support:appcompat-v7:$rootProject.ext.supportLibraryVersion"
    compile "com.android.support:design:$rootProject.ext.supportLibraryVersion"
    compile "com.android.support:support-annotations:$rootProject.ext.supportLibraryVersion"

    compile "com.google.guava:guava:$rootProject.guavaVersion"

    // Android Testing Support Library's runner and rules
    androidTestCompile(
            "com.android.support.test:rules:$rootProject.ext.rulesVersion",
            "com.android.support.test:runner:$rootProject.ext.runnerVersion"
    )

    // Mockito
    androidTestCompile(
            "org.mockito:mockito-core:$rootProject.ext.mockitoVersion",
            // Mockito Dependencies
            "com.google.dexmaker:dexmaker:1.2",
            "com.google.dexmaker:dexmaker-mockito:1.2")

    // Espresso UI Testing
    androidTestCompile(
            "com.android.support.test.espresso:espresso-core:$rootProject.ext.espressoVersion",
            "com.android.support.test.espresso:espresso-intents:$rootProject.ext.espressoVersion",
            "com.android.support.test.espresso:espresso-contrib:$rootProject.ext.espressoVersion",
            "com.android.support.test.espresso:espresso-web:$rootProject.ext.espressoVersion")
}

/*
ext {

    // Sdk and tools
    minSdkVersion = 15
    targetSdkVersion = 22
    compileSdkVersion = 23
    buildToolsVersion = '23.0.1'

    // App dependencies
    supportLibraryVersion = '23.0.1'

    guavaVersion = '18.0'

    runnerVersion = '0.4.1'
    rulesVersion = '0.4.1'

    espressoVersion = '2.2.1'
    mockitoVersion = '1.10.19'
}
* */

/**
 * Android UI自动化测试的框架：
 *  1、project/module/build.gradle
 *      # android -> defaultConfig -> testInstrumentationRunner "android.support.test.runner.AndroidJunitRunner"
 *      # android -> packagingOptions -> exclude 'LICENSE.txt'
 *      # android -> dependencies -> compile 'com.android.support:support-annotations:+'
 *      # android -> dependencies -> androidTestCompile
 *          - 'com.android.support.test.espresso:espresso-core:2.1'
 *          - 'com.android.support.test.espresso:espresso-intents:2.1'
 *          - 'com.android.support.test.espresso:espresso-contrib:2.1'
 *          - 'com.android.support.test:runner:0.2'
 *  2、"Run" > "Edit Configurations" > add "Android Tests"
 *
 *  3、module/androidTest
 *      # 创建测试类
 *          - @Rule 指定测试 Activity
 *          - @Test 声明测试方法
 *          - 核心API类:
 *              * Espresso: 匹配器
 *                  > onView(ViewMathcer).perform(ViewActoin).chek(ViewAssertion)
 *                      用法:寻找文本为 Hello World! 的控件是否显示
 *                      Espresso.onView(ViewMatchers.withText("Hello World!"))
 *                              .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
 *
 *                  > onData(ObjectMathcer).DataOptinos.perform(ViewAction).check(ViewAssertion)
 *                      DataOptinos:
 *                          inAdapterView(Matcher)
 *                          atPosition(Integer)
 *                          onChildView(Matcher)
 *                      用法:
 *                      Espresso.onData(ViewMatchers.hasFocus())
 *                              .inAdapterView(ViewMatchers.isNotChecked()).atPosition(0).onChildView(ViewMatchers.hasFocus())
 *                              .perform(ViewActions.clearText())
 *                              .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
 *
 *              * ViewActions: 界面行为工具类
 *                  > Click/Press:
 *                      click()
 *                      doubleClick()
 *                      longClick()
 *                      pressBack()
 *                      pressIMEActionButton()
 *                      pressKey([int/EspressoKey])
 *                      pressMenuKey()
 *                      closeSoftKeyboard()
 *                      openLink()
 *
 *                  > Gestures:
 *                      scrollTo()
 *                      swipeLeft()
 *                      swipeRight()
 *                      swipeUp()
 *                      swipeDown()
 *
 *                  > Text:
 *                      clearText()                     清空文本
 *                      typeText(String)                设置文本
 *                      typeTextInfoFocusedView(String) 设置文本并获取焦点
 *                      replaceText(String)             替换文本
 *
 *              * ViewMatchers: 匹配过滤
 *                  > User Properties
 *                      withId(...)                     资源ID匹配
 *                      withText(...)                   文本匹配
 *                      withTagKey(...)                 Tag键匹配
 *                      withTagValue(...)               Tag值匹配
 *                      hasContentDescription(...)      是否包含ContentDescription描述
 *                      withContentDescription(...)     ContentDescription描述匹配
 *                      withHint(...)
 *                      withSpinnerText(...)
 *                      hasLinks()
 *                      hasEllipsizedText()
 *                      HasMultilineTest()
 *
 *                  > UI Properties
 *                      isDisplayed()
 *                      isCompletelyDisplayed()
 *                      isEnabled()
 *                      hasFocus()
 *                      isClickable()
 *                      isChecked()
 *                      isNotChecked()
 *                      isSelected()
 *                      isDisplayingAtLeast((0, 100])   可见区域百分比匹配
 *                      withEffectiveVisibility(...)    可见状态匹配
 *
 *                  > Object Matcher
 *                      allOf(Matcher)                  匹配所有Matcher
 *                      anyOf(Matcher)
 *                      is(...)                         是匹配
 *                      not(...)                        否匹配
 *                      endsWith(String)
 *                      startWith(String)
 *                      instanceOf(Class)
 *
 *                  > Hierarchy
 *                      withParent(Matcher)
 *                      withChild(Matcher)
 *                      hasDescendant(Matcher)
 *                      isDescendantOfA(Matcher)        是否包含子节点匹配
 *                      hasSibling(Matcher)             是否包含兄弟节点匹配
 *                      isRoot()
 *
 *                  > Input
 *                      supportsInputMethods(...)
 *                      hasIMEAction(...)
 *
 *                  > Class
 *                      isAssignableFrom(...)           类型匹配
 *                      withClassName(...)              类名匹配
 *
 *                  > Root Mathcers
 *                      isFocusable()                   是否获取焦点
 *                      isTouchable()                   是否可吃馍
 *                      isDialog()                      是否是窗口
 *                      withDecorView()
 *                      isPlatformPopup()
 *
 *                  > See Also
 *                      Preference matcher
 *                      Cursor matcher
 *                      Layout matcher
 *
 *              * ViewAssertions: 界面判断工具类
 *                  >
 *                      matches(Matcher)
 *                      doesNotExist()
 *                      selectDescendantsMatch(...)
 *
 *                  > LayoutAssertions
 *                      noEllipsizedText(Matcher)
 *                      noMultilineButtons()
 *                      onOverlaps([Matcher])
 *
 *                  > Position Assertions
 *                      isLeftOf(Matcher)
 *                      isRightOf(Matcher)
 *                      isAbove(Matcher)
 *                      isBelow(Matcher)
 *                      isLeftAlignedWith(Matcher)
 *                      isRightAlignedWith(Matcher)
 *                      isBottomAlignedWith()Matcher
 *                      isTopAlignedWith(Matcher)
 *
 *              * Intent Matchers:
 *                  > Intent
 *                      hasAction(...)
 *                      hasCategories(...)
 *                      hasComponent(...)
 *                      hasExtra(...)
 *                      hasExtras(Matcher)
 *                      hasExtraWithKey(...)
 *                      hasType(...)
 *                      hasPackage()
 *                      toPackage(String)
 *                      hasFlag(int)
 *                      hasFlags(...)
 *                      isInternal()
 *                  > Uri
 *                      hasHost(...)
 *                      hasPath(...)
 *                      hasParamWithName(...)
 *                      hasParamWithValue(...)
 *                      hasScheme(...)
 *                      hasSchemeSpecificPart(...)
 *                  > Component Name
 *                      hasClassName(...)
 *                      hasPackageName(...)
 *                      hasShortClassName(...)
 *                      hasMyPackageName()
 *                  > Bundle
 *                      hasEntry(...)
 *                      hasKey(...)
 *                      hasValue(...)
 *
 * */
