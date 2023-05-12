# FormatTextViewLib

[![](https://jitpack.io/v/FlyJingFish/FormatTextViewLib.svg)](https://jitpack.io/#FlyJingFish/FormatTextViewLib)
[![GitHub stars](https://img.shields.io/github/stars/FlyJingFish/FormatTextViewLib.svg)](https://github.com/FlyJingFish/FormatTextViewLib/stargazers)
[![GitHub forks](https://img.shields.io/github/forks/FlyJingFish/FormatTextViewLib.svg)](https://github.com/FlyJingFish/FormatTextViewLib/network)
[![GitHub issues](https://img.shields.io/github/issues/FlyJingFish/FormatTextViewLib.svg)](https://github.com/FlyJingFish/FormatTextViewLib/issues)
[![GitHub license](https://img.shields.io/github/license/FlyJingFish/FormatTextViewLib.svg)](https://github.com/FlyJingFish/FormatTextViewLib/blob/master/LICENSE)

# [中文版使用说明](https://github.com/FlyJingFish/FormatTextViewLib/blob/master/README-zh.md)

## Most apps need to display the user agreement and privacy policy on the home page and need to be able to click. If you need to translate multiple national languages, the splicing of multiple TextViews will cause the word order to be wrong, and line breaks are also a problem.

Support switching languages  | More support for gradient fonts
 ------ | ------   
<img src="https://github.com/FlyJingFish/FormatTextViewLib/blob/master/screenshot/Screenrecording_20230116_164552.gif" width="320px" height="640px" alt="show" />|<img src="https://github.com/FlyJingFish/FormatTextViewLib/blob/master/screenshot/Screenshot_20230511_154759.jpg" width="320px" height="640px" alt="show" />|

# special function

## New inheritance [PerfectTextView](https://github.com/FlyJingFish/PerfectTextView)

**Inherit [PerfectTextView](https://github.com/FlyJingFish/PerfectTextView) to use all its functions, you can go to see how to use [click here to view](https://github.com/FlyJingFish/PerfectTextView)**

## FormatTextView function introduction

**1. Support setting font color or even gradient color, bold, italic, underline, strikethrough, font size**

**2, support underline to set line width, distance from text, color**

**3. Support strikethrough to set line width and color**

**4. Support setting picture, picture size, left and right distance, loading local and network pictures**

**5. Support adding click events to the rich text of each position**

**6, support to set the background color for the rich text of each position**

## HtmlTextView function introduction

**1. Support loading network pictures**

**2. Support adding click events to tags with links**


## The first step, the root directory build.gradle

```gradle
     allprojects {
         repositories {
             ...
             maven { url 'https://jitpack.io' }
         }
     }
````
## The second step, the build.gradle that needs to be referenced (the latest version [![](https://jitpack.io/v/FlyJingFish/FormatTextViewLib.svg)](https://jitpack.io/#FlyJingFish/FormatTextViewLib ))

```gradle
     dependencies {
         implementation 'com.github.FlyJingFish:FormatTextViewLib:2.2.6'
     }
````
## The third step, instructions for use

## 1. Instructions for FormatTextView

### Kotlin call example

```kotlin
  //If you include network pictures, you must first set the following methods
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
            val imageWidth = dp2px(this@DemoActivity, formatImage.width)
            val imageHeight = dp2px(this@DemoActivity, formatImage.height)
            requestBuilder.apply(
                RequestOptions().override(
                    imageWidth.toInt(),
                    imageHeight.toInt()
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
  //set data
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
                gradient = Gradient(intArrayOf(Color.GREEN,Color.RED),null,Gradient.Orientation.LEFT_TO_RIGHT)
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
// Set up click listener
 textView.setOnFormatClickListener(object : OnFormatClickListener{
            override fun onLabelClick(position: Int) {//position is the order in which the data is set
                Toast.makeText(this@MainActivity,"onItemClick-item"+position,Toast.LENGTH_SHORT).show()
            }
        })
```

### Special Note

The position returned by onLabelClick of OnFormatClickListener is the subscript of the set data

**for example**

There is such a text str = "I have read and agree with %1$s and %2$s" or "I have read and agree with %2$s and %1$s"

When calling setFormatTextBean(str,"Privacy Policy","User Agreement") with the above two strings

The results of the above two strings are

1. I have read and agree to **Privacy Policy** and **User Agreement**

2. I have read and agree to **User Agreement** and **Privacy Policy**

The above two results appear only because the order of **%1$s** and **%2$s** has been exchanged, but the order of calling **setFormatTextBean** to set the data has not changed, so when clicking **Privacy Both positions are 0 when the policy** is clicked, and both positions are 1 when **User Agreement** is clicked

**Simply speaking, in setFormatTextBean(str,"Privacy Policy","User Agreement"), the corresponding position of "Privacy Policy" is 0, and the corresponding position of "User Agreement" is 1**

### Java calling example

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
                .setUnderline(true).setItalic(true).setResValue(R.string.we)
                .setGradient(new Gradient(new int[]{Color.BLUE,Color.RED},null,Gradient.Orientation.LEFT_TO_RIGHT)),
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

### Underscore FAQ
If you set the following styles for underline:

**underlineColor, underlineMarginTop, underlineWidth**

Then the underline will be drawn in the underline scheme. **underlineMarginTop** is not set (ie 0) and drawn at the default strikethrough position. If the set value is too large and the text is long to a new line, you need to set the line spacing, otherwise there will be a glide line. The problem of pressing on the next line, you can solve the problem by setting lineSpacingExtra or lineSpacingMultiplier

If you set the gradient font and you do not set **underlineColor, underlineMarginTop, underlineWidth** these 3 items, the underline will also be gradient

### Strikethrough FAQ
If you set the following styles for strikethrough:

**deleteLineColor、deleteLineWidth**

Then the strikethrough will adopt the strikethrough scheme. If you set the gradient font and you do not set **deleteLineColor, deleteLineWidth**, the strikethrough will also be gradient

### verticalAlignment FAQ

ALIGN_CENTER adds an alignment method to the current library to solve the alignment problem in the center of small icons and text. When the image setting exceeds the line height, there will be a cropping problem. If your image is large, it is recommended to use ALIGN_BASELINE

## FormatText parameter list
| property           | parameter type |                            description                             |
|--------------------|:--------------:|:------------------------------------------------------------------:|
| textColor          | @ColorRes int  |                       Text resource color Id                       |
| bold               |    boolean     |                      Whether the text is bold                      |
| italic             |    boolean     |                     Whether the text is italic                     |
| strValue           |     String     |                       Text String type value                       |
| resValue           | @StringRes int |                          Text ResourceId                           |
| textSize           |     float      |                     Text font size (unit: SP)                      |
| underline          |    boolean     |                   Whether the text is underlined                   |
| underlineColor     | @ColorRes int  |                        Text underline color                        |
| underlineWidth     |     float      |                  Text underline width (unit: DP)                   |
| underlineMarginTop |     float      | The distance by which the text underline is offset down (unit: DP) |
| deleteLine         |    boolean     |                         Delete line or not                         |
| deleteLineColor    | @ColorRes int  |                      Text strikethrough color                      |
| deleteLineWidth    |     float      |              Text strikethrough line width (unit: DP)              |
| backgroundColor    | @ColorRes int  |                     Text area background color                     |
| ignorePaintShader  |    boolean     |        Whether the text ignores the Shader of the TextView         |
| gradient           |    Gradient    |                 Text Gradient Color Configuration                  |

## FormatImage parameter list
| property          |  parameter type  |                        description                         |
|-------------------|:----------------:|:----------------------------------------------------------:|
| imageUrlValue     |      String      |                     Network Image Url                      |
| imageResValue     | @DrawableRes int |                  Local Image Resource Id                   |
| imagePlaceHolder  | @DrawableRes int | Id of the image resource when the network image is loaded  |
| width             |      float       |                   Image width (unit: DP)                   |
| height            |      float       |                  Image height (unit: DP)                   |
| verticalAlignment |       int        | Image alignment (ALIGN_BASELINE/ALIGN_CENTER/ALIGN_BOTTOM) |
| marginLeft        |      float       |           Image distance to the left (unit: DP)            |
| marginRight       |      float       |           Image distance to the right (unit: DP)           |
| marginStart       |      float       |      Image distance from left (Rtl:right) (unit: DP)       |
| marginEnd         |      float       | Image distance to the right (Rtl:left) distance (unit: DP) |
| backgroundColor   |  @ColorRes int   |                Image area background color                 |

## Two, HtmlTextView instructions

### Kotlin call example

```kotlin
 //如果包含网络图片必须先设置以下方法
 text7.setOnInflateImageListener(object : HtmlTextView.OnInflateImageListener{
    override fun onInflate(
        source: String?,
        drawableListener: HtmlTextView.OnReturnDrawableListener?
    ) {
        val requestBuilder: RequestBuilder<Drawable> =
            Glide.with(this@MainActivity).asDrawable().load(
                source
            )
        requestBuilder.into(object : CustomTarget<Drawable?>() {
            override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable?>?) {
                drawableListener?.onReturnDrawable(resource)
            }

            override fun onLoadCleared(placeholder: Drawable?) {}
        })
    }
})
text7.setHtmlText("哈哈哈<a>lala</a>啦啦<a href=\"haha\">haha</a>哈哈哈<img src=\"https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fp0.itc.cn%2Fq_70%2Fimages03%2F20210227%2F6687c969b58d486fa2f23d8488b96ae4.jpeg&refer=http%3A%2F%2Fp0.itc.cn&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1661701773&t=19043990158a1d11c2a334146020e2ce\"></img>",
    HtmlImage().apply
    {
        maxWidth = 160f
        maxHeight = 160f
        verticalAlignment = ImageSpan.ALIGN_BASELINE
        imagePlaceHolder = R.mipmap.ic_launcher
    }
)
```

### Java calling example

```java
text7.setOnInflateImageListener(new HtmlTextView.OnInflateImageListener() {
    @Override
    public void onInflate(@Nullable String source, @Nullable final HtmlTextView.OnReturnDrawableListener drawableListener) {
        RequestBuilder<Drawable> requestBuilder = Glide.with(SecondActivity.this).asDrawable().load(source);
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

text7.setHtmlText("哈哈哈<a>lala</a>啦啦<a href=\"haha\">haha</a>哈哈哈<img src=\"https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fp0.itc.cn%2Fq_70%2Fimages03%2F20210227%2F6687c969b58d486fa2f23d8488b96ae4.jpeg&refer=http%3A%2F%2Fp0.itc.cn&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1661701773&t=19043990158a1d11c2a334146020e2ce\"></img>",
        new HtmlImage().setMaxHeight(100).setMaxWidth(100));
```

## HtmlImage parameter list
| property          |  parameter type  |                        description                         |
|-------------------|:----------------:|:----------------------------------------------------------:|
| imagePlaceHolder  | @DrawableRes int | Id of the image resource when the network image is loaded  |
| maxWidth          |      float       |        The maximum width of the picture (unit: DP)         |
| maxHeight         |      float       |              Maximum image height (unit: DP)               |
| verticalAlignment |       int        | Image alignment (ALIGN_BASELINE/ALIGN_CENTER/ALIGN_BOTTOM) |

# My more open source library recommendations

Supports circle or rounded corners without Bitmap operation, can draw circle background borders or rounded box background borders, in addition to the built-in properties of ImageView, 4 new display modes are added; in addition, there are pictures that can draw arbitrary graphics only you can't think of , can't be done without it

- [ShapeImageView](https://github.com/FlyJingFish/ShapeImageView)

An image viewer that supports clicking on the small image to view the animation opening effect of the larger image

- [OpenImage](https://github.com/FlyJingFish/OpenImage) (ShapeImageView built-in)

