package com.leo.extendedittext.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.leo.extendedittext.ExtendEditText;
import com.leo.extendedittext.ExtendEditTextListener;
import com.leo.extendedittext.Rule;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageButton mIbRedo;
    private ImageButton mIbUndo;

    private ImageButton mIbBold;
    private ImageButton mIbItalic;
    private ImageButton mIbUnderline;
    private ImageButton mIbStrikethrough;
    private ImageButton mIbLink;
    private ImageButton mIbBullet;
    private ImageButton mIbQuote;
    private ImageButton mIbClear;

    private ExtendEditText mExtendEdt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupView();
    }

    private void setupView() {
        mIbRedo = (ImageButton) findViewById(R.id.ib_redo);
        mIbRedo.setOnClickListener(this);
        mIbUndo = (ImageButton) findViewById(R.id.ib_undo);
        mIbUndo.setOnClickListener(this);
        mIbBold = (ImageButton) findViewById(R.id.ib_bold);
        mIbBold.setOnClickListener(this);
        mIbItalic = (ImageButton) findViewById(R.id.ib_italic);
        mIbItalic.setOnClickListener(this);
        mIbUnderline = (ImageButton) findViewById(R.id.ib_underline);
        mIbUnderline.setOnClickListener(this);
        mIbStrikethrough = (ImageButton) findViewById(R.id.ib_strikethrough);
        mIbStrikethrough.setOnClickListener(this);
        mIbLink = (ImageButton) findViewById(R.id.ib_link);
        mIbLink.setOnClickListener(this);
        mIbBullet = (ImageButton) findViewById(R.id.ib_bullet);
        mIbBullet.setOnClickListener(this);
        mIbQuote = (ImageButton) findViewById(R.id.ib_quote);
        mIbQuote.setOnClickListener(this);
        mIbClear = (ImageButton) findViewById(R.id.ib_clear);
        mIbClear.setOnClickListener(this);

        setupExtendEditText();
    }

    private void setupExtendEditText() {
        mExtendEdt = (ExtendEditText) findViewById(R.id.extend_edit_text);
//        mExtendEdt.setRule(Rule.EXCLUSIVE_INCLUSIVE);
//        mExtendEdt.setRule(Rule.EXCLUSIVE_EXCLUSIVE);
//        mExtendEdt.setRule(Rule.INCLUSIVE_INCLUSIVE);
        mExtendEdt.setExtendEditTextListener(new ExtendEditTextListener() {
            @Override
            public void onCursorChange(int selStart, int selEnd, List<Integer> formats) {
                if (formats != null) {
                    mIbBold.setSelected(formats.contains(ExtendEditText.STYLE_BOLD));
                    mIbItalic.setSelected(formats.contains(ExtendEditText.STYLE_ITALIC));
                    mIbUnderline.setSelected(formats.contains(ExtendEditText.STYLE_UNDERLINE));
                    mIbStrikethrough.setSelected(formats.contains(ExtendEditText.STYLE_STRIKETHROUGH));
                    mIbLink.setSelected(formats.contains(ExtendEditText.STYLE_LINK));
                }
            }
        });

        String text = "hello world";
        mExtendEdt.setText(text);
        mExtendEdt.selectAll();
        mExtendEdt.cover()
                .bold()
                .italic()
                .underline()
//                .strikethrough()
//                .link()
                .bullet()
                .quote()
                .action();
        mExtendEdt.setSelection(mExtendEdt.getSelectionEnd());
    }

    public void onClickRedo() {
        mExtendEdt.redo();
    }

    public void onClickUndo() {
        mExtendEdt.undo();
    }

    public void selectBold() {
        if (mIbBold.isSelected()) {
            mIbBold.setSelected(false);
        } else {
            mIbBold.setSelected(true);
        }
        mExtendEdt.bold();
    }

    public void selectItalic() {
        if (mIbItalic.isSelected()) {
            mIbItalic.setSelected(false);
        } else {
            mIbItalic.setSelected(true);
        }
        mExtendEdt.italic();
    }

    public void selectUnderline() {
        if (mIbUnderline.isSelected()) {
            mIbUnderline.setSelected(false);
        } else {
            mIbUnderline.setSelected(true);
        }
        mExtendEdt.underline();
    }

    public void selectStrikethrough() {
        if (mIbStrikethrough.isSelected()) {
            mIbStrikethrough.setSelected(false);
        } else {
            mIbStrikethrough.setSelected(true);
        }
        mExtendEdt.strikethrough();
    }

    public void onClickLink() {
        if (mIbLink.isSelected()) {
            mIbLink.setSelected(false);
        } else {
            mIbLink.setSelected(true);
        }
        mExtendEdt.link();
    }

    public void onClickBullet() {
        mExtendEdt.setRule(Rule.EXCLUSIVE_EXCLUSIVE);
        mExtendEdt.bullet();
        mExtendEdt.setRule(Rule.EXCLUSIVE_INCLUSIVE);
    }

    public void onClickQuote() {
        mExtendEdt.setRule(Rule.EXCLUSIVE_EXCLUSIVE);
        mExtendEdt.quote();
        mExtendEdt.setRule(Rule.EXCLUSIVE_INCLUSIVE);
    }

    public void onClickClear() {
        mExtendEdt.clear();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.ib_redo) {
            onClickRedo();
        } else if (id == R.id.ib_undo) {
            onClickUndo();
        } else if (id == R.id.ib_bold) {
            selectBold();
        } else if (id == R.id.ib_italic) {
            selectItalic();
        } else if (id == R.id.ib_underline) {
            selectUnderline();
        } else if (id == R.id.ib_strikethrough) {
            selectStrikethrough();
        } else if (id == R.id.ib_link) {
            onClickLink();
        } else if (id == R.id.ib_bullet) {
            onClickBullet();
        } else if (id == R.id.ib_quote) {
            onClickQuote();
        } else if (id == R.id.ib_clear) {
            onClickClear();
        }
    }
}
