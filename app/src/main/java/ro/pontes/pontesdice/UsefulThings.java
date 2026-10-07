package ro.pontes.pontesdice;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.Arrays;
import java.util.Locale;

public class UsefulThings {

    // The file for save and load preferences:
    public final static String PREFS_NAME = "pdSettings";
    public static boolean isNotFirstRunning = false;
    public static String[] lastDice;
    public static int iGeneralAverage = 0;
    public static String curLocale = "";

    private final Context contextHere;
    private static SharedPreferences historyPreferences;

    public UsefulThings(Context context) {
        contextHere = context.getApplicationContext();
        historyPreferences = contextHere.getSharedPreferences(PREFS_NAME, 0);
    }

    public static void initialiseThings() {

        // Resize the array for last dice:
        if (lastDice == null) {
            lastDice = new String[MainActivity.numberOfDiceInHistory];
            for (int i = 0; i < lastDice.length; i++) {
                lastDice[i] = historyPreferences.getString("history_" + i, null);
            }
            calculateAverageOfLastHandsOfDice();
        }

    } // end initialise things function.

    public static void addLastDice(String message) {
        // This is a push method for the array:
        for (int i = lastDice.length - 1; i > 0; i--) {
            if (lastDice[i - 1] != null) {
                lastDice[i] = lastDice[i - 1];
            }
        } // end for.
        // Add at index 0 last message, last dice thrown:
        lastDice[0] = message;
        saveHistory();
    } // end add last dice into history..

    public static void clearHistory() {
        Arrays.fill(lastDice, null);
        iGeneralAverage = 0;
        saveHistory();
    }

    private static void saveHistory() {
        SharedPreferences.Editor editor = historyPreferences.edit();
        for (int i = 0; i < lastDice.length; i++) {
            if (lastDice[i] == null) editor.remove("history_" + i);
            else editor.putString("history_" + i, lastDice[i]);
        }
        editor.apply();
    }

    // A function to calculate and show the lucky:
    public static void calculateAverageOfLastHandsOfDice() {
        // An array for the last dice hands average:
        double[] aiLastAverages = new double[MainActivity.numberOfDiceInHistory];

        // Insert the last averages into aiLastAverages:
        for (int i = 0; i < lastDice.length; i++) {
            if (lastDice[i] != null) {
                String[] theDiceOfAHand; // an array which holds the number of a
                // hand separately.
                theDiceOfAHand = lastDice[i].split(", ");
                // Add the values of the string array above as digits and make
                // the average:
                double curDiceHandAverage = 0.0;
                for (String s : theDiceOfAHand) {
                    curDiceHandAverage += Integer.parseInt(s);
                } // end nested loop.
                curDiceHandAverage = curDiceHandAverage / theDiceOfAHand.length;
                // Insert the average into aiLastAverages:
                aiLastAverages[i] = curDiceHandAverage;
            } else {
                break;
            }
        } // end for.

        // Make the general average:
        double generalAverage = 0.0;
        int it = 0; // counts how many hands were thrown:
        for (int i = 0; i < aiLastAverages.length; i++) {
            if (lastDice[i] != null) {
                generalAverage += aiLastAverages[i];
                it++;
            } else {
                break;
            }
        } // end for.

        // Final result:
        if (it == 0) {
            iGeneralAverage = 0;
            return;
        }
        generalAverage = generalAverage / it;

        // Make it as percentage:
        generalAverage = generalAverage - 1;
        generalAverage = generalAverage * 100 / 5;

        // Ceiling it to the closest integer:
        generalAverage = Math.ceil(generalAverage);

        iGeneralAverage = (int) generalAverage;

    }

    // Methods for save and read preferences with SharedPreferences:

    // Save a boolean value:
    public void saveBooleanSettings(String key, boolean value) {
        // We need an Editor object to make preference changes.
        // All objects are from android.context.Context
        SharedPreferences settings = contextHere.getSharedPreferences(
                PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(key, value);
        // Commit the edits!
        editor.apply();
    } // end save boolean.

    // Read boolean preference:
    public boolean getBooleanSettings(String key) {
        boolean value;
        // Restore preferences
        SharedPreferences settings = contextHere.getSharedPreferences(
                PREFS_NAME, 0);
        value = settings.getBoolean(key, false);

        return value;
    } // end get boolean preference from SharedPreference.

    public boolean getBooleanSettings(String key, boolean defaultValue) {
        SharedPreferences settings = contextHere.getSharedPreferences(PREFS_NAME, 0);
        return settings.getBoolean(key, defaultValue);
    }

    // Save a integer value:
    public void saveIntSettings(String key, int value) {
        // We need an Editor object to make preference changes.
        // All objects are from android.context.Context
        SharedPreferences settings = contextHere.getSharedPreferences(
                PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(key, value);
        // Commit the edits!
        editor.apply();
    } // end save integer.

    // Read integer preference:
    public int getIntSettings(String key) {
        int value;
        // Restore preferences
        SharedPreferences settings = contextHere.getSharedPreferences(
                PREFS_NAME, 0);
        value = settings.getInt(key, 0);

        return value;
    } // end get integer preference from SharedPreference.

    // Save a String value:
    public void saveStringSettings(String key, String value) {
        // We need an Editor object to make preference changes.
        // All objects are from android.context.Context
        SharedPreferences settings = contextHere.getSharedPreferences(
                PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        // Commit the edits!
        editor.apply();
    } // end save String.

    // Read String preference:
    public String getStringSettings(String key) {
        String value;
        // Restore preferences
        SharedPreferences settings = contextHere.getSharedPreferences(
                PREFS_NAME, 0);
        value = settings.getString(key, null);

        return value;
    } // end get String preference from SharedPreference.

    // End read and write settings in SharedPreferences.

    // Charge Settings function:
    public void chargeSettings() {

        // Determine if is first launch of the program:
        isNotFirstRunning = getBooleanSettings("isFirstRunning");
        if (!isNotFirstRunning) {
            saveBooleanSettings("isFirstRunning", true);
            // Make default values in SharedPrefferences:
            setDefaultSettings();
        }

        // Play or not the dice thrown sound:
        MainActivity.isSoundDice = getBooleanSettings("isSoundDice");

        // Play or not the numbers as human voice:
        MainActivity.isNumberSpoken = getBooleanSettings("isNumberSpoken");

        // 0 uses the bundled recordings, 1 uses Android Text to Speech.
        MainActivity.voiceMode = getIntSettings("voiceMode") == 1 ? 1 : 0;

        // Charge the number of dice:
        MainActivity.iNumberOfDice = getIntSettings("iNumberOfDice");
        // Check if there is a value for iNumberOfDice already saved, otherwise
        // iNumberOfDice will be 2:
        if (MainActivity.iNumberOfDice >= 1 && MainActivity.iNumberOfDice <= 6) {
            // Everything is OK...
        } else {
            MainActivity.iNumberOfDice = 2;
        }

        // Get the shake status:
        MainActivity.isOnShake = getBooleanSettings("isOnShake");

        // Get the setting for shake on pause:
        MainActivity.isOnShakeInPause = getBooleanSettings("isOnShakeInPause");

        // For keeping screen awake:
        MainActivity.isWakeLock = getBooleanSettings("isWakeLock");

        // Short vibration when a new result is generated:
        MainActivity.isHapticFeedback = getBooleanSettings("isHapticFeedback", true);

        // Announce every die value as a vibration pattern:
        MainActivity.isHapticResult = getBooleanSettings("isHapticResult", false);

        // Sorting method:
        MainActivity.sortMethod = getIntSettings("sortMethod");

        // Get the language for currentLanguage key in SharedPrefferences:
        String tempCurLocale = getStringSettings("currentLanguage");
        if ("en".equals(tempCurLocale) || "it".equals(tempCurLocale)
                || tempCurLocale.equals("ro")) {
            // If there is a saved value which exists also in the raw folder:
            MainActivity.currentLanguage = tempCurLocale;
        } else {
            // If is another language that one saved in settings, or another one
            // which also doesn't exists, English is the default:
            MainActivity.currentLanguage = "en";
        }
    } // end charge settings.

    public void setDefaultSettings() {

        // Numbers peaking language:
        // Get the system current locale: // Get the locale:
        curLocale = contextHere.getResources().getConfiguration().locale.getLanguage().toLowerCase(Locale.ROOT);
        if (curLocale.equals("en") || curLocale.equals("it")
                || curLocale.equals("ro")) {
            saveStringSettings("currentLanguage", curLocale);
        } else {
            saveStringSettings("currentLanguage", "en");
        }
        // End save default number speaking language.

        // Default number of dice:
        saveIntSettings("iNumberOfDice", 2);

        // // Activate sounds for dice and number speaking:
        saveBooleanSettings("isSoundDice", true);
        saveBooleanSettings("isNumberSpoken", true);
        saveIntSettings("voiceMode", 0);

        // For shake:
        saveBooleanSettings("isOnShake", true);

        // For shake if screen is blocked:
        saveBooleanSettings("isOnShakeInPause", false);

        // For keeping screen awake:
        saveBooleanSettings("isWakeLock", true);

        // Short vibration when rolling:
        saveBooleanSettings("isHapticFeedback", true);

        // Haptic result playback is optional and disabled by default:
        saveBooleanSettings("isHapticResult", false);

        // For sorting method:
        saveIntSettings("sortMethod", 2);

    } // end setDefaultSettings function.

} // end UsefulThings class.
