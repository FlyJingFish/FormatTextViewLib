package com.flyjingfish.formattextviewdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.flyjingfish.formattextview.FormatText
import com.flyjingfish.formattextview.FormatTextView
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
        text5.setFormatTextBean(R.string.test_text, FormatText(R.color.colorAccent,true,false,R.string.we),FormatText(R.color.colorPrimaryDark,true,true,R.string.you))
        text6.setFormatTextBean(R.string.test_text, FormatText(R.color.colorAccent,true,false,"we"),FormatText(R.color.colorPrimaryDark,true,true,"you"))
        text7.setFormatTextBean("%1\$s欢迎%2\$s", FormatText(R.color.colorAccent,true,false,R.string.we),FormatText(R.color.colorPrimaryDark,true,true,R.string.you))
        text8.setFormatTextBean("%1\$s欢迎%2\$s", FormatText(R.color.colorAccent,true,false,"we"),FormatText(R.color.colorPrimaryDark,true,true,"you"))
        text8.setOnFormatClickListener(object : OnFormatClickListener{
            override fun onLabelClick(position: Int) {
                Toast.makeText(this@MainActivity,"onItemClick-item"+position,Toast.LENGTH_SHORT).show()
            }
        })
        text8.setOnClickListener {
            Toast.makeText(this@MainActivity,"onClick-view",Toast.LENGTH_SHORT).show()
        }

        root.setOnClickListener{
            Toast.makeText(this@MainActivity,"onClick-parent",Toast.LENGTH_SHORT).show()
        }

    }
}