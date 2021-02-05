/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.techmojo.coding.test.toptentweets.main;

import com.techmojo.coding.test.toptentweets.service.TweetProcessingService;
import java.util.Scanner;

/**
 *
 * @author avi
 */
public class TopTenTweetsMain {
    public static void main(String[] args){
        
        Scanner scan = new Scanner(System.in);
        
        System.err.println("Enter the tweet or type quit to Exit:");
        
        String tweet;
        final TweetProcessingService tweetMaintenance = new TweetProcessingService();
        while(true){
            tweet = scan.nextLine();
            if("quit".equalsIgnoreCase(tweet)){
                break;
            }
            tweetMaintenance.captureHashTag(tweet);
        }
        
        tweetMaintenance.displayTopTenTags();

    }
}
