package com.example;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.util.Log;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.UserService;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created with IntelliJ IDEA.
 * User: Kostya
 * Date: 12.06.12
 * Time: 13:40
 * To change this template use File | Settings | File Templates.
 */
public class TemplateActivity extends Activity {
    protected static Map<String, Bitmap> userpics = new TreeMap<String, Bitmap>();
    protected String TAG = "github-client/Activity";

    protected Bitmap getUserPicture(GitHubClient client, String username) {
        if(userpics.containsKey(username) && userpics.get(username) != null){
            return userpics.get(username);
        } else {
            UserService service = new UserService(client);
            Bitmap icon = null;
            try {
                String avatarUrl = service.getUser(username).getAvatarUrl();
                URL newurl = new URL(avatarUrl);
                icon = BitmapFactory.decodeStream(newurl.openConnection().getInputStream());
            } catch (MalformedURLException e) {
                Log.e(TAG, "Can not parse avatar url for user " + username);
            } catch (IOException e) {
                Log.e(TAG, "Can not get a connection to server");
            }
            userpics.put(username, icon);
            return icon;
        }
    }

    protected GitHubClient createClientFromPreferences() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String login = prefs.getString("savedLogin", "");
        String password = prefs.getString("savedPassword", "");

        GitHubClient client = new GitHubClient();
        client.setCredentials(login, password);

        return client;
    }
}