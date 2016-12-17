package com.yanbinwa.OASystem.Utils;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class FileUtils
{
    public static String readJsonFile(String filename)
    {
        String ret = "";        
        try
        {
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(new FileReader(filename));
            if (obj instanceof JSONObject)
            {
                JSONObject jsonObj = (JSONObject)obj;
                ret = jsonObj.toJSONString();
            }
            else if(obj instanceof JSONArray)
            {
                JSONArray jsonArray = (JSONArray)obj;
                ret = jsonArray.toJSONString();
            }
        } 
        catch (IOException | ParseException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ret;
    }
    
    public static void writeFile(String filename, String context)
    {
        try
        {
            FileWriter writer = new FileWriter(filename);
            writer.write(context);
            writer.close();
        } 
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
