package com.example.fxtest;
import javafx.collections.ObservableList;
import java.io.*;
public class FileUtil {
    public static void savebooks(ObservableList date,String name) throws IOException {
        int lable=1;
        File file=new File(name);
        System.out.println(file.getPath());
        if(file.exists())
        {
            createFile(name,true,date);
        }
        else
        {
            createFile(name,false,date);
        }
    }
    public static void createFile(String name, boolean lable, ObservableList date) throws IOException {
        BufferedOutputStream out=null;
        StringBuffer sbf=new StringBuffer();
        File csv=new File(name);
        if(lable==false)
        {
            csv.createNewFile();
        }
        OutputStream outs=new FileOutputStream(csv,true);
        out= new BufferedOutputStream(outs);
        for(int i=0;i<date.size();i++)
        {
            out.write(date.get(i).toString().getBytes("GBK"));
            if(i!= date.size()-1)
            {
                out.write(",".getBytes("GBK"));
            }
        }
        out.write("\n".getBytes());
        out.close();
    }
}
