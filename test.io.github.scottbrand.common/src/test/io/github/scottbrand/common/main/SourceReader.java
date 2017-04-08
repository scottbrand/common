package test.io.github.scottbrand.common.main;

import static java.nio.file.FileVisitResult.CONTINUE;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;

import io.github.scottbrand.common.ServiceLocator;
import io.github.scottbrand.common.Strings;

public class SourceReader
{

    public void readFiles()
    {
        Paths.get("C:\\temp\\etl\\src");
        
        try
        {
            Files.walkFileTree(Paths.get("C:\\temp\\etl\\src"), new PrintFiles());
        }
        catch (IOException e)
        {
            ServiceLocator.getLogger().error(Strings.EXCEPTION,e);
        }
    }





    static class PrintFiles extends SimpleFileVisitor<Path>
    {

        // Print information about
        // each type of file.
        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attr)
        {
            if (attr.isSymbolicLink())
            {
            }
            else if (attr.isRegularFile())
            {

                if (file.toFile().getAbsolutePath().endsWith(".java"));
                System.out.format("Process this file: %s ,  length is %d\n",file,attr.size());
                processFile(file);

            }
            else
            {
            }
            return CONTINUE;
        }





        // Print each directory visited.
        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException exc)
        {
            return CONTINUE;
        }





        // If there is some error accessing
        // the file, let the user know.
        // If you don't override this method
        // and an error occurs, an IOException
        // is thrown.
        @Override
        public FileVisitResult visitFileFailed(Path file, IOException exc)
        {
            System.err.println(exc);
            return CONTINUE;
        }
        
        
        
        
        private void processFile(Path sourceFile)
        {
            try
            {
                String sf = sourceFile.toString();
                String newFile = sf.replace("src", "modifiedSrc");
                File f = new File(newFile);
                Files.createDirectories(Paths.get(f.getParent()));
                PrintWriter pw = new PrintWriter(newFile);
                List<String> lines = Files.readAllLines(sourceFile);
                int i = 1;
                boolean determinedPrefix = false;
                int lowerBound = 2;
                int upperBound = 4;
                for (String s : lines)
                {
                    if (s.trim().length() == 0)
                        break;
                    if (determinedPrefix == false)
                    {
                        determinedPrefix = true;
                        for (int c = lowerBound; c < s.length(); c++)
                            if (s.charAt(c) == '*')
                            {
                                upperBound = c - 1;
                                break;
                            }
                    }
                    String start = s.substring(0, upperBound+3);
                    String after = s.substring(upperBound+4);
                    if (after.trim().startsWith("//"))
                        continue;
                    String number = start.substring(lowerBound,upperBound).trim();
                    if (number.length() == 0)
                        pw.println(after);
                    else
                    {
                        int realLine = 0;
                        try
                        {
                            realLine = Integer.parseInt(number);
                        }
                        catch (Throwable t)
                        {
                            for (String s1 : lines)
                                pw.println(s1);
                            break;
                        }
                        if (i < realLine)
                        {
                            do
                            {
                                i++;
                                pw.println("");
                            } while (i < realLine);
                        }
                        pw.println(after + "\t\t//" + number + " @(" + i + ")");
                    }
                    i++;
                }
                pw.flush();
                pw.close();
                ServiceLocator.getLogger().debug("DONE");
                
            }
            catch (IOException e)
            {
                ServiceLocator.getLogger().error(Strings.EXCEPTION,e);
            }
            
        }

    }
    
    
    
    
    
    
    public static void main(String[] args)
    {
        SourceReader sr = new SourceReader();
        sr.readFiles();
    }
}
