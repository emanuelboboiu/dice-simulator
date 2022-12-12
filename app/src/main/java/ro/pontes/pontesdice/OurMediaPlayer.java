package ro.pontes.pontesdice;

import android.content.Context;
import android.media.MediaPlayer;

public class OurMediaPlayer {

    public static MediaPlayer mp = new MediaPlayer();

    public static void DeclareSound(int soundID, Context context) {

        // Determine which sound to be played:
        String soundString;
        // If the soundID is between 1 and 6, is a first dice, if is between 7
        // and 12, it is a final dice:
        if (soundID <= 6) {
            soundString = MainActivity.currentLanguage + soundID;
        } else {
            soundID = soundID - 6;
            soundString = MainActivity.currentLanguage + soundID + "a";
        }
        int resID = context.getResources().getIdentifier(soundString, "raw",
                context.getPackageName());
        mp = MediaPlayer.create(context, resID);
    }

    public static void playWait(Context context, int soundID) {
        DeclareSound(soundID, context);

        mp.start();

        mp.setOnCompletionListener(MediaPlayer::release);

        // Try to make sleep until the sound is played:
        // Determine the duration of the sound:
        long duration = mp.getDuration();

        try {
            Thread.sleep(duration + MainActivity.pBDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
