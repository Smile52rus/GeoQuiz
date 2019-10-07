package com.arzaapps.android.geoquiz;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class QuizActivity extends AppCompatActivity {

    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";

    private Button mTrueButton;
    private Button mFalseButton;
    private Button mNextButton;
    private Button mBackButton;
    private TextView mQuestionTextView;

    private Question[] mQuestionBank = new Question[]{
            new Question(R.string.question_australia, true, false),
            new Question(R.string.question_oceans, true, false),
            new Question(R.string.question_mideast, false, false),
            new Question(R.string.question_africa, false, false),
            new Question(R.string.question_americas, true, false),
            new Question(R.string.question_asia, true, false)
    };

    private int mCurrentIndex = 0;
    View.OnClickListener nextAnswerListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
            updateQuestion();
        }
    };
    View.OnClickListener backAnswerListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mCurrentIndex--;
            if (mCurrentIndex < 0) mCurrentIndex = 0;
            updateQuestion();
        }
    };
    private int mAmountAnsvered = 0;
    private int mAmountCorrects = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "Активность создана OnCreate");
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX);
            Log.d(TAG, "Данные загружены ");
        }

        mQuestionTextView = findViewById(R.id.question_text_view);
        mQuestionTextView.setOnClickListener(nextAnswerListener);

        mTrueButton = findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
            }
        });

        mFalseButton = findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);
            }
        });

        mNextButton = findViewById(R.id.next_button);
        mNextButton.setOnClickListener(nextAnswerListener);

        mBackButton = findViewById(R.id.back_button);
        mBackButton.setOnClickListener(backAnswerListener);

        updateQuestion();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "Активность стартовала Activity OnStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "Активность показана Activity OnResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "Активность спрятана Activity OnPause");
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.d(TAG, "Данные сохранены SavedInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "Активность остановлена Activity OnStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Активность уничтожена Activity OnDestroy");
    }

    private void updateQuestion() {
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);

        if (mQuestionBank[mCurrentIndex].isCompleted()) buttonsSetDisable();
        else
            buttonsSetEnable();
    }

    private void checkAnswer(boolean userPressedTrue) {
        mAmountAnsvered++;
        if (userPressedTrue == mQuestionBank[mCurrentIndex].isAnswerTrue()) {
            mAmountCorrects++;
            showToast(true);
        } else {
            showToast(false);
        }
        mQuestionBank[mCurrentIndex].setCompleted(true);

        if (mQuestionBank[mCurrentIndex].isCompleted()) buttonsSetDisable();
        if (mAmountAnsvered == mQuestionBank.length) showResult();
    }

    private void showToast(boolean isRight) {
        if (isRight) {
            Toast toast = Toast.makeText(QuizActivity.this, R.string.correct_toast, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP, 1, 1);
            toast.show();
        } else {
            Toast.makeText(QuizActivity.this,
                    R.string.incorrect_toast,
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void buttonsSetEnable() {
        mTrueButton.setEnabled(true);
        mFalseButton.setEnabled(true);
    }

    private void buttonsSetDisable() {
        mTrueButton.setEnabled(false);
        mFalseButton.setEnabled(false);
    }

    private void showResult() {
        Toast toast = Toast.makeText(QuizActivity.this, "Все отвечено. Вы ответили правильно на " + mAmountCorrects + " вопросов из " + mQuestionBank.length, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP, 1, 1);
        toast.show();
    }
}

