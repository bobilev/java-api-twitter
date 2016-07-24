package sample;

import javafx.application.Platform;
import javafx.scene.control.Label;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.util.Set;
import java.util.regex.Pattern;

import static java.lang.Thread.sleep;

public class ProxyListPotok implements Runnable {
    String oauthtoken;
    String oauthtokensecret;
    String setOAuthConsumerKey;
    String setOAuthConsumerSecret;
    String proxy;
    static int x = 0;
    static int y = 0;
    int z;
    Set<String> ListProxy;
    Label labCountProxy;

    public ProxyListPotok(String oauthtoken,
                          String oauthtokensecret,
                          String setOAuthConsumerKey,
                          String setOAuthConsumerSecret,
                          String proxy,
                          Set<String> ListProxy,
                          int z,
                          final Label labCountProxy){
        this.oauthtoken = oauthtoken;
        this.oauthtokensecret = oauthtokensecret;
        this.setOAuthConsumerKey = setOAuthConsumerKey;
        this.setOAuthConsumerSecret = setOAuthConsumerSecret;
        this.proxy = proxy;
        this.ListProxy = ListProxy;
        this.z = z;
        this.labCountProxy = labCountProxy;
    }

    public Twitter ConfigTwitter(String setOAuthConsumerKey, String setOAuthConsumerSecret, String oauthtoken, String oauthtokensecret, String proxy){
        Pattern pt = Pattern.compile(":");
        String[] proxyPort = pt.split(proxy);

        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(setOAuthConsumerKey)
                .setOAuthConsumerSecret(setOAuthConsumerSecret)
                .setOAuthAccessToken(oauthtoken)
                .setOAuthAccessTokenSecret(oauthtokensecret)
                .setHttpProxyHost(proxyPort[0])
                .setHttpProxyPort(Integer.parseInt(proxyPort[1]));
        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();
        return twitter;
    }
    @Override
    public void run() {
        Twitter twitter = ConfigTwitter(setOAuthConsumerKey,setOAuthConsumerSecret,oauthtoken,oauthtokensecret,proxy);
        for(int i=0;i<10;i++){
            try {
                Query query = new Query("Привет");
                QueryResult result = twitter.search(query.count(1).lang("ru"));
                x++;
                //System.out.println(z+")Работает ---------------------- "+x);
                ListProxy.add(proxy);
                final int count = ListProxy.size();
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        labCountProxy.setText("Proxy: " + count);
                    }
                });
                break;
            } catch (TwitterException e) {
                try {
                    sleep(4000);
                } catch (InterruptedException e1) {e1.printStackTrace();}
                if(i==9){
                    y++;
                    //System.out.println("Не рабочих "+y);
                    break;
                }
            }
            catch (java.util.NoSuchElementException e) {
                if (i == 9) {
                    y++;
                    break;
                }
            }
        }
    }
}
