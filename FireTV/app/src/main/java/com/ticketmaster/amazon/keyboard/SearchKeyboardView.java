package com.ticketmaster.amazon.keyboard;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Created by Georgii_Goriachev on 5/18/2016.
 */
public class SearchKeyboardView extends TableLayout implements View.OnClickListener, View.OnFocusChangeListener {

    private static final String TAG = "SearchKeyboardView";
    private List<Character> charList = new ArrayList<>();
    private TextView selectedTextView;
    private OnCharClick onCharClicked;

    public SearchKeyboardView(Context context) {
        super(context);

        populateKeyboard();
        this.setOnFocusChangeListener(this);
        this.setOnClickListener(this);
    }

    public SearchKeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);

        populateKeyboard();
        this.setOnFocusChangeListener(this);
        this.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "onClick: " + v);
        if (v instanceof TextView) {
            if (onCharClicked == null) {
                throw new IllegalArgumentException("OnCharClick interfece must be set by method setOnCharClicked()");
            }
            onCharClicked.onType(((TextView) v).getText());
            Log.d(TAG, "TextView: " + ((TextView) v).getText());
        }
    }

    private void populateKeyboard() {
        for(char c = 'A'; c <= 'Z'; c++) {
            charList.add(Character.valueOf(c));
        }
        for(char c = '0'; c < '9'; c++) {
            charList.add(Character.valueOf(c));
        }
        charList.add("˽".toCharArray()[0]);
        charList.add("←".toCharArray()[0]);

        TableRow tr = null;
        for(int i=0; i<charList.size(); i++) {
            if(i % 10 == 0) {
                if(tr != null)
                    this.addView(tr);
                tr = new TableRow(this.getContext());
                tr.setGravity(Gravity.CENTER_HORIZONTAL);
            }
            TextView tv = new TextView(this.getContext());
            if (selectedTextView == null) {
                selectedTextView = tv;
            }
            tv.setPadding(21, 2, 21, 2);
            tv.setText(charList.get(i).toString());
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 22);
            tv.setTextColor(Color.WHITE);
            tv.setBackgroundColor(Color.TRANSPARENT);
            tv.setGravity(Gravity.CENTER);
            tv.setFocusable(true);
            tv.setFocusableInTouchMode(true);
            tv.setEnabled(true);
            tv.setOnClickListener(this);
            tv.setOnFocusChangeListener(onFocusChangeListener);
            tr.addView(tv);

        }
        if(tr.getChildCount() > 0)
            this.addView(tr);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        v.getFocusables(FOCUS_FORWARD);
        selectedTextView.requestFocus();
        Log.d(TAG, "mInputView onFocusChange " + (hasFocus ? "true" : "false") + "; view:" + v);
    }
    OnFocusChangeListener onFocusChangeListener = new OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                v.setBackgroundColor(Color.DKGRAY);
            } else {
                v.setBackgroundColor(Color.TRANSPARENT);
            }
            Log.d(TAG, "textView onFocusChange " + hasFocus + "; view:" + v);
        }
    };


    public void setOnCharClicked(OnCharClick onCharClicked) {
        this.onCharClicked  = onCharClicked;
    }
    public interface OnCharClick {
        void onType(CharSequence charString);
    }
}
