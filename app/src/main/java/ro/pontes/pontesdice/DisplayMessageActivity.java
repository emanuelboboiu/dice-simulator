package ro.pontes.pontesdice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class DisplayMessageActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // Get the message from the intent
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        message.length();

        // Set the corresponding XML as the activity layout
        setContentView(R.layout.activity_display_message);

        for (int i = 0; i < UsefulThings.lastDice.length; i++) {
            if (UsefulThings.lastDice[i] != null) {
                String tvString = "TextView" + (i + 1);
                int resID = getResources().getIdentifier(tvString, "id",
                        "ro.pontes.pontesdice");
                TextView textView = (TextView) findViewById(resID);
                textView.setText(UsefulThings.lastDice[i]);
            } else {
                break; // it means there are not more values in the array.
            }
        } // end for.

        // Try a simple message:
        /*
         * String greetingText = "Hello Manu!" Toast tempMessage =
         * Toast.makeText(getApplicationContext(), greetingText,
         * Toast.LENGTH_LONG); tempMessage.show(); // end try a simple message.
         */

    } // end on create option.

} // end display history activity.
