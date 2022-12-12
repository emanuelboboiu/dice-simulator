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

        UsefulThings ut = new UsefulThings(getApplicationContext()); // we need
        // it
        // for
        // saving
        // with
        // SharedPreferences.
        String key = "currentLanguage";

        // Check which radio button was clicked:
        switch (view.getId()) {
            case R.id.radio_en:
                if (checked) {
                    MainActivity.currentLanguage = "en";
                }
                break;
            case R.id.radio_it:
                if (checked) {
                    MainActivity.currentLanguage = "it";
                }
                break;
            case R.id.radio_ro:
                if (checked) {
                    MainActivity.currentLanguage = "ro";
                }
                break;
        } // } // end switch.

        // Save now the setting:
        ut.saveStringSettings(key, MainActivity.currentLanguage);
    } // end onRadioButtonClicked.

} // end language activity.
