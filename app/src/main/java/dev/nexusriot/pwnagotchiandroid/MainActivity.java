package dev.nexusriot.pwnagotchiandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import dev.nexusriot.pwnagotchiandroid.fragments.MemoryFragment;
import dev.nexusriot.pwnagotchiandroid.fragments.SummaryFragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import com.google.android.material.tabs.TabLayout;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //TODO: PoC Code - refactor this
        NTask mt = new NTask();
        mt.execute();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ViewPager viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new SummaryFragment(), "Summary");
        adapter.addFragment(new MemoryFragment(), "Memory");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}

class NTask extends AsyncTask<Void, Void, Void> {

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... params) {
           SSHManager sshManager = new SSHManager(
                   // TODO: use settings to save params
                    "pi", "raspberry", "10.0.0.2",
                   "~/.ssh/known_hosts");
            sshManager.connect();
//            sshManager.sendCommand("ls /");
            // TODO: again, PoC code
            sshManager.setPortForwardingL(8666, "127.0.0.1", 8666);
        try {
            String mesh_data = doGet("http://127.0.0.1:8666/api/v1/mesh/data");
            String memory_data = doGet("http://127.0.0.1:8666/api/v1/mesh/memory");


        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            sshManager.downloadFile("/var/tmp/pwnagotchi/pwnagotchi.png",
                    "/data/data/dev.nexusriot.pwnagotchiandroid/cache/pwnagotchi.png");
        } catch (JSchException e) {
            e.printStackTrace();
        } catch (SftpException e) {
            e.printStackTrace();
        }
        sshManager.close();
        return null;
    }
    public void onPostExecute() {

    }

    public static String doGet(String url)
            // TODO: PoC code testing only, replace with Retofit for example
            throws Exception {

        URL obj = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
        connection.setRequestMethod("GET");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = bufferedReader.readLine()) != null) {
            response.append(inputLine);
        }
        bufferedReader.close();
        Log.d("Ololo","Response string: " + response.toString());
        return response.toString();
    }
}
