package com.example.stocki_client.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;

public class ClearableAutoCompleteTextView extends androidx.appcompat.widget.AppCompatAutoCompleteTextView {

    public ClearableAutoCompleteTextView(Context context) {
        super(context);
        init();
    }

    public ClearableAutoCompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ClearableAutoCompleteTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        // Icons initial setzen (Lupe links, X rechts)
        updateDrawables();

        // Textänderungen beobachten, um Lupe dynamisch ein-/auszublenden
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateDrawables();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // X-Button Klick
        setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                Drawable rightDrawable = getCompoundDrawables()[2];
                if (rightDrawable != null) {
                    int leftEdgeOfRightDrawable = getWidth() - getPaddingRight() - rightDrawable.getIntrinsicWidth();
                    if (event.getX() >= leftEdgeOfRightDrawable) {
                        setText(""); // Text löschen
                        clearFocus(); // Fokus entfernen
                        // Keyboard ausblenden
                        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        if (imm != null) {
                            imm.hideSoftInputFromWindow(getWindowToken(), 0);
                        }
                        performClick(); // Accessibility
                        return true;
                    }
                }
            }
            return false;
        });
    }

    private void updateDrawables() {
        Drawable left = getText().length() == 0 ?
                getResources().getDrawable(android.R.drawable.ic_menu_search, null) : null;
        Drawable right = getResources().getDrawable(android.R.drawable.ic_menu_close_clear_cancel, null);
        setCompoundDrawablesWithIntrinsicBounds(left, null, right, null);
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }
}
