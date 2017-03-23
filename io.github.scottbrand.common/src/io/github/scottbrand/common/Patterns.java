package io.github.scottbrand.common;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Patterns
{
	public static final Pattern p = Pattern.compile("\\$\\{([\\w\\d_]+)\\}|%([\\w\\d_]+)%");
	
	public static final Pattern p2 = Pattern.compile("\\{([\\d]+)\\}");

    public static final Pattern          NamePattern              = Pattern.compile("[A-Za-z][\\w\\d_ ]*");

    public static final Pattern          ColumnPattern            = Pattern.compile("[A-Za-z][\\w\\d_\\$]*");
    
    public static final Pattern          DescriptionPattern       = Pattern.compile("[\\S].*");
    
    public static final Pattern          NonWhiteSpacePattern     = Pattern.compile("[\\S]+");

    public static final Pattern          WholeNumberPattern       = Pattern.compile("[1-9][\\d]*");

    public static final Pattern          IntegerPattern           = Pattern.compile("[\\-+]?[\\d]+");

    public static final Pattern          RealNumberPattern        = Pattern.compile("[\\-+]?[\\d]+([\\.]?[\\d]+)?");

    public static final Pattern          AlphaNumPattern          = Pattern.compile("[\\w\\d]+");
    
    public static final Pattern          AlphaPattern             = Pattern.compile("[\\w]+");

    public final static Pattern          newLinePattern           = Pattern.compile("\n|\r|\t");

    public final static Pattern          trimPattern              = Pattern.compile("^[ ]*|[ ]*$");

    public final static Pattern          normalizePattern         = Pattern.compile("('[^']*')|([ ]+)");

    public final static Pattern          nonPrintablePattern      = Pattern.compile("[\\x00-\\x08\\x0E-\\x1F\\x0B\\x0C\\u2000-\\u2FFF]");

	
	private int count = 0;
	
	private static final Patterns INSTANCE = new Patterns();
	
	
	
    public String doReplace(String rawText, Map<String,String> vars) throws Exception
    {
        return doReplace(rawText,vars,true);
    }
    
    
    
    public String doReplace(String rawText, Map<String,String> vars, boolean failOnNoMatch) throws Exception
    {
        if (vars.isEmpty() || rawText == null)
            return rawText;
        
        count = 0;
        
        Matcher m = p.matcher(rawText);
        StringBuffer sb = null;
        boolean matched = false;
        while (m.find())
        {
            if (matched == false)
            {
                matched = true;
                sb = new StringBuffer();
            }
            String expr = m.group(1);
            if (expr == null)
                expr = m.group(2);
            String v    = vars.get(expr);
            if (v == null)
            {
                if (failOnNoMatch)
                    throw new Exception("Matched expression: '" + expr + "' but found no replacement value");
                else
                    continue;
            }
            m.appendReplacement(sb, v);
            count++;
        }
        if (matched)
        {
            m.appendTail(sb);
            return sb.toString();
        }
        else 
            return rawText;
    }
    
   
    
    /**
     * doReplace will replace values with a pattern like {#} in a String.
     * The number between the {} holder should be an index in the args array.
     * For example,
     * doReplace("The {0} {1} fox jumped {2} over the {3}",true,"quick","brown","over","box");
     * Would result in:
     * <pre>The quick brown fox jumped over the box</pre>
     * @param text - The String that will be replaced.
     * @param failOnNoMatch - If an index is found that is outside of the args array, then throw an
     *                        exception if true.  Otherwise, fill the replacement with an empty String value.
     * @param args - Array of Strings that will be the substitution parameters.
     * @return - String with substituitions made.
     * @throws Exception - If an index value is outside the args array boundary and when the failOnNoMatch flag is true.
     */
    public String doReplace(String text, boolean failOnNoMatch, String... args) throws Exception
    {
        if (args == null || args.length == 0)
            return text;
        
        
        Matcher m = p2.matcher(text);
        StringBuffer sb = null;
        boolean matched = false;
        while (m.find())
        {
            if (matched == false)
            {
                matched = true;
                sb = new StringBuffer();
            }
            String expr = m.group(1);
            if (expr == null)
                expr = m.group(2);
            Integer index = Numbers.getInteger(expr, -1, 0, args.length-1);
            String v = null;
            if (index == -1)
            {
                if (failOnNoMatch)
                    throw new Exception("Matched expression: '" + expr + "' but found no replacement value");
                else
                    v = "";
            }
            else
            	v = args[index];
            m.appendReplacement(sb, v);
            count++;
        }
        if (matched)
        {
            m.appendTail(sb);
            return sb.toString();
        }
        else 
            return text;
    }
    
    
    
    
    public static final synchronized String replace(String text, boolean failOnNoMatch, String... args) 
    {
    	try
    	{
    		return INSTANCE.doReplace(text, failOnNoMatch, args);
    	}
    	catch (Throwable t)
    	{
    		return Strings.EMPTY;
    	}
    }
    
    
    public static final synchronized String replace(String text, boolean failOnNoMatch, Map<String,String> args) 
    {
    	try
    	{
    		return INSTANCE.doReplace(text, args, failOnNoMatch);
    	}
    	catch (Throwable t)
    	{
    		return Strings.EMPTY;
    	}
    }
    
    
    
    public Integer getReplacementCount()
    {
    	return count;
    }
    
    
    
    public final static boolean isMatch(Pattern p, String text)
    {
        return p == null || text == null ? false : p.matcher(text).matches();
    }
    
    
    public final static boolean contains(Pattern p, String text)
    {
        return p == null || Strings.isEmpty(text) ? false : p.matcher(text).find();
    }
    
    
    
    
    public final static boolean isMatch(Pattern p, String text, Integer maxLen)
    {
        Boolean result = p == null || text == null ? false : p.matcher(text).matches();
        return result == false || maxLen == null ? result : text.length() <= maxLen;
    }

    
    public final static boolean isMatch(Pattern p, String text, Integer minLen, Integer maxLen)
    {
        Boolean result = p == null || text == null ? false : p.matcher(text).matches();
        return result == false || minLen == null || maxLen == null ? result : text.length() >= minLen && text.length() <= maxLen;
    }

}
