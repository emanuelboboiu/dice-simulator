package ro.pontes.pontesdice;

import java.util.HashMap;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

public class OurSoundPlayer {

    public static final int S1 = R.raw.dice;

    private static SoundPool soundPool;
    private static HashMap<Integer, Integer> soundPoolMap;

    // Populate the SoundPool:
    @SuppressWarnings("deprecation")
    public static void initSounds(Context context) {

        soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 100);
        soundPoolMap = new HashMap<>(1);

        soundPoolMap.put(S1, soundPool.load(context, R.raw.dice, 13));
        // soundPoolMap.put(S2, soundPool.load(context, R.raw.ramo, 2));
    }

    // Play a given sound in the soundPool:
    public static void playSound(Context context, int soundID) {
        if (soundPool == null || soundPoolMap == null) {
            initSounds(context);
        }

        // play sound with same right and left volume, with a priority of 1:,
        // zero repeats (i.e play once), and a playback rate of 1f:

        // soundPoolMap.get(soundID)
        soundPool.play(soundID, MainActivity.soundVolume,
                MainActivity.soundVolume, 1, 0, 1f);
    }

}
