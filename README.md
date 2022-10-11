# FormatTextViewLib
[![](https://jitpack.io/v/FlyJingFish/FormatTextViewLib.svg)](https://jitpack.io/#FlyJingFish/FormatTextViewLib)

## 多数app登陆首页都需要显示用户协议和隐私政策并且需要能够点击，遇到需要翻译多个国家语言的，多个TextView拼接会导致语序不对，而且换行也是个问题

# 特色功能

### 1、本库支持字体设置字体颜色，加粗，斜体，下划线，删除线，字体大小

### 2、本库支持下划线支持设置线宽，距离文字距离，下划线颜色

### 3、本库支持删除线支持设置线宽，删除线颜色

### 4、本库支持设置图片，大小，左右距离，加载本地、网络图片

### 5、支持给每个位置的富文本添加点击事件

### 6、支持给每个位置的富文本设置背景色


<img src="https://github.com/FlyJingFish/FormatTextViewLib/blob/master/screenshot/Screenshot_20220908_184829.jpg" width="405px" height="842px" alt="show" />


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
        implementation 'com.github.FlyJingFish:FormatTextViewLib:2.1.5'
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
        if (formatImage.width > 0 && formatImage.height > 0) {
            requestBuilder.apply(
                RequestOptions().override(
                    formatImage.width.toInt(),
                    formatImage.height.toInt()
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
                textSize = 30f
                textColor = R.color.colorAccent
                bold = false
                italic = true
                underline = true
                underlineColor = R.color.color_red
                underlineMarginTop = 10f
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
                .setUnderlineColor(R.color.color_red).setUnderlineMarginTop(10f).setUnderlineWidth(2f)
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

### 下划线常见问题
如果您设置了下划线以下样式：

**underlineColor、underlineMarginTop、underlineWidth**

那么下划线将采用绘制下划线方案，**underlineMarginTop**不设置（即为0）绘制在默认的删除线位置，如果设置数值过大并且文本长到换行，则需要设置行间距，否则会出现下滑线压在下一行的问题，你可通过设置lineSpacingExtra或lineSpacingMultiplier来解决问题

### verticalAlignment 常见问题

ALIGN_CENTER 为当前库新增对齐方式旨解决在小图标和文本中心对齐问题，在图片设置超过行高时将会出现裁剪问题，如果您图片很大还是建议使用ALIGN_BASELINE

## FormatText 参数一览
|属性|参数类型|描述|
|---|:---:|:---:|
|textColor|@ColorRes int|文字资源颜色Id|
|bold|boolean|文字是否加粗|
|italic|boolean|文字是否斜体|
|strValue|String|文字String类型值|
|resValue|@StringRes int|文字文本资源Id|
|textSize|float|文字字体大小(单位：SP)|
|underline|boolean|文字是否下划线|
|underlineColor|@ColorRes int|文字下划线颜色|
|underlineWidth|float|文字下划线线宽|
|underlineMarginTop|float|文字下划线向下偏移的距离|
|deleteLine|boolean|文字是否删除线|
|deleteLineColor|@ColorRes int|文字删除线颜色|
|deleteLineWidth|float|文字删除线线宽|
|backgroundColor|@ColorRes int|文字区域背景色|

## FormatImage 参数一览
|属性|参数类型|描述|
|---|:---:|:---:|
|imageUrlValue|String|网络图片Url|
|imageResValue|@DrawableRes int|本地图片资源Id|
|imagePlaceHolder|@DrawableRes int|网络图片加载时图片资源Id|
|width|float|图片宽度(单位：DP)|
|height|float|图片高度(单位：DP)|
|verticalAlignment|int|图片对齐方式(ALIGN_BASELINE/ALIGN_CENTER/ALIGN_BOTTOM)|
|marginLeft|float|图片距离左侧距离(单位：DP)|
|marginRight|float|图片距离右侧距离(单位：DP)|
|marginStart|float|图片距离左侧(Rtl:右侧)距离(单位：DP)|
|marginEnd|float|图片距离右侧(Rtl:左侧)距离(单位：DP)|
|backgroundColor|@ColorRes int|图片区域背景色|

## 我的更多开源库推荐


支持不操作Bitmap的圆图或圆角图，可绘制圆环背景边框或圆角框背景边框，除ImageView自带属性外新增4种显示模式

- [ShapeImageView](https://github.com/FlyJingFish/ShapeImageView)

支持点击小图查看大图的动画打开效果的图片查看器

- [OpenImage](https://github.com/FlyJingFish/OpenImage)（已内置ShapeImageView）

