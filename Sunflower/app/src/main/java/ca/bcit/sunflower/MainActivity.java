package ca.bcit.sunflower;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.Toast;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.exceptions.OutOfDateRangeException;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.amazonaws.amplify.generated.graphql.CreateUserMutation;
import com.amazonaws.amplify.generated.graphql.ListUsersQuery;
import com.amazonaws.amplify.generated.graphql.UpdateUserMutation;
import com.amazonaws.mobileconnectors.appsync.fetcher.AppSyncResponseFetchers;
import com.apollographql.apollo.GraphQLCall;
import com.apollographql.apollo.api.Mutation;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import ca.bcit.sunflower.parser.NdefMessageParser;
import ca.bcit.sunflower.record.ParsedNdefRecord;
import type.CreateUserInput;
import type.UpdateUserInput;

import java.util.ArrayList;

import javax.annotation.Nonnull;

public class MainActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    MyAdapter mAdapter;
    //NFC
    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;
    private TextView text;
    private String userId;

    //calendar
    CalendarView cv;
    List<EventDay> events;
    List<Calendar_Contents> calendar_contents;

    private ArrayList<ListUsersQuery.Item> mUsers;
    private final String TAG = MainActivity.class.getSimpleName();

    public void replaceFragment(Fragment someFragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, someFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.recycler_view);

        // use a linear layout manager
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // specify an adapter (see also next example)
        mAdapter = new MyAdapter(this);

//        mRecyclerView.setAdapter(mAdapter);

        ClientFactory.init(this);

        //NFC
        text = (TextView) findViewById(R.id.text);
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (nfcAdapter == null) {
            Toast.makeText(this, "No NFC", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        pendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, this.getClass())
                        .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);


        //calendar

        Calendar calendar = Calendar.getInstance();

        cv = findViewById(R.id.calendarView);
        try {
            cv.setDate(calendar);
        } catch (OutOfDateRangeException e) {
            e.printStackTrace();
        }
        events = new ArrayList<>();
        events.add(new EventDay(calendar, R.drawable.rose));
        cv.setEvents(events);

        cv.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay) {
                Calendar clickedDayCalendar = eventDay.getCalendar();
                Calendar_Contents content = new Calendar_Contents(clickedDayCalendar);
                Bundle bundle = new Bundle();
                bundle.putString("date", content.getDate());
                bundle.putInt("mood", content.getMood());
                bundle.putInt("flow", content.getFlow());
                events.add(new EventDay(clickedDayCalendar, R.drawable.sunflower));
                cv.setEvents(events);
                Toast.makeText(MainActivity.this, content.getDate(), Toast.LENGTH_LONG).show();
                Fragment fragment = new CalendarFragment();
                fragment.setArguments(bundle);
                replaceFragment(fragment);
            }
        });



    }

    @Override
    public void onResume() {
        super.onResume();

        // Query list data when we return to the screen
        query();
        update();

        if (nfcAdapter != null) {
            if (!nfcAdapter.isEnabled())
                showWirelessSettings();

            nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
        }
    }
//todo
    public void update() {
        //id, fname, lname, lastlogin, startdate, enddate, misseddays, responsetype
//        UpdateUserInput userInput = UpdateUserInput.builder().id("200").fname("uwu").lname("owo").lastlogin("date1").startdate("date2").enddate("date3").
//        UpdateUserMutation updateUserMutation = UpdateUserMutation.builder().input(userInput).build();
        //update user mutation
//        ClientFactory.appSyncClient().mutate(updateUserMutation);
    }

    //frankenstein
    private void save() {

        UpdateUserInput input = UpdateUserInput.builder()
                .id("2")
                .fname("Stella")
                .lname("Tran")
                .lastlogin("03-12-20")
                .startdate("03-10-20")
                .enddate("03-19-20")
                .misseddates(0)
                .responsetype("urgent")
                .build();

        UpdateUserMutation addPetMutation = UpdateUserMutation.builder()
                .input(input)
                .build();
        ClientFactory.appSyncClient().mutate(addPetMutation).enqueue(mutateCallback);
    }

    // Mutation callback code
    private GraphQLCall.Callback<UpdateUserMutation.Data> mutateCallback = new GraphQLCall.Callback<UpdateUserMutation.Data>() {
        @Override
        public void onResponse(@Nonnull final Response<UpdateUserMutation.Data> response) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    Toast.makeText(MainActivity.this, "Updated user", Toast.LENGTH_SHORT).show();
//                    MainActivity.this.finish();
                }
            });
        }

        @Override
        public void onFailure(@Nonnull final ApolloException e) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e("", "Failed to perform AddPetMutation", e);
                    Toast.makeText(MainActivity.this, "Failed to update user", Toast.LENGTH_SHORT).show();
                    MainActivity.this.finish();
                }
            });
        }
    };
    //frankenstein
    public void query(){
        ClientFactory.appSyncClient().query(ListUsersQuery.builder().build())
                .responseFetcher(AppSyncResponseFetchers.CACHE_AND_NETWORK)
                .enqueue(queryCallback);


    }

    private GraphQLCall.Callback<ListUsersQuery.Data> queryCallback = new GraphQLCall.Callback<ListUsersQuery.Data>() {
        @Override
        public void onResponse(@Nonnull Response<ListUsersQuery.Data> response) {

            mUsers = new ArrayList<>(response.data().listUsers().items());
            final ArrayList<ListUsersQuery.Item> mSelectedUser = new ArrayList<>();
//            mSelectedUser.add(mUsers.get(0));
            for (ListUsersQuery.Item item: mUsers) {
                if (item.id().equals(userId)) {
                    mSelectedUser.add(item);
                }
            }


            Log.i(TAG, "Retrieved list items: " + mUsers.toString());
            Log.i(TAG, "Retrieved SUNFLOWER: " + mSelectedUser.toString());
            Log.i(TAG, "USER" + mSelectedUser.toString());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAdapter.setItems(mSelectedUser);
                    mAdapter.notifyDataSetChanged();
                }
            });
        }

        @Override
        public void onFailure(@Nonnull ApolloException e) {
            Log.e(TAG, e.toString());
        }
    };

    private String toHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = bytes.length - 1; i >= 0; --i) {
            int b = bytes[i] & 0xff;
            if (b < 0x10)
                sb.append('0');
            sb.append(Integer.toHexString(b));
            if (i > 0) {
                sb.append(" ");
            }
        }
        return sb.toString();
    }

    private String toReversedHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; ++i) {
            if (i > 0) {
                sb.append(" ");
            }
            int b = bytes[i] & 0xff;
            if (b < 0x10)
                sb.append('0');
            sb.append(Integer.toHexString(b));
        }
        return sb.toString();
    }

    private long toDec(byte[] bytes) {
        long result = 0;
        long factor = 1;
        for (int i = 0; i < bytes.length; ++i) {
            long value = bytes[i] & 0xffl;
            result += value * factor;
            factor *= 256l;
        }
        return result;
    }

    private long toReversedDec(byte[] bytes) {
        long result = 0;
        long factor = 1;
        for (int i = bytes.length - 1; i >= 0; --i) {
            long value = bytes[i] & 0xffl;
            result += value * factor;
            factor *= 256l;
        }
        return result;
    }

    private String dumpTagData(Tag tag) {
        StringBuilder sb = new StringBuilder();
        byte[] id = tag.getId();
        sb.append("ID (hex): ").append(toHex(id)).append('\n');
        sb.append("ID (reversed hex): ").append(toReversedHex(id)).append('\n');
        sb.append("ID (dec): ").append(toDec(id)).append('\n');
        sb.append("ID (reversed dec): ").append(toReversedDec(id)).append('\n');

        String prefix = "android.nfc.tech.";
        sb.append("Technologies: ");
        for (String tech : tag.getTechList()) {
            sb.append(tech.substring(prefix.length()));
            sb.append(", ");
        }

        sb.delete(sb.length() - 2, sb.length());

        for (String tech : tag.getTechList()) {
            if (tech.equals(MifareClassic.class.getName())) {
                sb.append('\n');
                String type = "Unknown";

                try {
                    MifareClassic mifareTag = MifareClassic.get(tag);

                    switch (mifareTag.getType()) {
                        case MifareClassic.TYPE_CLASSIC:
                            type = "Classic";
                            break;
                        case MifareClassic.TYPE_PLUS:
                            type = "Plus";
                            break;
                        case MifareClassic.TYPE_PRO:
                            type = "Pro";
                            break;
                    }
                    sb.append("Mifare Classic type: ");
                    sb.append(type);
                    sb.append('\n');

                    sb.append("Mifare size: ");
                    sb.append(mifareTag.getSize() + " bytes");
                    sb.append('\n');

                    sb.append("Mifare sectors: ");
                    sb.append(mifareTag.getSectorCount());
                    sb.append('\n');

                    sb.append("Mifare blocks: ");
                    sb.append(mifareTag.getBlockCount());
                } catch (Exception e) {
                    sb.append("Mifare classic error: " + e.getMessage());
                }
            }

            if (tech.equals(MifareUltralight.class.getName())) {
                sb.append('\n');
                MifareUltralight mifareUlTag = MifareUltralight.get(tag);
                String type = "Unknown";
                switch (mifareUlTag.getType()) {
                    case MifareUltralight.TYPE_ULTRALIGHT:
                        type = "Ultralight";
                        break;
                    case MifareUltralight.TYPE_ULTRALIGHT_C:
                        type = "Ultralight C";
                        break;
                }
                sb.append("Mifare Ultralight type: ");
                sb.append(type);
            }
        }

        return sb.toString();
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        if (nfcAdapter != null) {
//            if (!nfcAdapter.isEnabled())
//                showWirelessSettings();
//
//            nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
//        }
//    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        resolveIntent(intent);
    }

    private void resolveIntent(Intent intent) {
        String action = intent.getAction();

        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage[] msgs;

            if (rawMsgs != null) {
                msgs = new NdefMessage[rawMsgs.length];

                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                }

            } else {
                byte[] empty = new byte[0];
                byte[] id = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
                Tag tag = (Tag) intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                byte[] payload = dumpTagData(tag).getBytes();
                NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN, empty, id, payload);
                NdefMessage msg = new NdefMessage(new NdefRecord[] {record});
                msgs = new NdefMessage[] {msg};
            }

            userId = displayMsgs(msgs);
        }
    }

    private void showWirelessSettings() {
        Toast.makeText(this, "You need to enable NFC", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
        startActivity(intent);
    }

    private String displayMsgs(NdefMessage[] msgs) {
        if (msgs == null || msgs.length == 0)
            return "";

        StringBuilder builder = new StringBuilder();
        List<ParsedNdefRecord> records = NdefMessageParser.parse(msgs[0]);
        final int size = records.size();

        for (int i = 0; i < size; i++) {
            ParsedNdefRecord record = records.get(i);
            String str = record.str();
            builder.append(str).append("\n");
        }

//        text.setText(builder.toString());
        Toast.makeText(MainActivity.this, "Mood updated.", Toast.LENGTH_LONG).show();
        save();
        return builder.toString();
    }
}