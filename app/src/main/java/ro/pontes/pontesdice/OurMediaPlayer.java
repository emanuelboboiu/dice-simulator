package ro.pontes.pontesdice;

import android.content.Context;
import android.media.MediaPlayer;

public class OurMediaPlayer {
    private static final int[][] SOUNDS = {
            {R.raw.en1, R.raw.en2, R.raw.en3, R.raw.en4, R.raw.en5, R.raw.en6,
                    R.raw.en1a, R.raw.en2a, R.raw.en3a, R.raw.en4a, R.raw.en5a, R.raw.en6a},
            {R.raw.it1, R.raw.it2, R.raw.it3, R.raw.it4, R.raw.it5, R.raw.it6,
                    R.raw.it1a, R.raw.it2a, R.raw.it3a, R.raw.it4a, R.raw.it5a, R.raw.it6a},
            {R.raw.ro1, R.raw.ro2, R.raw.ro3, R.raw.ro4, R.raw.ro5, R.raw.ro6,
                    R.raw.ro1a, R.raw.ro2a, R.raw.ro3a, R.raw.ro4a, R.raw.ro5a, R.raw.ro6a}
    };

    public static void playWait(Context context, int soundId) {
        if (soundId < 1 || soundId > 12) return;
        int language = "it".equals(MainActivity.currentLanguage) ? 1 :
                "ro".equals(MainActivity.currentLanguage) ? 2 : 0;
        int resource = SOUNDS[language][soundId - 1];
        MediaPlayer player = MediaPlayer.create(context, resource);
        if (player == null) return;
        try {
            player.start();
            Thread.sleep(player.getDuration() + MainActivity.pBDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            player.release();
        }
    }
}
