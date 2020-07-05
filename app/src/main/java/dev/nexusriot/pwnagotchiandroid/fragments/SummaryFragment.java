package dev.nexusriot.pwnagotchiandroid.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import java.io.File;
import dev.nexusriot.pwnagotchiandroid.R;
import androidx.fragment.app.Fragment;


public class SummaryFragment extends Fragment {

    public SummaryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_summary, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        File imgFile = new File(
                "/data/data/dev.nexusriot.pwnagotchiandroid/cache/pwnagotchi.png");

        if (imgFile.exists()) {
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            int nh = (int) ( myBitmap.getHeight() * (640.0 / myBitmap.getWidth()) );
            Bitmap scaled = Bitmap.createScaledBitmap(myBitmap, 640, nh, true);
            ImageView myImage = getActivity().findViewById(R.id.image);
            if (myImage != null) {
                myImage.setImageBitmap(scaled);
            }
        }
    }
}
