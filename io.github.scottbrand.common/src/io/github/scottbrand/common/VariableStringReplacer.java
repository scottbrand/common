package io.github.scottbrand.common;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;



/**
 * VariableStringReplacer is a simple utility class that allows for basic variable
 * substitution in a string. By default character surrounded by a default
 * enclosure of "${x}", will then attempt to substitute the value "x" with
 * itself replacement value found in the given replacementMap where the value
 * "x" is used as the key into the replacementMap. This class does support
 * recursive substitution as well. For example:
 * 
 * <pre>
 * ${abc-${foo}}
 * 
 *  will match "foo" and replace "foo" with its looked up value - for example, "bar".
 *  the pattern would then be:
 *  
 * ${abc-bar}
 * 
 * Then abc-bar would be the key to look up for final replacement.
 * </pre>
 * 
 * The class first uses the given replacement map to look for substitution
 * matches, then if a key is not found there, it will look in
 * System.getProperty() and if not found there, in System.getEnv().get()
 * 
 * 
 * @author Scott Brand (Scott Brand)
 *
 */
public final class VariableStringReplacer
{
    private int escapeChar    = '\\';
    private int firstOpenChar = '$';
    private int openChar      = '{';
    private int closeChar     = '}';





    /**
     * Convenience method to do variable replacement. The name of the method is
     * plural not to make it plural but the "s" is to designate it as a static
     * method and to differentiate it from the instance method "replace" which
     * this method actually calls.
     * 
     * This method will silently remove variables on the returned string if
     * their key value is not found in any of the maps.
     * 
     * @param value
     *            the String to scan and do variable replace on.
     * @param replacementMap
     *            the map that contains replacement keys and their values.
     * @return a String with captured variables replaced.
     */
    public static final String replaces(String value, Map<String, String> replacementMap)
    {
        return new VariableStringReplacer().replace(value, replacementMap);
    }





    /**
     * Convenience method to do variable replacement. The name of the method is
     * plural not to make it plural but the "s" is to designate it as a static
     * method and to differentiate it from the instance method "replace" which
     * this method actually calls.
     * 
     * This method will throw an {@link java.lang.IllegalArgumentException} if
     * any of the variables key values are not found in any of the maps.
     * 
     * @param value
     *            the String to scan and do variable replace on.
     * @param replacementMap
     *            the map that contains replacement keys and their values.
     * @param throwExceptionWhenNoReplacement
     *            boolean that when true will throw an exception if no matching
     *            key is found.
     * @return a String with captured variables replaced.
     */
    public static final String replaces(String value, Map<String, String> replacementMap, boolean throwExceptionWhenNoReplacement) throws IllegalArgumentException
    {
        return new VariableStringReplacer().replace(value, replacementMap,throwExceptionWhenNoReplacement);
    }





    /**
     * Replaces all detected variables with their replacement value.
     * 
     * This method will silently remove variables on the returned string if
     * their key value is not found in any of the maps.
     * 
     * @param value
     *            the String to scan and do variable replace on.
     * @param replacementMap
     *            the map that contains replacement keys and their values.
     * @return a String with captured variables replaced.
     */
    public String replace(String value, Map<String, String> replacementMap)
    {
        return replace(value, replacementMap, false);
    }





    /**
     * Replaces all detected variables with their replacement value.
     * 
     * This method will throw an {@link java.lang.IllegalArgumentException} if
     * any of the variables key values are not found in any of the maps.
     * 
     * @param value
     *            the String to scan and do variable replace on.
     * @param replacementMap
     *            the map that contains replacement keys and their values.
     * @return a String with captured variables replaced.
     */
    public String replace(String value, Map<String, String> replacementMap, boolean throwExceptionWhenNoReplacement) throws IllegalArgumentException
    {
        if (value == null || value.length() == 0)
            return value;

        StringBuilder finalString = new StringBuilder(256);
        Deque<StringBuilder> stack = new ArrayDeque<StringBuilder>();

        stack.push(finalString);

        StringBuilder sb = finalString;
        int index = 0;

        while (index < value.length())
        {
            if (isNextChar(value, index, firstOpenChar))
            {
                if (wasLastChar(value, index, escapeChar) == false && isNextChar(value, index + 1, openChar))
                {
                    index = index + 2;
                    if (index >= value.length())
                    {
                        sb.append((char) firstOpenChar).append((char) openChar);
                        break;
                    }
                    else
                    {
                        // starting a substitution
                        StringBuilder newSub = new StringBuilder(64);
                        stack.push(newSub);
                        sb = newSub;
                    }
                }
                else
                {
                    //
                    // if last char escaped '$' then remove the escape char from
                    // our buffer
                    // and just put the '$'
                    //
                    if (wasLastChar(value, index, escapeChar))
                        sb.setLength(sb.length() - 1);
                    sb.append(value.charAt(index++));
                }
            }
            else if (isNextChar(value, index, closeChar))
            {
                if (wasLastChar(value, index, escapeChar))
                {
                    sb.append((char) closeChar);
                    index = index + 2;
                }
                else
                {
                    if (stack.size() == 1) // trailing '}' not part of any
                                           // current substitution
                    {
                        sb.append((char) closeChar);
                        index++;
                    }
                    else
                    {
                        // closing a substituiton;

                        //
                        // when stack size == 2, then we only have our original
                        // string
                        // plus the current ${x} substitution that we are
                        // working, so
                        // pop "x" off the stack and replace it with its
                        // substitution value.
                        //
                        // when stack size > 2 then we have a substitution
                        // within a substitution
                        // like ${xyz-${abc}}, so at this point we have closed
                        // out the "abc"
                        // and if we find it has a substitution value of "foo"
                        // we would have
                        // a current replacement of ${xyz-foo}
                        //

                        StringBuilder varSb = stack.pop();
                        sb = stack.peek();
                        String r = replaceKey(varSb.toString(), replacementMap);
                        if (r != null)
                            sb.append(r);
                        else if (throwExceptionWhenNoReplacement)
                            throw new IllegalArgumentException("Unable to find replacement for variable: " + varSb);
                        index++;
                    }
                }
            }
            else
            {
                sb.append(value.charAt(index++));
            }
        }

        return sb.toString();
    }





    private String replaceKey(String matchKey, Map<String, String> replacementMap)
    {
        if (replacementMap != null && replacementMap.containsKey(matchKey))
            return replacementMap.get(matchKey).toString();
        String v = System.getProperty(matchKey);
        if (v != null)
            return v;
        v = System.getenv().get(matchKey);
        if (v != null)
            return v;
        return null;
    }





    private boolean isNextChar(String input, int atIndex, int charToCheckFor)
    {
        return (atIndex < 0 || atIndex >= input.length()) ? false : input.charAt(atIndex) == charToCheckFor;
    }





    private boolean wasLastChar(String input, int atIndex, int charToCheckFor)
    {
        int beforeIndex = atIndex - 1;
        return (beforeIndex < 0 || beforeIndex >= input.length()) ? false : input.charAt(beforeIndex) == charToCheckFor;
    }

}
