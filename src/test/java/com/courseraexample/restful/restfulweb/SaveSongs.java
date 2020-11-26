package com.courseraexample.restful.restfulweb;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@SpringBootTest
class SaveSongs {
	String basePath = "/Users/adityaraj/Documents/Coursera/restfulweb/src/main/resources/";
	String songsUrl =  basePath + "sufiSongs.txt";
	String filePathTosaveSongs = "songs/";

	@Test
	void contextLoads() throws IOException {
		readFile(songsUrl);
	}

	public void readFile(String path) throws IOException {
		List<String> e = new ArrayList<>();

		Files.lines(Paths.get(path))
				.parallel()
				.forEach(i -> {
					try {
						e.addAll(getMp3LinksFromUrl(i));
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				});

			e.parallelStream()
				.forEach(i -> {
					try {
						saveMp3(i);
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				});
	}


	public List<String> getMp3LinksFromUrl(String url) throws IOException {
		if(url.endsWith("mp3") || url.endsWith("wav")){
			saveMp3(url);
			return new ArrayList<>();
		}
		Document doc = Jsoup.connect(url).get();
		return doc.getElementsByAttributeValueContaining("href", "mp3")
				.parallelStream()
				.map(i -> i.attr("href"))
				.filter(i -> i.endsWith(".mp3"))
				.collect(Collectors.toList());
	}

	public static String encodeValue(String value) {
		try {
			return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
		} catch (UnsupportedEncodingException ex) {
			throw new RuntimeException(ex.getCause());
		}
	}


	public void saveMp3(String mp3Url) throws IOException {
		URLConnection conn = new URL(mp3Url.replace(" ", "%20")).openConnection();
		InputStream is = conn.getInputStream();
		OutputStream outstream = new FileOutputStream(new File(basePath + filePathTosaveSongs + getFileName(mp3Url)));
		byte[] buffer = new byte[4096];
		int len;
		while ((len = is.read(buffer)) > 0) {
			outstream.write(buffer, 0, len);
		}
		System.out.println("Save file "+ mp3Url);
		outstream.close();
	}

	public String getFileName(String url){
		String[] strings = url.split("/");
		String name = strings[strings.length-1];
		int nfakIndex = name.indexOf("nusrat-fateh-ali-khan");
		if(nfakIndex != -1){
			return String.join(" ", name.substring("nusrat-fateh-ali-khan".length()).split("-"));
		} else {
			return String.join(" ", name.split("-"));
		}
	}
}
