package dev.nexusriot.pwnagotchiandroid;

import androidx.appcompat.app.AppCompatActivity;
import android.os.AsyncTask;
import android.os.Bundle;


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
                    "pi", "raspberry", "10.0.0.2", "~/.ssh/known_hosts");
            sshManager.connect();
            sshManager.sendCommand("ls /");
            sshManager.close();
        return null;
    }

    public void onPostExecute() {
    }
}
