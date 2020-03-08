package ca.bcit.sunflower;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;


public class CalendarFragment extends Fragment {
    private Context context;
    static int[] sunny = {R.drawable.one_sunny, R.drawable.two_sunny, R.drawable.all_sunny};
    static int[] rosy = {R.drawable.one_rose, R.drawable.two_rose, R.drawable.all_rose};


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle bundle = this.getArguments();
        View inf = inflater.inflate(R.layout.calendar_fragment, container, false);
        String date = bundle.getString("date");
        int mood = bundle.getInt("mood");
        int flow = bundle.getInt("flow");
        TextView today = inf.findViewById(R.id.date_view);
        ImageView sunflowers = inf.findViewById(R.id.sunny_view);
        Drawable res = getResources().getDrawable(sunny[mood]);
        sunflowers.setImageDrawable(res);
        ImageView roses = inf.findViewById(R.id.rose_view);
        Drawable resrose = getResources().getDrawable(rosy[flow]);
        roses.setImageDrawable(resrose);
        today.setText(date);




        return inf;

    }
}
