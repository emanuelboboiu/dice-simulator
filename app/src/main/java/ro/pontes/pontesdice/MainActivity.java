package ro.pontes.pontesdice;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Random;

public class MainActivity extends Activity {
    // The following fields are used for the shake detection:
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;
    // End fields declaration for shake detector.

    public int nrOfThrows = 0;
    public final static String EXTRA_MESSAGE = "ro.pontes.pontesdice.MESSAGE";
    public static String message = ""; // here will be the dice as text.
    public static UsefulThings ut;

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

    public static OurSoundPlayer player = new OurSoundPlayer();
    public static Random rand = new Random();
    public static Context c;

    // A boolean variable to know when numbers are spoken:
    public static boolean isSpeaking = false;

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
        View messageView = getLayoutInflater().inflate(R.layout.about_dialog,
                null, false);

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
        String tempString = getString(R.string.thrown_dice);
        TextView textView = (TextView) findViewById(R.id.tvThrownDice);
        textView.setText(tempString);

        tempString = getString(R.string.lucky_percentage);
        TextView textView2 = (TextView) findViewById(R.id.tvLuckyPercentage);
        textView2.setText(tempString);

        // Let's empty the array lastDice from UsefulThings:

        Arrays.fill(UsefulThings.lastDice, null);
        showDiceAsImages(); // to clear the images from the screen.

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
     * Called when the user clicks the number of dice settings option in menu:
     */
    public void goToNumberOfDiceSettings() {
        Intent intent = new Intent(this, NumberOfDiceActivity.class);
        String message;
        message = "Pontes Dice"; // without a reason, just to be something sent
        // by the intent.
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    } // end function which performs when the option number of dice in menu is
    // clicked.

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

        OurSoundPlayer.initSounds(c);

        // To keep screen awake:
        if (MainActivity.isWakeLock) {
            getWindow()
                    .addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        } // end wake lock.

        // // ShakeDetector initialisation
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
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

        fillLastDiceTextView(); // to refill onResume, orientation change or
        // reappear.
        fillLuckyPercentageTextView(); // to refill onResume, orientation change
        // or reappear.
        showDiceAsImages(); // redraw the images at the restart like when
        // orientation is changed.

        // To delete, just a test:
        /*
         * TextView textView = (TextView) findViewById(R.id.tvThrownDice);
         * textView.setText(UsefulThings.curLocale);
         */

        if (isOnShake) {
            // Add the following line to register the Session Manager Listener
            // onResume
            mSensorManager.registerListener(mShakeDetector, mAccelerometer,
                    SensorManager.SENSOR_DELAY_UI);
        }
    } // end onResume method.

    @Override
    public void onPause() {
        // Add here what you want to happens on pause

        if (nrOfThrows > 0) {
            Statistics stats = new Statistics();
            stats.postStats("7", nrOfThrows);
            nrOfThrows = 0;
        }
        if (isOnShake && !isOnShakeInPause) {
            // Add the following line to unregister the Sensor Manager onPause
            mSensorManager.unregisterListener(mShakeDetector);
        }
        super.onPause();
    }

    public void onDestroy() {
        mSensorManager.unregisterListener(mShakeDetector);
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
        } else if (id == R.id.number_of_dice) {
            goToNumberOfDiceSettings();
        } else if (id == R.id.other_settings) {
            goToOtherSettings();
        } else if (id == R.id.default_settings) {
            // Get the strings:
            String tempTitle = getString(R.string.set_defaults_title);
            String tempBody = getString(R.string.set_defaults_body);
            new AlertDialog.Builder(this)
                    .setTitle(tempTitle)
                    .setMessage(tempBody)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes,
                            (dialog, whichButton) -> {
                                UsefulThings tempUT = new UsefulThings(c);
                                tempUT.setDefaultSettings();
                                tempUT.chargeSettings();
                                // To be sure the shake detection will be
                                // again available, as a default setting:
                                mSensorManager.registerListener(
                                        mShakeDetector, mAccelerometer,
                                        SensorManager.SENSOR_DELAY_UI);
                            }).setNegativeButton(android.R.string.no, null)
                    .show();
        } else if (id == R.id.about_program) {
            showAbout();
        } else if (id == R.id.exit_program) {
            this.finish();

        } // end if about item was chosen.
        return super.onOptionsItemSelected(item);
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

            nrOfThrows++;

            int[] aDice = new int[iNumberOfDice];

            // We play the throw dice sound:
            if (isSoundDice) {
                new Thread(() -> OurSoundPlayer.playSound(getApplicationContext(), 1)).start();
                // end thread to play the throwing sound.
            } // end if is activated the sound player.

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
                Arrays.sort(aDice);
            } else if (sortMethod == 2) {
                // Descendant sorting:
                Arrays.sort(aDice);
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
            showDiceAsImages();

            fillLastDiceTextView(); // a method created below in this class. to
            // fill the dedicated text view for dice as
            // text.

            // Calculate and fill the text view with the luck percentage:
            UsefulThings.calculateAverageOfLastHandsOfDice();
            fillLuckyPercentageTextView();

            // Play dice sounds if activated:
            if (isNumberSpoken) {
                // Let's try playing sound in a new thread:

                new Thread(() -> {

                    isSpeaking = true;
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    String[] aTempDice = UsefulThings.lastDice[0]
                            .split(", ");
                    for (int i = 0; i < aTempDice.length; i++) {
                        if (i < aTempDice.length - 1) {
                            OurMediaPlayer.playWait(
                                    getApplicationContext(),
                                    Integer.parseInt(aTempDice[i]));
                        } else {
                            OurMediaPlayer.playWait(
                                    getApplicationContext(),
                                    Integer.parseInt(aTempDice[i]) + 6);
                        }
                    } // end for.
                    isSpeaking = false;

                }).start();

                // End the thread for playing dice.
            } // end say numbers if is activated.

        } // end if is not speaking.
    } // end throw actions function.

    // Methods for onResume method, and other shows in application:
    public void fillLastDiceTextView() {
        // If there is a thrown dice, let's refill the text view:
        if (UsefulThings.lastDice[0] != null) {
            TextView textView = (TextView) findViewById(R.id.tvThrownDice);
            textView.setText(UsefulThings.lastDice[0]);
        }
    } // end fillLastDiceTextView.

    // Fill also the luck percentage text view:
    public void fillLuckyPercentageTextView() {
        if (UsefulThings.lastDice[0] != null) {
            // Refill also the lucky percentage:
            String tempString = getString(R.string.lucky_calculated);
            TextView textView2 = (TextView) findViewById(R.id.tvLuckyPercentage);
            textView2.setText(tempString + UsefulThings.iGeneralAverage + "%");
        }
    } // end fill lucky percentage text view.

    public void showDiceAsImages() {

        // Show the images on the screen in a linear layout llDiceImages:
        // Find the LinearLayout:
        LinearLayout ll = (LinearLayout) findViewById(R.id.llDiceImages);
        // Clear if there is something there:
        if (ll.getChildCount() > 0)
            ll.removeAllViews();

        // Just if there is a last hand of dice in UsefulThings.lastDice[0]:
        if (UsefulThings.lastDice[0] != null) {
            // Create an array with the last hand of dice, split from lastDice
            // static field of the usefulThingsClass.
            String[] aDice;
            aDice = UsefulThings.lastDice[0].split(", ");

            for (String s : aDice) {
                ImageView mImage = new ImageView(c); // c is actual context.
                String uri = "@drawable/d" + s; // the name of the image
                // dynamically.
                int imageResource = getResources().getIdentifier(uri, null,
                        getPackageName());
                @SuppressWarnings("deprecation")
                Drawable res = getResources().getDrawable(imageResource);
                mImage.setImageDrawable(res);
                String tempString = getString(R.string.image);
                mImage.setContentDescription(tempString + " " + s);
                ll.addView(mImage);
            } // end if there is a last hand of dice to show.
        } // end for.

    } // end show image method.

} // end main activity class.
