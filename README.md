# FormatTextViewLib
[![](https://jitpack.io/v/FlyJingFish/FormatTextViewLib.svg)](https://jitpack.io/#FlyJingFish/FormatTextViewLib)

## 多数app登陆首页都需要显示用户协议和隐私政策并且需要能够点击，遇到需要翻译多个国家语言的，多个TextView拼接会导致语序不对，而且换行也是个问题

# 特色功能

### 1、本库支持字体设置字体颜色，加粗，斜体，下划线，字体大小

### 2、本库支持下划线支持设置线宽，距离文字距离，下划线颜色

### 3、本库支持设置图片，大小，左右距离，加载本地、网络图片

### 4、支持给每个位置的富文本添加点击事件


<img src="https://github.com/FlyJingFish/FormatTextViewLib/blob/master/screenshot/Screenshot_20220907_145501.jpg" width="405px" height="842px" alt="show" />


第一步，根目录build.gradle

```gradle
    allprojects {
        repositories {
            ...
            maven { url 'https://jitpack.io' }
        }
    }
```
第二步，需要引用的build.gradle （最新版本[![](https://jitpack.io/v/FlyJingFish/FormatTextViewLib.svg)](https://jitpack.io/#FlyJingFish/FormatTextViewLib)）

```gradle
    dependencies {
        implementation 'com.github.FlyJingFish:FormatTextViewLib:latest.release.here'
    }
```
第三步，使用示例

### Kotlin调用示例

```kotlin
  //如果包含网络图片必须先设置以下方法
  textView.setOnInflateImageListener(object : FormatTextView.OnInflateImageListener {
    override fun onInflate(
        formatImage: FormatImage?,
        drawableListener: FormatTextView.OnReturnDrawableListener?
    ) {
        val requestBuilder: RequestBuilder<Drawable> =
            Glide.with(this@MainActivity).asDrawable().load(
                formatImage!!.imageUrlValue
            )
        if (formatImage!!.width > 0 && formatImage!!.height > 0) {
            requestBuilder.apply(
                RequestOptions().override(
                    formatImage!!.width.toInt(),
                    formatImage!!.height.toInt()
                ).centerCrop()
            )
        }
        requestBuilder.into(object : CustomTarget<Drawable?>() {
            override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable?>?) {
                drawableListener?.onReturnDrawable(resource)
            }

            override fun onLoadCleared(placeholder: Drawable?) {}
        })
    }
})
  //设置数据
 textView.setFormatTextBean("%1\$s欢迎欢迎欢迎欢迎欢迎欢迎%3\$s欢迎欢迎欢迎%2\$s",
            FormatText().apply {
                textSize = 30
                textColor = R.color.colorAccent
                bold = false
                italic = true
                underline = true
                underlineColor = R.color.color_red
                underlineTopForBaseline = 10f
                underlineWidth = 2f
                resValue = R.string.we
            },
            FormatImage().apply {
                imagePlaceHolder = R.mipmap.ic_launcher_round
                imageUrlValue = "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fp0.itc.cn%2Fq_70%2Fimages03%2F20210227%2F6687c969b58d486fa2f23d8488b96ae4.jpeg&refer=http%3A%2F%2Fp0.itc.cn&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1661701773&t=19043990158a1d11c2a334146020e2ce"
                verticalAlignment = FormatImage.ALIGN_CENTER
                width = 25f
                height = 25f
                marginStart = 10f
                marginEnd = 10f
            },
            FormatImage().apply {
                imagePlaceHolder = R.mipmap.ic_launcher_round
                imageUrlValue = "https://pics4.baidu.com/feed/50da81cb39dbb6fd95aa0c599b8d0d1e962b3708.jpeg?token=bf17224f51a6f4bb389e787f9c487940"
                verticalAlignment = FormatImage.ALIGN_CENTER
                width = 25f
                height = 25f
                marginStart = 10f
                marginEnd = 10f
            })
// 设置点击的监听
 textView.setOnFormatClickListener(object : OnFormatClickListener{
            override fun onLabelClick(position: Int) {//position就是设置数据的顺序
                Toast.makeText(this@MainActivity,"onItemClick-item"+position,Toast.LENGTH_SHORT).show()
            }
        })
```

### Java调用示例

```java
textView.setOnInflateImageListener(new FormatTextView.OnInflateImageListener() {
    @Override
    public void onInflate(FormatImage formatImage, final FormatTextView.OnReturnDrawableListener drawableListener) {
        RequestBuilder<Drawable> requestBuilder = Glide.with(SecondActivity.this).asDrawable().load(formatImage.imageUrlValue);
        if (formatImage.width > 0 && formatImage.height > 0) {
            requestBuilder.apply(new RequestOptions().override(((int) formatImage.width), ((int) formatImage.height)).centerCrop());
        }
        requestBuilder.into(new CustomTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                drawableListener.onReturnDrawable(resource);
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {

            }
        });
    }
});
textView.setFormatTextBean("%1$s欢迎欢迎欢迎欢迎欢迎欢迎欢迎%3$s欢迎欢迎欢迎%2$s",
        new FormatText().setTextColor(R.color.colorAccent).setBold(false)
                .setUnderlineColor(R.color.color_red).setUnderlineTopForBaseline(10f).setUnderlineWidth(2f)
                .setUnderline(true).setItalic(true).setResValue(R.string.we),
        new FormatImage().setImagePlaceHolder(R.mipmap.ic_launcher_round)
                .setImageUrlValue("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fp0.itc.cn%2Fq_70%2Fimages03%2F20210227%2F6687c969b58d486fa2f23d8488b96ae4.jpeg&refer=http%3A%2F%2Fp0.itc.cn&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1661701773&t=19043990158a1d11c2a334146020e2ce")
                .setVerticalAlignment(FormatImage.ALIGN_CENTER)
                .setWidth(30f)
                .setHeight(40f)
                .setMarginStart(20f)
                .setMarginEnd(20f),
        new FormatImage().setImagePlaceHolder(R.mipmap.ic_launcher_round)
                .setImageUrlValue("https://pics4.baidu.com/feed/50da81cb39dbb6fd95aa0c599b8d0d1e962b3708.jpeg?token=bf17224f51a6f4bb389e787f9c487940")
                .setVerticalAlignment(FormatImage.ALIGN_CENTER)
                .setWidth(30f)
                .setHeight(40f)
                .setMarginStart(20f)
                .setMarginEnd(20f));
```

## FormatText 参数一览
|属性|参数类型|描述|
|---|:---:|:---:|
|textColor|@ColorRes int|文字资源颜色Id|
|bold|boolean|文字是否加粗|
|italic|boolean|文字是否斜体|
|strValue|String|文字String类型值|
|resValue|@StringRes int|文字文本资源Id|
|textSize|int|文字字体大小(单位：SP)|
|underline|boolean|文字是否下划线|
|underlineColor|@ColorRes int|文字下划线颜色|
|underlineWidth|float|文字下划线线宽|
|underlineTopForBaseline|float|文字下划线距离文字baseline的距离|

## FormatImage 参数一览
|属性|参数类型|描述|
|---|:---:|:---:|
|imageUrlValue|String|网络图片Url|
|imageResValue|@DrawableRes int|本地图片资源Id|
|imagePlaceHolder|@DrawableRes int|网络图片加载时图片资源Id|
|width|int|图片宽度(单位：DP)|
|height|int|图片高度(单位：DP)|
|verticalAlignment|int|图片对齐方式(ALIGN_BASELINE/ALIGN_CENTER/ALIGN_BOTTOM)|
|marginLeft|float|图片距离左侧距离(单位：DP)|
|marginRight|float|图片距离右侧距离(单位：DP)|
|marginStart|float|图片距离左侧(Rtl:右侧)距离(单位：DP)|
|marginEnd|float|图片距离右侧(Rtl:左侧)距离(单位：DP)|
