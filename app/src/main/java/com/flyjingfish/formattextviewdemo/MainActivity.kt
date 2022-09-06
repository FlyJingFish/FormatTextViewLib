package com.flyjingfish.formattextviewdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.flyjingfish.FormatTexttextview.FormatText
import com.flyjingfish.formattextview.OnFormatClickListener
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
            FormatText().setTextColor(R.color.colorAccent).setTextBold(true).setTextUnderline(true).setTextItalic(true).setTextResValue(R.string.we).setTextSizes(30),
            FormatText().setTextColor(R.color.colorPrimaryDark).setTextBold(true).setTextUnderline(true).setTextItalic(false).setTextStrValue("you"))
        text6.setFormatTextBean("%1\$s欢迎欢迎欢迎欢迎欢迎欢迎%2\$s",
            FormatText().setTextColor(R.color.colorAccent).setTextBold(false).setTextUnderline(true).setTextItalic(true).setTextResValue(R.string.we),
            FormatText().setTextColor(R.color.colorPrimaryDark).setTextBold(false).setTextUnderline(true).setTextItalic(false).setTextStrValue("y ou").setTextSizes(60))
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

    }
}