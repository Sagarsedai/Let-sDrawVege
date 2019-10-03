// やさいいくつ知ってる？
// model  https://colab.research.google.com/drive/179JlRFf0xT82QlsaWMLRHc9Z1J4zINjK#scrollTo=5r_9wQkz0KyV&uniqifier=1
// DATA  MY PC   /media/oshimamasara/Data256GB4/Programming/python/AI/Alphabet/DATASET/
// 人参、　ブロッコリー、　じゃがいも　学習済み
// more   ペンの太さ、学習野菜、色 、採点、会話式に
// まるく塗りつぶしたらじゃがいも、細長く塗りつぶしたら人参、塗りつぶさずに書いたら概ねブロッコリー
// 参考 Android  https://github.com/nex3z/tflite-mnist-android
// 参考 model  https://www.tensorflow.org/tutorials/images/transfer_learning_with_hub,  https://www.tensorflow.org/lite/convert/python_api

package com.oshimamasara.tflite_vegetable;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.nex3z.fingerpaintview.FingerPaintView;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.fpv_paint) FingerPaintView mFpvPaint;
    @BindView(R.id.tv_prediction) TextView mTvPrediction;
    @BindView(R.id.tv_probability) TextView mTvProbability;
    @BindView(R.id.tv_timecost) TextView mTvTimeCost;

    private Classifier mClassifier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        init();
    }

    @OnClick(R.id.btn_detect)
    void onDetectClick() {
        if (mClassifier == null) {
            Log.e(LOG_TAG, "onDetectClick(): Classifier is not initialized");
            return;
        } else if (mFpvPaint.isEmpty()) {
            Toast.makeText(this, R.string.please_write_a_digit, Toast.LENGTH_SHORT).show();
            return;
        }

        Bitmap image = mFpvPaint.exportToBitmap(
                Classifier.IMG_WIDTH, Classifier.IMG_HEIGHT);
        Result result = mClassifier.classify(image);
        renderResult(result);
    }

    @OnClick(R.id.btn_clear)
    void onClearClick() {
        mFpvPaint.clear();
        mTvPrediction.setText(R.string.empty);
        mTvProbability.setText(R.string.empty);
        mTvTimeCost.setText(R.string.empty);
    }

    private void init() {
        try {
            mClassifier = new Classifier(this);
        } catch (IOException e) {
            Toast.makeText(this, R.string.failed_to_create_classifier, Toast.LENGTH_LONG).show();
            Log.e(LOG_TAG, "init(): Failed to create Classifier", e);
        }
    }

    private void renderResult(Result result) {
        mTvPrediction.setText(String.valueOf(result.getNumber()));
        mTvProbability.setText(String.valueOf(result.getProbability()));
        mTvTimeCost.setText(String.format(getString(R.string.timecost_value),
                result.getTimeCost()));
    }

}
