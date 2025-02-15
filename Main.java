package com;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.UIManager;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JOptionPane;
import com.jd.swing.util.Theme;
import com.jd.swing.util.PanelType;
import com.jd.swing.custom.component.panel.StandardPanel;
import javax.swing.JFileChooser;
import java.io.File;
import java.awt.Cursor;
import java.util.Map;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import org.jfree.ui.RefineryUtilities;
import java.util.LinkedHashMap;
import java.util.Map;
public class Main extends JFrame{
	StandardPanel p1;
	JPanel p2;
	JLabel l1,l2,title;
	JButton b1,b2,b3,b4,b5,b6,b7;
	Font f1;
	JFileChooser chooser;
	File file;
	static int communal,non;
	LinkedHashMap<String,Integer> malicious = new LinkedHashMap<String,Integer>(); 
	static int malicious_count;
public Main(){
	super("Communal Tweets Detection hi");
	p1 = new StandardPanel(Theme.STANDARD_GREEN_THEME,PanelType.PANEL_ROUNDED);
	p1.setLayout(null);
	
	p2 = new TitlePanel(600,30);
	p2.setBackground(new Color(204, 110, 155));
	title = new JLabel("<HTML><BODY><CENTER>An Event Independent Classifier that Filters out Communal Tweets</BODY></HTML>");
	title.setFont(new Font("Courier New",Font.BOLD,16));
	p2.add(title);

	title = new JLabel("Modules Information");
	title.setFont(new Font("Times New Roman",Font.BOLD,16));
	title.setBounds(370,70,200,30);
	p1.add(title);

	chooser = new JFileChooser(new File("dataset"));

	f1 = new Font("Times New Roman",Font.BOLD,14);
	b1 = new JButton("Upload Tweet Train Dataset");
	b1.setBounds(280,120,300,30);
	b1.setFont(f1);
	p1.add(b1);
	b1.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent ae){
			int option = chooser.showOpenDialog(Main.this);
			if(option == JFileChooser.APPROVE_OPTION){
				file = chooser.getSelectedFile();
				ReadTweets.readFeatures("features.txt");
				JOptionPane.showMessageDialog(Main.this,"Dataset Loaded");
			}
		}
	});
	
	b2 = new JButton("Read Tweets");
	b2.setFont(f1);
	b2.setBounds(280,170,300,30);
	p1.add(b2);
	b2.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent ae){
			Cursor hourglassCursor = new Cursor(Cursor.WAIT_CURSOR);
			setCursor(hourglassCursor);
			ReadTweets.readDataset(file.getPath());
			Cursor normalCursor = new Cursor(Cursor.DEFAULT_CURSOR);
			setCursor(normalCursor);
		}
	});
	
	b3 = new JButton("Build Classifier");
	b3.setFont(f1);
	b3.setBounds(280,220,300,30);
	p1.add(b3);
	b3.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent ae){
			Cursor hourglassCursor = new Cursor(Cursor.WAIT_CURSOR);
			setCursor(hourglassCursor);
			Classifier.buildClassifier();
			Cursor normalCursor = new Cursor(Cursor.DEFAULT_CURSOR);
			setCursor(normalCursor);
			ViewClassifier vc = new ViewClassifier();
			for(Map.Entry<String,String> me : ReadTweets.classifier.entrySet()){
				String tweet = me.getKey();
				String classify = me.getValue();
				Object row[] = {tweet,classify};
				vc.dtm.addRow(row);
			}
			vc.setVisible(true);
			vc.setSize(800,600);
		}
	});

	b4 = new JButton("Upload Test Tweets Dataset");
	b4.setFont(f1);
	b4.setBounds(280,270,300,30);
	p1.add(b4);
	b4.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent ae){
			int option = chooser.showOpenDialog(Main.this);
			if(option == JFileChooser.APPROVE_OPTION){
				file = chooser.getSelectedFile();
				classifyTest();
			}
		}
	});

	b5 = new JButton("Communal & Noncommunal Tweets Graph");
	b5.setFont(f1);
	b5.setBounds(280,320,300,30);
	p1.add(b5);
	b5.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent ae){
			communal = 0;
			non = 0;
			for(Map.Entry<String,String> me : ReadTweets.classifier.entrySet()){
				String tweet = me.getKey();
				String classify = me.getValue();
				if(classify.equals("Communal"))
					communal = communal + 1;
				else
					non = non + 1;
			}
			Chart chart1 = new Chart("Communal & Noncommunal Tweets Graph");
			chart1.pack();
			RefineryUtilities.centerFrameOnScreen(chart1);
			chart1.setVisible(true);
		}
	});

	b6 = new JButton("Malicious URL Detection");
	b6.setFont(f1);
	b6.setBounds(280,370,300,30);
	p1.add(b6);
	b6.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent ae){
			detection();
		}
	});
	
	b7 = new JButton("Normal & Malicious URL Chart");
	b7.setFont(f1);
	b7.setBounds(280,420,300,30);
	p1.add(b7);
	b7.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent ae){
			MaliciousChart chart1 = new MaliciousChart("Normal & Malicious URL Chart");
			chart1.pack();
			RefineryUtilities.centerFrameOnScreen(chart1);
			chart1.setVisible(true);
		}
	});

	getContentPane().add(p1,BorderLayout.CENTER);
	getContentPane().add(p2,BorderLayout.NORTH);
}
public static void main(String a[])throws Exception{
	UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	Main main = new Main();
	main.setVisible(true);
	main.setExtendedState(JFrame.MAXIMIZED_BOTH);
}
public void classifyTest(){
	try{
		ArrayList<String> test = new ArrayList<String>();
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line = null;
		while((line = br.readLine())!= null){
			line = line.trim();
			test.add(line);
		}
		br.close();
		ViewTestClassifiction vtc = new ViewTestClassifiction();
		for(int i=0;i<test.size();i++){
			String text = test.get(i);
			Classifier.testClassifier(text,vtc.dtm);
		}
		vtc.setSize(800,600);
		vtc.setVisible(true);
	}catch(Exception e){
		e.printStackTrace();
	}
}
public String getTweet(String url){
	String tweet_text = "";
	for(int i=0;i<ReadTweets.tweets.size();i++){
		String tweet = ReadTweets.tweets.get(i);
		String urls = ReadTweets.urls.get(i);
		if(urls.equals(url)){
			tweet_text = tweet;
			break;
		}
	}
	return tweet_text;
}
public void detection(){
	try{
		malicious_count = 0;
		malicious.clear();
		for(int i=0;i<ReadTweets.tweets.size();i++){
			String tweet = ReadTweets.tweets.get(i);
			String urls = ReadTweets.urls.get(i);
			if(malicious.containsKey(urls)){
				int count = malicious.get(urls) + 1;
				malicious.put(urls,count);
			} else {
				malicious.put(urls,1);
			}
		}
		ViewMaliciousUrl vmu = new ViewMaliciousUrl();
		for(Map.Entry<String,Integer> me : malicious.entrySet()){
			int count = me.getValue();
			String key = me.getKey();
			if(count > 1){
				String expand = MaliciousUrlExpand.expandUrl(key);
				String tweet = getTweet(key);
				Object row[] = {tweet,key,expand,count};
				vmu.dtm.addRow(row);
				malicious_count = malicious_count + count;
			}
		}
		vmu.setVisible(true);
		vmu.setSize(800,600);
	}catch(Exception e){
		e.printStackTrace();
	}
}
}