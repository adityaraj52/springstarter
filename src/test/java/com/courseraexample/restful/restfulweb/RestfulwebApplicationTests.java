package com.courseraexample.restful.restfulweb;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@SpringBootTest
class RestfulwebApplicationTests {
	String basePath = "/Users/adityaraj/Documents/Coursera/restfulweb/src/main/resources/";
	String filePath =  basePath + "sufiSongs2.txt";
	String regex_pattern = "((https?|ftp|gopher|telnet|file|notes|ms-help)" +
			":((//)|(\\\\))" +
			"+[\\w\\d:#@%/;" +
			"$()~_?\\+-=\\\\.&]\\*" +
			"\\.mp3)";
	@Test
	void contextLoads() throws IOException {
		readFile(filePath);

	}

	@Test
	void saveListmp3s() throws IOException {
		String filePath =  basePath + "songLinks2.txt";
		File oracle = new File(filePath);
		List<String> listMp3 = new ArrayList<>();
		BufferedReader in = new BufferedReader(new FileReader(oracle));

		String inputLine;
		while ((inputLine = in.readLine()) != null){
			saveMp3(inputLine);
		}
		in.close();
	}

	public String readUrl(String url) throws IOException {
		Document doc = Jsoup.connect(url).get();
		return Jsoup.parse(doc.getElementsByTag("audio").html()).getElementsByTag("a").attr("href");
	}

	public void readFile(String path) throws IOException {
		File oracle = new File(path);
		List<String> listMp3 = new ArrayList<>();
		BufferedReader in = new BufferedReader(new FileReader(oracle));

		String inputLine;
		while ((inputLine = in.readLine()) != null){
			String a = readUrl(inputLine);
			listMp3.add(a);
			System.out.println(a);
		}
		in.close();
	}

	public List<String> findMp3(String content){
		List<String> listMp3 = new ArrayList<>();
		Pattern pattern = Pattern.compile(regex_pattern);
		Matcher matcher = pattern.matcher(content);
		while (matcher.find()){
			listMp3.add(matcher.group());
		}
//		writeToFile(listMp3);
		return listMp3;
	}

	public void saveMp3(String mp3Url) throws IOException {
		URLConnection conn = new URL(mp3Url).openConnection();
		InputStream is = conn.getInputStream();

		OutputStream outstream = new FileOutputStream(new File(basePath + getFileName(mp3Url)) + ".mp3");
		byte[] buffer = new byte[4096];
		int len;
		while ((len = is.read(buffer)) > 0) {
			outstream.write(buffer, 0, len);
		}
		outstream.close();
	}

	public void writeToFile(List<String> string) {
		try {
			FileWriter myWriter = new FileWriter("/Users/adityaraj/Documents/Coursera/restfulweb/src/main/resources/abc.txt");
			string.parallelStream().forEach(stringVal -> {
				try {
					myWriter.write(stringVal);
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
			myWriter.close();
			System.out.println("Successfully wrote to the file.");
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}

	public void writeToFile(String string) {
		try {
			FileWriter myWriter = new FileWriter("/Users/adityaraj/Documents/Coursera/restfulweb/src/main/resources/abc.txt");
			myWriter.append("\n" + string);
			myWriter.close();
			System.out.println("Successfully wrote to the file.");
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}

	public String getFileName(String url){
		String file = url;
		String[] strings = file.split("/");
		String name = strings[strings.length-1];
		int mp3Index = name.indexOf(".mp3");
		name = name.substring(0, mp3Index);
		int nfakIndex = name.indexOf("nusrat-fateh-ali-khan");
		if(nfakIndex != -1){
			return Arrays.stream(name.substring("nusrat-fateh-ali-khan".length()).split("-")).collect(Collectors.joining(" "));
		} else {
			return Arrays.stream(name.split("-")).collect(Collectors.joining(" "));
		}


	}

}
