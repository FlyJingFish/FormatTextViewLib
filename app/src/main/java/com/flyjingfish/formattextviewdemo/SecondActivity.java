package com.flyjingfish.formattextviewdemo;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.flyjingfish.FormatTexttextview.FormatText;
import com.flyjingfish.formattextview.FormatImage;
import com.flyjingfish.formattextview.FormatTextView;


public class SecondActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FormatTextView formatTextView = findViewById(R.id.text1);
        formatTextView.setFormatText(R.string.test_text,new int[]{R.string.we,R.string.you});
        formatTextView.setFormatText(R.string.test_text,new String[]{"wo","ni"});
        formatTextView.setFormatTextBean(R.string.test_text,
                new FormatText().setTextColor(R.color.colorAccent).setStrValue("222"),
                new FormatText().setTextColor(R.color.colorAccent).setStrValue("111"));
        formatTextView.setOnInflateImageListener(new FormatTextView.OnInflateImageListener() {
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
        formatTextView.setFormatTextBean("%1$s欢迎欢迎欢迎欢迎欢迎欢迎欢迎%3$s欢迎欢迎欢迎%2$s",
                new FormatText().setTextColor(R.color.colorAccent).setBold(false)
                        .setUnderlineColor(R.color.color_red).setUnderlineMarginTop(10f).setUnderlineWidth(2f)
                        .setUnderline(true).setItalic(true).setResValue(R.string.we),
//            FormatText.setTextColor(R.color.colorPrimaryDark).setTextBold(false)
//                .setUnderlineColor(R.color.color_red).setUnderlineTopForBaseline(10f).setUnderlineWidth(2f)
//                .setTextUnderline(true).setTextItalic(false).setTextStrValue("y ou").setTextSizes(60).build(),
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
    }
}
