package sample.Search;

import twitter4j.*;
import twitter4j.auth.AccessToken;

public class Search {
    Twitter twitter;

    //Twitter twitter = TwitterFactory.getSingleton();
    public Search(Twitter twitter) {
        this.twitter = twitter;
    }
    public QueryResult searchTwit(String slovo, int num) throws TwitterException {
        Query query = new Query(slovo);
        QueryResult result = twitter.search(query.count(num).lang("ru"));
        return result;
    }
        /*Query query = new Query("like");
        QueryResult result = twitter.search(query.count(40).lang("ru"));
        for (
                Status status : result.getTweets()) {
            System.out.println("@" + status.getUser().getScreenName() + "("+ status.getUser().getId()+")" + ":" + status.getText());
        }
    }*/
}
