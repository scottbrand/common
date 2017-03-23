package io.github.scottbrand.common;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Map;

public class Sed
{
    public int sed(File input, File output, Map<String,String> args)
    {
        return sed(input,output,args,true);
    }
    
    
    

	public int sed(File input, File output, Map<String,String> args, boolean failOnNoMatch)
    {
        FileReader     fr = null;
        BufferedReader br = null;
        BufferedWriter bw = null;
        PrintWriter    pw = null;
        FileWriter     fw = null;
        
        Patterns        pr    = new Patterns();
        int             count = 0;
        try
        {
            fr = new FileReader(input);
            br = new BufferedReader(fr);
            
            fw = new FileWriter(output);
            bw = new BufferedWriter(fw);
            pw = new PrintWriter(bw);
            
            String s = null;
            while ( (s=br.readLine()) != null)
            {
                pw.print(pr.doReplace(s,args,failOnNoMatch));
                pw.print("\n");
                count = count + pr.getReplacementCount();
            }
            return count;
        }
        catch (Throwable t)
        {
            return -1;
        }
        finally
        {
            if (pw != null)
                pw.close();
            if (bw != null)
                try {bw.close();} catch (Throwable t) {};
            if (fw != null)
                try {fw.close();} catch (Throwable t) {};
            if (br != null)
                try {br.close();} catch (Throwable t) {};
            if (fr != null)
                try {fr.close();} catch (Throwable t) {};
        }
    }
}
