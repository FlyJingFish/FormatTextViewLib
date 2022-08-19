# FormatTextViewLib
###多数app登陆首页都需要显示用户协议和隐私政策并且需要能够点击，遇到需要翻译多个国家语言的，多个TextView拼接会导致语序不对，而且换行也是个问题

##本库支持字体设置字体颜色，加粗，斜体，下划线，字体大小

<img src="https://github.com/FlyJingFish/FormatTextViewLib/blob/master/screenshot/Screenshot_20220819_135240.jpg" width="405px" height="842px" alt="show" />


使用示例：
    可以看示例代码Demo

第一步，根目录build.gradle

```gradle
    allprojects {
        repositories {
            ...
            maven { url 'https://jitpack.io' }
        }
    }
```
第二步，需要引用的build.gradle

```gradle
    dependencies {
        implementation 'com.github.FlyJingFish:FormatTextViewLib:v1.4'
    }
```
第三步，使用示例

调用显示
```java
 textView.setFormatTextBean(R.string.test_text,
            FormatText().setColor(R.color.colorAccent).setBold(true).setUnderline(true).setItalic(true).setResValue(R.string.we).setTextSize(30),
            FormatText().setColor(R.color.colorPrimaryDark).setBold(true).setUnderline(false).setItalic(false).setStrValue("you"))
```
设置监听
```java
 textView.setOnFormatClickListener(object : OnFormatClickListener{
            override fun onLabelClick(position: Int) {//position就是设置数据的顺序
                Toast.makeText(this@MainActivity,"onItemClick-item"+position,Toast.LENGTH_SHORT).show()
            }
        })
```
