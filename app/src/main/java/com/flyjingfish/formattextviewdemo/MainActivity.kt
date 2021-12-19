package com.flyjingfish.formattextviewdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
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
            FormatText().setColor(R.color.colorAccent).setBold(true).setUnderline(true).setItalic(true).setResValue(R.string.we).setTextSize(30),
            FormatText().setColor(R.color.colorPrimaryDark).setBold(true).setUnderline(false).setItalic(false).setStrValue("you"))
        text6.setFormatTextBean("%1\$s欢迎%2\$s",
            FormatText().setColor(R.color.colorAccent).setBold(false).setUnderline(true).setItalic(true).setResValue(R.string.we),
            FormatText().setColor(R.color.colorPrimaryDark).setBold(false).setUnderline(true).setItalic(false).setStrValue("you").setTextSize(60))
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