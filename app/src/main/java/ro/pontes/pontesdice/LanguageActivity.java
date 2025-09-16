package ro.pontes.pontesdice;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

public class LanguageActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);

        // Check the radio button depending of language for dice voice chosen:
        String rb = "radio_" + MainActivity.currentLanguage;
        int resID = getResources().getIdentifier(rb, "id",
                "ro.pontes.pontesdice");
        RadioButton radioButton = (RadioButton) findViewById(resID);
        radioButton.setChecked(true);

    } // end onCreate method.

    // See and save the radio button clicked:
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        UsefulThings ut = new UsefulThings(getApplicationContext());
        String key = "currentLanguage";

        int id = view.getId();

        if (id == R.id.radio_en && checked) {
            MainActivity.currentLanguage = "en";
        } else if (id == R.id.radio_it && checked) {
            MainActivity.currentLanguage = "it";
        } else if (id == R.id.radio_ro && checked) {
            MainActivity.currentLanguage = "ro";
        }

        // Save the setting:
        ut.saveStringSettings(key, MainActivity.currentLanguage);
    } // end onRadioButtonClicked.

} // end language activity.
