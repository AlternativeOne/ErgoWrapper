package com.lexoff.ergowrapper;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class CopyOnClickTextView extends TextView {

    public CopyOnClickTextView(Context context) {
        super(context);

        setOnClickListener(v -> Utils.copyToClipboard(context, "", getNonNullText()));
    }

    public CopyOnClickTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        setOnClickListener(v -> Utils.copyToClipboard(context, "", getNonNullText()));
    }

    public CopyOnClickTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setOnClickListener(v -> Utils.copyToClipboard(context, "", getNonNullText()));
    }

    public CopyOnClickTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        setOnClickListener(v -> Utils.copyToClipboard(context, "", getNonNullText()));
    }

    private String getNonNullText(){
        return getText()==null ? "" : getText().toString();
    }

}
