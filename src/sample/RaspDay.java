package sample;


import java.io.Serializable;

public class RaspDay implements Serializable{
    int TweetH = 0;
    boolean Tweet = false;
    int FollowingH = 0;
    boolean Following = false;
    int UnFollowingH = 0;
    boolean UnFollowing = false;
    int RipH = 0;
    boolean Rip = false;

    public RaspDay(){

    }
    //GET
    public boolean isTweet() {
        return Tweet;
    }
    public boolean isFollowing() {
        return Following;
    }
    public boolean isUnFollowing() {
        return UnFollowing;
    }
    public boolean isRip() {
        return Rip;
    }
    public int getTweetH() {
        return TweetH;
    }
    public int getFollowingH() {
        return FollowingH;
    }
    public int getUnFollowingH() {
        return UnFollowingH;
    }
    public int getRipH() {
        return RipH;
    }

    //SET
    public void setTweetH(int tweetH) {
        TweetH = tweetH;
    }
    public void setTweet(boolean tweet) {
        Tweet = tweet;
    }
    public void setFollowingH(int followingH) {
        FollowingH = followingH;
    }
    public void setFollowing(boolean following) {
        Following = following;
    }
    public void setUnFollowingH(int unFollowingH) {
        UnFollowingH = unFollowingH;
    }
    public void setUnFollowing(boolean unFollowing) {
        UnFollowing = unFollowing;
    }
    public void setRipH(int ripH) {
        RipH = ripH;
    }
    public void setRip(boolean rip) {
        Rip = rip;
    }
}
