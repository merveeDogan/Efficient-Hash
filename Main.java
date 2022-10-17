import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class Main {

	//static String src = "";

	public static void main(String[] args) throws IOException {
		String src="";
		HashTable<String, LinkedList<HashValue>> hashTable = new HashTable<String, LinkedList<HashValue>>(31,Menu.LoadFactor(),Menu.HashType(),Menu.CollisionHandlingType());
		               //PUTTING VALUES IN BBC FOLDER INTO A HASHTABLE//
		//-----------------------------------------------------------------------------------------------------------------//
		String DELIMITERS = "[-+=" + " " + "\r\n " + "1234567890" + "’'\"" + "(){}<>\\[\\]" + ":" + "," + "‒–—―" + "…"
				+ "!" + "." + "«»" + "-‐" + "?" + "‘’“”" + ";" + "/" + "⁄" + "␠" + "·" + "&" + "@" + "*" + "\\" + "•"
				+ "^" + "¤¢$€£¥₩₪" + "†‡" + "°" + "¡" + "¿" + "¬" + "#" + "№" + "%‰‱" + "¶" + "′" + "§" + "~" + "¨"
				+ "_" + "|¦" + "⁂" + "☞" + "∴" + "‽" + "※" + "]";
		Scanner sc = null;
		String line = "";
		String text = "";
		String stopWords = "";
		final File stopWordsFile = new File("stop_words_en.txt");
		try {
			sc = new Scanner(stopWordsFile);
			while (sc.hasNextLine()) {
				line = sc.nextLine();
				stopWords += line.equals("") ? "|" : line.replaceAll("\n", "");
			}
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		long diff1 = 0;
		final File folder = new File("bbc");
		final List<File> fileList = Arrays.asList(folder.listFiles());
		for (File file : fileList) {
			final List<File> subfileList = Arrays.asList(file.listFiles());
			for (File textFile : subfileList) {
				src = file.getName() + "_" + textFile.getName();
				text = "";
				try {
					sc = new Scanner(textFile);
					while (sc.hasNextLine()) {
						line = sc.nextLine().toLowerCase(Locale.ENGLISH);
						String afterStopWords = line.replaceAll("\\b(?i)(" + stopWords + ")\\b", "");
						text = text + " " + afterStopWords;
					}
					String[] splitted = text.split(DELIMITERS);
					for (String word : splitted) {
						if (!word.equals("")) {
							if (!hashTable.contains(word)) {
								LinkedList<HashValue> List = new LinkedList<HashValue>();
								HashValue value = new HashValue(src, 1);
								List.add(value);
								long start1 = System.nanoTime();
								hashTable.put(word, List);
								long end1 = System.nanoTime();
								diff1+=(end1-start1);
								
							} else {
								boolean contain = false;
								LinkedList<HashValue> List = hashTable.getValue(word);
								for (int i = 0; i < List.size(); i++) {
									if (List.get(i).getDirectory().equals(src)) {
										long start2 = System.nanoTime();
										List.get(i).setCount(List.get(i).getCount() + 1);
										long end2 = System.nanoTime();
										diff1+=(end2-start2);
										contain = true;
										
									}
								}
								if (!contain) {
									HashValue value = new HashValue(src, 1);
									long start3 = System.nanoTime();
									List.add(value);
									long end3 = System.nanoTime();
									diff1+=(end3-start3);
								}
							}
						}

					}

				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
		//--------------------------------------------------------------------------------------------------------------//
		                        // READING SEARCH TXT AND THROWING IT INTO ARRAY //
		//--------------------------------------------------------------------------------------------------------------//
		String quit;
		int num;
		do {
			 num=Menu.Operation();
			 if (num==1) {
					System.out.println("Enter a key: ");
					String key=Menu.scan.nextLine();
					hashTable.remove(key);
				}
				else if (num==2) {
					line = "";
					String txt = "";
					String path = "search.txt";
					File file1;
					if (new File("search.txt").exists()) {
						file1 = new File("search.txt");
					} else {
						file1 = new File(path);
					}
					Scanner read = new Scanner(file1, "UTF-8");
					while (read.hasNextLine()) {
						line = read.nextLine();
						txt += line + " ";
					}
					String Words[] = txt.split(" ");
					read.close();
					//--------------------------------------------------------------------------------------------------------------//
					                         // TIME CALCULATIONS FOR SEARCH TXT //
					//--------------------------------------------------------------------------------------------------------------//
					double sumSearchTime = 0;
					double minSearchTime = Integer.MAX_VALUE;
					double maxSearchTime = 0;
					long diff = 0;
					for (int i = 0; i < Words.length; i++) {
						long start = System.nanoTime();
						hashTable.SearchTXT((String) Words[i]);
						long end = System.nanoTime();
						Boolean check=hashTable.SearchTXT((String) Words[i]);
						if (check==true) {
							diff = (end - start);
						}
						sumSearchTime += (end - start);
						if (diff < minSearchTime)
							minSearchTime = diff;
						if (diff > maxSearchTime)
							maxSearchTime = diff;
					}
					sumSearchTime = sumSearchTime / 1000;
					System.out.println("> Total Collision: "+HashTable.collisionCount);
					System.out.println("> Indexing Time: "+diff1);
					System.out.println("> Minimum search time :" + minSearchTime);
					System.out.println("> Maximum search time :" + maxSearchTime);
					System.out.println("> Average search time :" + sumSearchTime);
					//--------------------------------------------------------------------------------------------------------------//
					
				}
				else if(num==3) {
					hashTable.SearchKey();
				}
			System.out.println("Do you want to do another operation? Yes(Y)-No(N)");
			quit=Menu.scan.nextLine();
		} while (!quit.equalsIgnoreCase("N"));
		
		

	}

}
