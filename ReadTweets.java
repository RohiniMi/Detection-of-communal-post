package com;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
public class ReadTweets{
	static ArrayList<String> features = new ArrayList<String>();
	static LinkedHashMap<String,String> classifier = new LinkedHashMap<String,String>();
	
	static ArrayList<String> tweets = new ArrayList<String>();
	static ArrayList<String> urls = new ArrayList<String>();

public static void readFeatures(String path){
	try{
		features.clear();
		BufferedReader br = new BufferedReader(new FileReader(path));
		String line = null;
		while((line = br.readLine()) != null){
			line = line.trim();
			if(line.length() > 0){
				features.add(line.toLowerCase());
			}
		}
		br.close();
	}catch(Exception e){
		e.printStackTrace();
	}
}
public static void addTweets(String tweet){
	String arr[] = tweet.split("\\s+");
	for(int i=0;i<arr.length;i++){
		String text = arr[i].trim();
		if(text.startsWith("http:") && !text.endsWith(".")){
			tweets.add(tweet);
			urls.add(text);
			break;
		}
	}
}
public static void readDataset(String path){
	try{
		tweets.clear();
		urls.clear();
		ViewDataset vd = new ViewDataset();
		BufferedReader br = new BufferedReader(new FileReader(path));
		String line = null;
		while((line = br.readLine()) != null){
			line = line.trim();
			addTweets(line);
			if(line.length() > 0){
				int count = checkCommunal(line);
				if(count > 1){
					classifier.put(line,"Communal");
				} else {
					classifier.put(line,"NonCommunal");
				}
			}
			Object row[] = {line};
			vd.dtm.addRow(row);
		}
		br.close();
		vd.setVisible(true);
		vd.setSize(600,600);
	}catch(Exception e){
		e.printStackTrace();
	}
}
public static int checkCommunal(String line){
	String arr[] = line.toLowerCase().split("\\s+");
	int count = 0;
	for(int i=0;i<features.size();i++){
		String f[] = features.get(i).split("\\s+");
		if(f.length == 1){
			for(int j=0;j<arr.length;j++){
				if(f[0].equals(arr[j])){
					count++;
				}
			}
		} else if(f.length == 2){
			for(int j=0;j<arr.length-1;j++){
				if(f[0].equals(arr[j]) && f[1].equals(arr[j+1])){
					count++;
				}
			}
		}
	}
	return count;
}
}