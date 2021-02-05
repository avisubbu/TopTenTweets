/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.techmojo.coding.test.toptentweets.service;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author avi
 */
public class TweetProcessingService {
    
    private final Pattern hashTagPattern = Pattern.compile("((#){1}(\\w)+)+"); 
    
    public void captureHashTag(String tweet){
        
        Matcher matcher = hashTagPattern.matcher(tweet);
        String tag;
        HashTagCount tagCntObj;
        while(matcher.find()){
            tag = matcher.group();
            tagCntObj = DataStore.getCache().get(tag);
            
            if(tagCntObj==null){
                tagCntObj = new HashTagCount(tag);
                tagCntObj = DataStore.getCache().putIfAbsent(tag, tagCntObj);
                if(tagCntObj == null){
                    tagCntObj = DataStore.getCache().get(tag);
                }
            }
            
            tagCntObj.increaseCount();
            
        }
    }
    
    public void displayTopTenTags(){
        if(DataStore.getCache().isEmpty()){
            return;
        }
        TreeSet<HashTagCount> cntSet = new TreeSet<HashTagCount>(
                (HashTagCount tag1, HashTagCount tag2) -> {
                    int compareValue = tag2.getCount().compareTo(tag1.getCount());
                    if(compareValue==0){
                    return tag1.getTag().compareTo(tag2.getTag());
                    }
                    return compareValue;
                });
        
        cntSet.addAll(DataStore.getCache().values());
        
        List<HashTagCount> lst = new ArrayList<>(cntSet);
        
        lst.subList(0, cntSet.size()<10?cntSet.size():10).stream().forEach(System.out::println);
    }
}

class HashTagCount{
    
    private String tag;
    
    private AtomicInteger count;
    
    public HashTagCount(String tag){
        this.tag = tag;
        this.count = new AtomicInteger(0);
    }
    
    public Integer getCount(){
        return this.count.get();
    }
    
    public void increaseCount(){
        this.count.incrementAndGet();
    }
    
    public String getTag(){
        return this.tag;
    }

    @Override
    public String toString(){
        return "Tag: "+this.tag.substring(1) + " appeared "+this.count.toString()+" times";
    }
}
class DataStore{
        private volatile static ConcurrentHashMap<String, HashTagCount> map = new ConcurrentHashMap<>();
        
        public static ConcurrentHashMap<String, HashTagCount> getCache(){
            return DataStore.map;
        }
}