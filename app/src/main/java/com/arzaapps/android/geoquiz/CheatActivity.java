package com.arzaapps.android.geoquiz;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class CheatActivity extends AppCompatActivity {
    private static final String EXTRA_ANSWER_IS_TRUE = "com.arzaapps.android.geoquiz.answer_is_true";
    private static final String EXTRA_ANSWER_SHOWN = "com.arzaapps.android.geoquiz.answer_shown";

    private static final String TAG = "CheatActivity";
    private static final String KEY_IS_CHEATER = "isCheater";

    private boolean mAnswerIsTrue;
    private boolean mCheating = false;

    private TextView mAnswerTextView;
    private TextView mTipsLeftTextView;
    private Button mShowAnswerButton;



    public static Intent newIntent(Context pacContext, boolean answerIsTrue) {
        Intent intent = new Intent(pacContext, CheatActivity.class);
        intent.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue);
        return intent;
    }

    public static boolean wasAnswerShown(Intent result) {
        return result.getBooleanExtra(EXTRA_ANSWER_SHOWN, false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

        if (savedInstanceState != null) {
            mCheating = savedInstanceState.getBoolean(KEY_IS_CHEATER);
            Log.d(TAG, "Данные загружены ");
        }

        mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);

        mAnswerTextView = findViewById(R.id.answer_text_view);
        mShowAnswerButton = findViewById(R.id.show_answer_button);

        if (QuizActivity.tipsLeft == 0) mShowAnswerButton.setEnabled(false);

        mTipsLeftTextView = findViewById(R.id.tips_left_text_view);
        mTipsLeftTextView.setText("Осталось " + QuizActivity.tipsLeft + " подсказок");

        mShowAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAnswerIsTrue) mAnswerTextView.setText(R.string.true_button);
                else
                    mAnswerTextView.setText(R.string.false_button);
                mCheating = true;
                setAnswerShownResult(mCheating);

                animCloseButton(mShowAnswerButton);

                QuizActivity.tipsLeft--;
                if (QuizActivity.tipsLeft < 0) QuizActivity.tipsLeft++;
                mTipsLeftTextView.setText("Осталось " + QuizActivity.tipsLeft + " подсказок");
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.d(TAG, "Данные сохранены SavedInstanceState");
        savedInstanceState.putBoolean(KEY_IS_CHEATER, mCheating);
    }

    private void setAnswerShownResult(boolean isAnswerShown){
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown);
        setResult(RESULT_OK, data);
    }

    public void animCloseButton(final Button button){
        int cx = button.getWidth() / 2;
        int cy = button.getHeight() / 2;
        float radius = button.getWidth();
        Animator anim = ViewAnimationUtils
                .createCircularReveal(button, cx, cy, radius, 0);
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                button.setVisibility(View.INVISIBLE);
            }
        });
        anim.start();
    }
}
