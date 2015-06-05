package com.graphanalysis.web.json;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class JsonDeal {
	 public static void writeFile(String filePath, String sets)  
	            throws IOException {  
	        FileWriter fw = new FileWriter(filePath);  
	        PrintWriter out = new PrintWriter(fw);  
	        out.write(sets);  
	        out.println();  
	        fw.close();  
	        out.close(); 
	        System.out.println("写入成功");
	    }  
	  
	    public static String ReadFile(String path) {  
	        File file = new File(path);  
	        BufferedReader reader = null;  
	        String laststr = "";  
	        try {  
	            reader = new BufferedReader(new FileReader(file));  
	            String tempString = null;  
	            while ((tempString = reader.readLine()) != null) {  
	                laststr = laststr + tempString;  
	            }  
	            reader.close();  
	        } catch (IOException e) {  
	            e.printStackTrace();  
	        } finally {  
	            if (reader != null) {  
	                try {  
	                    reader.close();  
	                } catch (IOException e1) {  
	                }  
	            }  
	        }  
	        return laststr;  
	    }  
}
