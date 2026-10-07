package ro.pontes.pontesdice;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;

public class OurSoundPlayer {
    private static SoundPool soundPool;
    private static int diceSound;
    private static boolean loaded;

    public static void initSounds(Context context) {
        release();
        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        soundPool = new SoundPool.Builder().setMaxStreams(1).setAudioAttributes(attributes).build();
        soundPool.setOnLoadCompleteListener((pool, sampleId, status) -> loaded = status == 0);
        diceSound = soundPool.load(context, R.raw.dice, 1);
    }

    public static void playSound() {
        if (soundPool != null && loaded) {
            soundPool.play(diceSound, MainActivity.soundVolume, MainActivity.soundVolume, 1, 0, 1f);
        }
    }

    public static void release() {
        if (soundPool != null) soundPool.release();
        soundPool = null;
        loaded = false;
    }
}
