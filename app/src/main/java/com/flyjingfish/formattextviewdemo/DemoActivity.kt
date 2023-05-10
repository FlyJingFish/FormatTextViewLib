package com.flyjingfish.formattextviewdemo

import android.graphics.LinearGradient
import android.graphics.Shader
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.flyjingfish.formattextview.*
import kotlinx.android.synthetic.main.activity_main.*

class DemoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo)
        text1.setFormatTextBean(R.string.xieyi,
            FormatText().apply {
                textSize = 22f
                textColor = R.color.colorAccent
                bold = false
                italic = true
                underline = true
                underlineColor = R.color.color_red
                underlineMarginTop = 6f
                underlineWidth = 2f
                resValue = R.string.User_Agreement
            },
            FormatText().apply {
                textSize = 22f
                textColor = R.color.colorPrimary
                bold = false
                italic = true
                underline = true
                underlineColor = R.color.colorAccent
                underlineMarginTop = 6f
                underlineWidth = 2f
                resValue = R.string.Privacy_Policy
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
        text2.setFormatTextBean(R.string.xiao_ming_book,
            FormatText().apply {
                textSize = 30f
                textColor = R.color.colorAccent
                bold = false
                italic = true
                resValue = R.string.ming
            },FormatImage().apply {
                imagePlaceHolder = R.mipmap.ic_launcher_round
                imageUrlValue = "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fp0.itc.cn%2Fq_70%2Fimages03%2F20210227%2F6687c969b58d486fa2f23d8488b96ae4.jpeg&refer=http%3A%2F%2Fp0.itc.cn&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1661701773&t=19043990158a1d11c2a334146020e2ce"
                verticalAlignment = FormatImage.ALIGN_CENTER
                width = 40f
                height = 40f
                marginStart = 10f
                marginEnd = 10f
            },
            FormatText().apply {
                textSize = 30f
                textColor = R.color.gray
                bold = true
                italic = true
                resValue = R.string.money_200
                underline = true
                underlineColor = R.color.colorPrimary
                underlineMarginTop = 10f
                underlineWidth = 3f
                ignorePaintShader = false
            },
            FormatText().apply {
                textSize = 30f
                textColor = R.color.black
                bold = false
                italic = true
                resValue = R.string.money_120
                deleteLine = true
                deleteLineColor = R.color.color_red
                deleteLineWidth = 2f
            })
        text2.setOnFormatClickListener(object : OnFormatClickListener{
            override fun onLabelClick(position: Int) {
                Toast.makeText(this@DemoActivity,"onLabelClick-position="+position,Toast.LENGTH_SHORT).show()
            }
        })
        text2.setOnClickListener {
            Toast.makeText(this@DemoActivity,"onClick-view",Toast.LENGTH_SHORT).show()
        }
        root.setOnClickListener {
            Toast.makeText(
                this@DemoActivity,
                "onClick-root",
                Toast.LENGTH_SHORT
            ).show()
        }
//
//        text2.viewTreeObserver
//            .addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
//                override fun onGlobalLayout() {
//                    text2.viewTreeObserver.removeOnGlobalLayoutListener(this)
//                    var width: Int = text2.width
//                    var height: Int = text2.height
//                    if (text2.layout != null) {
//                        width = text2.layout.width
//                        height = text2.layout.height
//                    }
//                    val mLinearGradient = LinearGradient(
//                        0f, 0f, width.toFloat(), height.toFloat(), intArrayOf(
//                            resources.getColor(R.color.colorPrimary),
//                            resources.getColor(R.color.color_red)
//                        ), null, Shader.TileMode.MIRROR
//                    )
//                    text2.paint.shader = mLinearGradient
//                }
//            })
    }

}