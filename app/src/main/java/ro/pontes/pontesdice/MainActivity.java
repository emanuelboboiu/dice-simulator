package ro.pontes.pontesdice;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.os.Build;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityManager;
import android.widget.GridLayout;
import android.widget.TextView;
import android.view.animation.OvershootInterpolator;

import java.util.Random;

public class MainActivity extends Activity {
    // The following fields are used for the shake detection:
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;
    private PowerManager mPowerManager;
    private PowerManager.WakeLock mLockedShakeWakeLock;
    private boolean mIsResumed;
    private final BroadcastReceiver mScreenOnReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!mIsResumed) {
                mSensorManager.unregisterListener(mShakeDetector);
                releaseLockedShakeWakeLock();
            }
        }
    };
    // End fields declaration for shake detector.

    public final static String EXTRA_MESSAGE = "ro.pontes.pontesdice.MESSAGE";
    public static String message = ""; // here will be the dice as text.
    private UsefulThings ut;

    // Settings variables:
    public static int iNumberOfDice = 2;
    public static int sortMethod = 2; // 0 means no, 1 ascendant, 2 descendant.
    public static float soundVolume = 0.5F; // the sound volume for SoundPool.
    public static boolean isSoundDice = true; // sound for throwing the dice is
    // active or not.
    public static boolean isNumberSpoken = true; // if to speak or not the dice
    // numbers.
    public static long pBDS = 10; // milliseconds to add between numbers spoken.
    public static String currentLanguage = "ro";
    public static int numberOfDiceInHistory = 7;
    public static boolean isOnShake = true; // if throw on shake is or not
    // active.
    public static boolean isOnShakeInPause = false;
    public static boolean isWakeLock = true;
    public static boolean isHapticFeedback = true;

    public static Random rand = new Random();
    private Context c;

    // A boolean variable to know when numbers are spoken:
    public static volatile boolean isSpeaking = false;

    /**
     * Called when the user clicks the last dice thrown button
     */
    public void sendMessage(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        String message;
        message = "Pontes Dice"; // without a reason, just to be something sent
        // by the intent.
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    } // end function which performs when the button is clicked.

    // The about dialog:
    public void showAbout() {
        // Inflate the about message contents
        View messageView = getLayoutInflater().inflate(R.layout.about_dialog, null, false);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // builder.setIcon(R.drawable.app_icon);
        builder.setTitle(R.string.app_name);
        builder.setView(messageView);
        builder.setPositiveButton("OK", null);
        builder.create();
        builder.show();
    } // end show about program function.

    /**
     * Called when the user clicks the clear dice option in menu:
     */
    public void clearDice() {
        String tempString = getString(R.string.not_thrown_yet);
        TextView textView = findViewById(R.id.tvThrownDice);
        textView.setText(tempString);

        tempString = getString(R.string.lucky_percentage);
        TextView textView2 = findViewById(R.id.tvLuckyPercentage);
        textView2.setText(tempString);

        // Let's empty the array lastDice from UsefulThings:

        UsefulThings.clearHistory();
        showDiceAsImages(); // to clear the images from the screen.
        ((TextView) findViewById(R.id.tvResultLabel)).setText(R.string.ready_to_roll);
        updateResultAccessibility(true);

    } // end clear dice function.

    /**
     * Called when the user clicks the audio settings option in menu:
     */
    public void goToSettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        String message;
        message = "Pontes Dice"; // without a reason, just to be something sent
        // by the intent.
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    } // end function which performs when the option in menu is clicked.

    /**
     * Called when the user clicks the audio settings option in menu:
     */
    public void goToLanguageSettings() {
        Intent intent = new Intent(this, LanguageActivity.class);
        String message;
        message = "Pontes Dice"; // without a reason, just to be something sent
        // by the intent.
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    } // end function which performs when the option language settings in menu
    // is clicked.

    /**
     * Called when the user clicks the other settings option in menu:
     */
    public void goToOtherSettings() {
        Intent intent = new Intent(this, OtherSettingsActivity.class);
        String message;
        message = "Pontes Dice"; // without a reason, just to be something sent
        // by the intent.
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    } // end function which performs when the option other settings in menu is
    // clicked.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        c = getApplicationContext();

        ut = new UsefulThings(c);

        UsefulThings.initialiseThings();

        ut.chargeSettings();
        updateDiceCount();

        OurSoundPlayer.initSounds(c);

        // To keep screen awake:
        updateWakeLock();

        // // ShakeDetector initialisation
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mPowerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mLockedShakeWakeLock = mPowerManager.newWakeLock(
                PowerManager.PARTIAL_WAKE_LOCK, "PontesDice:lockedShake");
        mLockedShakeWakeLock.setReferenceCounted(false);
        registerReceiver(mScreenOnReceiver, new IntentFilter(Intent.ACTION_SCREEN_ON));
        mShakeDetector = new ShakeDetector();
        /*
         * method you would use to setup whatever you want done once the
         * device has been shook.
         */
        mShakeDetector.setOnShakeListener(this::handleShakeEvent);
        // End initialisation of the shake detector.

    } // end onCreate method of the main activity.

    @Override
    public void onResume() {
        super.onResume();
        mIsResumed = true;
        releaseLockedShakeWakeLock();

        fillLastDiceTextView(); // to refill onResume, orientation change or
        // reappear.
        fillLuckyPercentageTextView(); // to refill onResume, orientation change
        // or reappear.
        showDiceAsImages(); // redraw the images at the restart like when
        // orientation is changed.
        ((TextView) findViewById(R.id.tvResultLabel)).setText(
                UsefulThings.lastDice[0] == null ? R.string.ready_to_roll : R.string.result_label);
        updateResultAccessibility(false);
        updateDiceCount();
        updateWakeLock();

        // To delete, just a test:
        /*
         * TextView textView = (TextView) findViewById(R.id.tvThrownDice);
         * textView.setText(UsefulThings.curLocale);
         */

        if (isOnShake && mAccelerometer != null) {
            // Add the following line to register the Session Manager Listener
            // onResume
            mSensorManager.registerListener(mShakeDetector, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
        } else {
            mSensorManager.unregisterListener(mShakeDetector);
        }
    } // end onResume method.

    @Override
    public void onPause() {
        mIsResumed = false;
        updateLockedShakeState();
        super.onPause();
    }

    @Override
    protected void onStop() {
        // Some devices report the screen as off only after onPause.
        updateLockedShakeState();
        super.onStop();
    }

    private void updateLockedShakeState() {
        if (isOnShake && isOnShakeInPause && mAccelerometer != null
                && !mPowerManager.isInteractive()) {
            mSensorManager.registerListener(mShakeDetector, mAccelerometer,
                    SensorManager.SENSOR_DELAY_UI);
            if (!mLockedShakeWakeLock.isHeld()) mLockedShakeWakeLock.acquire();
        } else {
            mSensorManager.unregisterListener(mShakeDetector);
            releaseLockedShakeWakeLock();
        }
    }

    private void releaseLockedShakeWakeLock() {
        if (mLockedShakeWakeLock != null && mLockedShakeWakeLock.isHeld()) {
            mLockedShakeWakeLock.release();
        }
    }

    public void onDestroy() {
        unregisterReceiver(mScreenOnReceiver);
        mSensorManager.unregisterListener(mShakeDetector);
        releaseLockedShakeWakeLock();
        OurSoundPlayer.release();
        super.onDestroy();
    } // end onDestroy() method.

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.clear_dice) {
            clearDice();
        } else if (id == R.id.action_settings) {
            goToSettings();
        } else if (id == R.id.language_settings) {
            goToLanguageSettings();
        } else if (id == R.id.other_settings) {
            goToOtherSettings();
        } else if (id == R.id.default_settings) {
            // Get the strings:
            String tempTitle = getString(R.string.set_defaults_title);
            String tempBody = getString(R.string.set_defaults_body);
            new AlertDialog.Builder(this).setTitle(tempTitle).setMessage(tempBody).setIcon(android.R.drawable.ic_dialog_alert).setPositiveButton(android.R.string.yes, (dialog, whichButton) -> {
                UsefulThings tempUT = new UsefulThings(c);
                tempUT.setDefaultSettings();
                tempUT.chargeSettings();
                // To be sure the shake detection will be
                // again available, as a default setting:
                mSensorManager.registerListener(mShakeDetector, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
            }).setNegativeButton(android.R.string.no, null).show();
        } else if (id == R.id.about_program) {
            showAbout();
        } else if (id == R.id.exit_program) {
            this.finish();

        } // end if about item was chosen.
        return true;
    }

    public void handleShakeEvent(int count) {
        throwActions();
    }

    public void throwDice(View view) {
        throwActions();
    }

    public void throwActions() {
        // Throws only if there are not still spoken:
        if (!isSpeaking) {

            int[] aDice = new int[iNumberOfDice];

            // We play the throw dice sound:
            if (isSoundDice) OurSoundPlayer.playSound();

            // We need also a Random object, it is instanced at the beginning of
            // the class.:

            // Let's generate the dice here:
            for (int i = 0; i < iNumberOfDice; i++) {
                // One die:
                int die = rand.nextInt(6) + 1;
                // Put it into the array:
                aDice[i] = die;
            }

            // Sort the dice if is set 1 or 2 for sortMethod:
            if (sortMethod == 1) {
                // Ascendant sorting:
                java.util.Arrays.sort(aDice);
            } else if (sortMethod == 2) {
                // Descendant sorting:
                java.util.Arrays.sort(aDice);
                // Now let's reverse the order:
                for (int i = 0; i < aDice.length / 2; i++) {
                    int temp;
                    temp = aDice[i];
                    aDice[i] = aDice[aDice.length - (i + 1)];
                    aDice[aDice.length - (i + 1)] = temp;
                } // end for.
            } // end if must be sort in descendant order.

            message = "";
            // Create the string from the array:
            for (int j : aDice) {
                message += j + ", ";
            }
            // Cut the last comma:
            message = message.substring(0, message.length() - 2);

            // Add this throw into the lastDice array:
            UsefulThings.addLastDice(message); // it adds the last hand at 0
            // index and pushes all the
            // hands before.

            // Now call the method which shows the dice as images:
            showDiceAsImages(true);

            fillLastDiceTextView(); // a method created below in this class. to
            // fill the dedicated text view for dice as
            // text.

            // Calculate and fill the text view with the luck percentage:
            UsefulThings.calculateAverageOfLastHandsOfDice();
            fillLuckyPercentageTextView();

            ((TextView) findViewById(R.id.tvResultLabel)).setText(R.string.result_label);
            updateResultAccessibility(true);
            // Play dice sounds if activated:
            if (isNumberSpoken && !isTouchExplorationEnabled()) {
                // Let's try playing sound in a new thread:

                isSpeaking = true;
                final int[] spokenDice = aDice.clone();
                new Thread(() -> {
                    try {
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                    for (int i = 0; i < spokenDice.length; i++) {
                        if (i < spokenDice.length - 1) {
                            OurMediaPlayer.playWait(getApplicationContext(), spokenDice[i]);
                        } else {
                            OurMediaPlayer.playWait(getApplicationContext(), spokenDice[i] + 6);
                        }
                    } // end for.
                    } finally {
                        isSpeaking = false;
                    }

                }).start();

                // End the thread for playing dice.
            } // end say numbers if is activated.

        } // end if is not speaking.
    } // end throw actions function.

    // Methods for onResume method, and other shows in application:
    public void fillLastDiceTextView() {
        TextView textView = findViewById(R.id.tvThrownDice);
        textView.setText(UsefulThings.lastDice[0] == null ? getString(R.string.not_thrown_yet) : UsefulThings.lastDice[0]);
    } // end fillLastDiceTextView.

    // Fill also the luck percentage text view:
    public void fillLuckyPercentageTextView() {
        if (UsefulThings.lastDice[0] != null) {
            // Refill also the lucky percentage:
            TextView textView2 = findViewById(R.id.tvLuckyPercentage);
            textView2.setText(getString(R.string.luck_format, UsefulThings.iGeneralAverage));
        } else {
            ((TextView) findViewById(R.id.tvLuckyPercentage)).setText(R.string.lucky_percentage);
        }
    } // end fill lucky percentage text view.

    public void showDiceAsImages() {
        showDiceAsImages(false);
    }

    private void showDiceAsImages(boolean animate) {
        GridLayout grid = findViewById(R.id.diceGrid);
        grid.removeAllViews();
        TextView totalView = findViewById(R.id.tvTotal);
        if (UsefulThings.lastDice[0] == null) {
            totalView.setVisibility(View.GONE);
            return;
        }
        int size = (int) (Math.min(104, (getResources().getDisplayMetrics().widthPixels /
                getResources().getDisplayMetrics().density - 96) / 3) * getResources().getDisplayMetrics().density);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            int landscapeSize = UsefulThings.lastDice[0].split(", ").length > 3 ? 68 : 90;
            size = Math.min(size, (int) (landscapeSize * getResources().getDisplayMetrics().density));
        }
        int total = 0;
        for (String s : UsefulThings.lastDice[0].split(", ")) {
            try {
                int value = Integer.parseInt(s);
                total += value;
                DiceView die = new DiceView(this, value);
                die.setImportantForAccessibility(View.IMPORTANT_FOR_ACCESSIBILITY_NO);
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = size;
                params.height = size;
                grid.addView(die, params);
            } catch (NumberFormatException ignored) {
                // Ignore an invalid historic value.
            }
        }
        totalView.setText(getString(R.string.total_format, total));
        totalView.setVisibility(View.VISIBLE);
        if (animate) {
            grid.setAlpha(0f);
            grid.setScaleX(0.72f);
            grid.setScaleY(0.72f);
            grid.animate()
                    .alpha(1f)
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(280)
                    .setInterpolator(new OvershootInterpolator(1.15f))
                    .start();
            playHapticFeedback();
        }
    } // end show image method.

    private void playHapticFeedback() {
        if (!isHapticFeedback) return;
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator == null || !vibrator.hasVibrator()) return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(35, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            vibrator.vibrate(35);
        }
    }

    private void updateResultAccessibility(boolean announce) {
        View resultCard = findViewById(R.id.resultCard);
        String description;
        if (UsefulThings.lastDice[0] == null) {
            description = getString(R.string.ready_to_roll);
        } else {
            int total = 0;
            for (String value : UsefulThings.lastDice[0].split(", ")) {
                try {
                    total += Integer.parseInt(value);
                } catch (NumberFormatException ignored) {
                    // Keep the readable historic values if one cannot be parsed.
                }
            }
            description = getString(R.string.roll_result_announcement,
                    UsefulThings.lastDice[0], total);
        }
        resultCard.setContentDescription(description);
        if (announce && isTouchExplorationEnabled()) {
            resultCard.announceForAccessibility(description);
        }
    }

    private boolean isTouchExplorationEnabled() {
        AccessibilityManager manager = (AccessibilityManager)
                getSystemService(Context.ACCESSIBILITY_SERVICE);
        return manager != null && manager.isEnabled() && manager.isTouchExplorationEnabled();
    }

    public void decreaseDice(View view) { changeDiceCount(-1); }

    public void increaseDice(View view) { changeDiceCount(1); }

    private void changeDiceCount(int change) {
        iNumberOfDice = Math.max(1, Math.min(6, iNumberOfDice + change));
        new UsefulThings(getApplicationContext()).saveIntSettings("iNumberOfDice", iNumberOfDice);
        updateDiceCount();
    }

    private void updateDiceCount() {
        TextView count = findViewById(R.id.tvDiceCount);
        count.setText(String.valueOf(iNumberOfDice));
        count.setContentDescription(getString(R.string.dice_count_value, iNumberOfDice));
        findViewById(R.id.buttonLess).setEnabled(iNumberOfDice > 1);
        findViewById(R.id.buttonMore).setEnabled(iNumberOfDice < 6);
    }

    private void updateWakeLock() {
        if (isWakeLock) getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        else getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

} // end main activity class.
