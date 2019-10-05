package com.oshimamasara.tflite_vegetable;

public class Result {

    private final int mNumber;
    private final float mProbability;
    private final long mTimeCost;

    public Result(float[] probs, long timeCost) {
        mNumber = argmax(probs);
        mProbability = probs[mNumber]*100; // %
        mTimeCost = timeCost;
    }

    public String getNumber() {
        //Log.d(LOG_TAG, "mNumber::"+mNumber);

        String[] answere = {"ブロッコリー", "にんじん", "じゃがいも"};
        String predict = answere[mNumber] ;
        //Log.d(LOG_TAG, "predict::"+ predict);
        return predict;
    }

    public float getProbability() {
        return mProbability;
    }

    public long getTimeCost() {
        return mTimeCost;
    }

    private static int argmax(float[] probs) {
        int maxIdx = -1;
        float maxProb = 0.0f;
        for (int i = 0; i < probs.length; i++) {
            if (probs[i] > maxProb) {
                maxProb = probs[i];
                maxIdx = i;
            }
        }
        return maxIdx;
    }
}
