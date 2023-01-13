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
import com.flyjingfish.formattextview.FormatText
import com.flyjingfish.formattextview.*
import kotlinx.android.synthetic.main.activity_main.*

class DemoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo)
        text1.setFormatTextBean("我已阅读并同意%1\$s和%2\$s",
            FormatText().apply {
                textSize = 22f
                textColor = R.color.colorAccent
                bold = false
                italic = true
                underline = true
                underlineColor = R.color.color_red
                underlineMarginTop = 10f
                underlineWidth = 2f
                strValue = "《用户协议》"
            },
            FormatText().apply {
                textSize = 22f
                textColor = R.color.colorPrimary
                bold = false
                italic = true
                underline = true
                underlineColor = R.color.color_red
                underlineMarginTop = 10f
                underlineWidth = 2f
                strValue = "《隐私政策》"
            }
        )
        text1.setOnFormatClickListener(object :OnFormatClickListener{
            override fun onLabelClick(position: Int) {
                Toast.makeText(this@DemoActivity,"onLabelClick-position="+position,Toast.LENGTH_SHORT).show()
            }

        })
        text2.setOnInflateImageListener(object : FormatTextView.OnInflateImageListener {
            override fun onInflate(
                formatImage: FormatImage?,
                drawableListener: FormatTextView.OnReturnDrawableListener?
            ) {
                val requestBuilder: RequestBuilder<Drawable> =
                    Glide.with(this@DemoActivity).asDrawable().load(
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
        text2.setFormatTextBean("%1\$s看到自己喜欢的一个卡通相册%4\$s原价%3\$s现价%2\$s",
            FormatText().apply {
                textSize = 30f
                textColor = R.color.colorAccent
                bold = false
                italic = true
                resValue = R.string.ming
            },
            FormatText().apply {
                textSize = 30f
                textColor = R.color.color_red
                bold = false
                italic = true
                strValue = "¥120元"
                deleteLine = true
                deleteLineColor = R.color.color_red
                deleteLineWidth = 1f
            },
            FormatText().apply {
                textSize = 30f
                textColor = R.color.gray
                bold = false
                italic = true
                strValue = "¥200元"
                underline = true
                underlineColor = R.color.colorPrimary
                underlineMarginTop = 10f
                underlineWidth = 2f
            },
            FormatImage().apply {
                imagePlaceHolder = R.mipmap.ic_launcher_round
                imageUrlValue = "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fp0.itc.cn%2Fq_70%2Fimages03%2F20210227%2F6687c969b58d486fa2f23d8488b96ae4.jpeg&refer=http%3A%2F%2Fp0.itc.cn&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1661701773&t=19043990158a1d11c2a334146020e2ce"
                verticalAlignment = FormatImage.ALIGN_CENTER
                width = 40f
                height = 40f
                marginStart = 10f
                marginEnd = 10f
            })
        text2.setOnFormatClickListener(object : OnFormatClickListener{
            override fun onLabelClick(position: Int) {
                Toast.makeText(this@DemoActivity,"onItemClick-item"+position,Toast.LENGTH_SHORT).show()
            }
        })
        text2.setOnClickListener {
            Toast.makeText(this@DemoActivity,"onClick-view",Toast.LENGTH_SHORT).show()
        }

    }
}