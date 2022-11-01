package com.flyjingfish.formattextviewdemo

import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.style.ImageSpan
import android.util.Log
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.flyjingfish.FormatTexttextview.FormatText
import com.flyjingfish.formattextview.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        text1.setFormatText(R.string.test_text, R.string.we,R.string.you)
        text2.setFormatText(R.string.test_text, "wo","you")
        text3.setFormatText("%1\$s欢迎%2\$s", R.string.we,R.string.you)
        text4.setFormatText("%1\$s欢迎%2\$s", "wo","you")
        text5.setFormatTextBean(R.string.test_text,
            FormatText().setTextColor(R.color.colorAccent).setBold(true).setUnderline(true).setItalic(true).setResValue(R.string.we).setTextSize(30f),
            FormatImage().setImageResValue(R.mipmap.ic_launcher_round)
                .setVerticalAlignment(FormatImage.ALIGN_CENTER)
                .setWidth(20f)
                .setHeight(20f),
        )
        text6.setOnInflateImageListener(object : FormatTextView.OnInflateImageListener {
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
        text6.setFormatTextBean("%1\$s欢迎欢迎欢迎欢迎欢迎欢迎%3\$s欢迎欢迎欢迎%2\$s",
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
            FormatText().apply {
                textSize = 30f
                textColor = R.color.colorAccent
                bold = false
                italic = true
                strValue = "你们你们"
                deleteLine = true
                deleteLineColor = R.color.color_red
                deleteLineWidth = 1f
            })
        text6.setOnFormatClickListener(object : OnFormatClickListener{
            override fun onLabelClick(position: Int) {
                Toast.makeText(this@MainActivity,"onItemClick-item"+position,Toast.LENGTH_SHORT).show()
            }
        })
        text6.setOnClickListener {
            Toast.makeText(this@MainActivity,"onClick-view",Toast.LENGTH_SHORT).show()
        }

        root.setOnClickListener{
            Toast.makeText(this@MainActivity,"onClick-parent",Toast.LENGTH_SHORT).show()
        }

        text7.setOnInflateImageListener(object : HtmlTextView.OnInflateImageListener{
            override fun onInflate(
                source: String?,
                drawableListener: FormatTextView.OnReturnDrawableListener?
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
            }
        )
        text7.setOnHtmlClickListener(object :HtmlTextView.OnHtmlClickListener{
            override fun onUrlSpanClick(url: String?) {
                Toast.makeText(this@MainActivity,"onClick-Html"+url,Toast.LENGTH_SHORT).show()
                text7.setHtmlText("哈哈哈<a>lala</a>啦啦<a href=\"haha\">haha</a>哈哈哈<img width=\"20px\" height=\"20px\" src=\"https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fp0.itc.cn%2Fq_70%2Fimages03%2F20210227%2F6687c969b58d486fa2f23d8488b96ae4.jpeg&refer=http%3A%2F%2Fp0.itc.cn&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1661701773&t=19043990158a1d11c2a334146020e2ce\"></img>",
                HtmlImage().apply
                 {
                     maxWidth = 160f
                     maxHeight = 160f
                     verticalAlignment = ImageSpan.ALIGN_BASELINE
                 }
                )

            }

            override fun onImageSpanClick(url: String?) {
                Toast.makeText(this@MainActivity,"onClick-Html-image"+url,Toast.LENGTH_SHORT).show()
            }
        });
    }
}