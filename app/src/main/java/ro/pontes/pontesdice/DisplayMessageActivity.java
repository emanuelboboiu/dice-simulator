package ro.pontes.pontesdice;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DisplayMessageActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);
        LinearLayout list = findViewById(R.id.historyList);
        if (UsefulThings.lastDice == null || UsefulThings.lastDice[0] == null) {
            TextView empty = new TextView(this);
            empty.setText(R.string.not_thrown_yet);
            empty.setTextColor(getResources().getColor(R.color.text_secondary));
            empty.setTextSize(18);
            list.addView(empty);
            return;
        }
        for (int i = 0; i < UsefulThings.lastDice.length; i++) {
            String roll = UsefulThings.lastDice[i];
            if (roll == null) break;
            LinearLayout row = new LinearLayout(this);
            row.setGravity(Gravity.CENTER_VERTICAL);
            row.setPadding(dp(20), dp(16), dp(20), dp(16));
            row.setBackgroundResource(R.drawable.card_background);
            row.setContentDescription(getString(R.string.roll_number, i + 1, roll));
            row.setImportantForAccessibility(LinearLayout.IMPORTANT_FOR_ACCESSIBILITY_YES);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                row.setScreenReaderFocusable(true);
            }
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.bottomMargin = dp(12);
            list.addView(row, params);

            TextView index = new TextView(this);
            index.setText(String.valueOf(i + 1));
            index.setTextColor(getResources().getColor(R.color.text_secondary));
            index.setTextSize(16);
            index.setImportantForAccessibility(TextView.IMPORTANT_FOR_ACCESSIBILITY_NO);
            row.addView(index, new LinearLayout.LayoutParams(dp(36), LinearLayout.LayoutParams.WRAP_CONTENT));

            TextView result = new TextView(this);
            result.setText(roll);
            result.setTextColor(getResources().getColor(R.color.text_primary));
            result.setTextSize(21);
            result.setGravity(Gravity.END);
            result.setImportantForAccessibility(TextView.IMPORTANT_FOR_ACCESSIBILITY_NO);
            row.addView(result, new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        }
    }

    private int dp(int value) {
        return (int) (value * getResources().getDisplayMetrics().density + 0.5f);
    }
}
