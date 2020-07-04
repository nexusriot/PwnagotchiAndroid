package dev.nexusriot.pwnagotchiandroid;

import androidx.appcompat.app.AppCompatActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //TODO: PoC Code - refactor this
        NTask mt = new NTask();
        mt.execute();
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
                    "pi", "raspberry", "10.0.0.2", "~/.ssh/known_hosts");
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
//        connection.setRequestProperty("User-Agent", "Mozilla/5.0" );
//        connection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
//        connection.setRequestProperty("Content-Type", "application/json");
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
