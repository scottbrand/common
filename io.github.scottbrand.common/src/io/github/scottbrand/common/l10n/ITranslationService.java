package io.github.scottbrand.common.l10n;

import java.util.Locale;


/**
 * Interface used to enable system wide message Localization.
 * Only one instance of this interface should be exposed as a 
 * service. 
 * 
 * @author Scott Brand
 *
 */
public interface ITranslationService
{
    
    /**
     * When a key/value pair is requested from the Translation service
     * and the key/value cannot be found then the string returned is:
     * <code>!Missing[x]</code>  with x being the key name given.
     */
    public static final String MISSING_KEY_PREFIX = "!Missing[";
    
    
    
	/**
	 * Retrieves the String found with key property for the default Locale.
	 * If no String for the given key is found in the default Locale, the
	 * key value is returned.
	 * 
	 * @param key
	 * @return
	 */
    String translate(String key);
    

	/**
	 * Retrieves the String found with key property for the given Locale.
	 * If the String is not found then an upward search to the default Locale is
	 * performed to find the best matching String.
	 * If no String for the given key is found in the set of Locales, the
	 * key value is returned.
	 * 
	 * 
	 * @param key
	 * @return
	 */
    String translateLocale(String key, String locale);
    
    
    
    
    
	/**
	 * Retrieves the String found with key property for the default Locale.
	 * If the String is not found then an the key value itself is returned.
	 * Upon finding find the String, replace all {Integer} values with their
	 * associated String found in the bindings arrays.
	 * Note, in the {Integer} expression, the "Integer" should be replaced
	 * with an Integer value such as:  {0} or {1}
	 * If the Integer value inside the {Integer} expression is greater than
	 * the number of supplied bindings, then the value is replaced with "?"
	 * The value substituted from the bindings array is always the String 
	 * derived from String.valueOf(object).
	 * 
	 * @param key
	 * @return
	 */
    String translate(String key, String... bindings);
    
    
    
	/**
	 * Retrieves the String found with key property for the default Locale.
	 * If the String is not found then an upward search to the default Locale is
	 * performed to find the best matching String. 
	 * Upon finding find the String, replace all {Integer} values with their
	 * associated String found in the bindings arrays.
	 * Note, in the {Integer} expression, the "Integer" should be replaced
	 * with an Integer value such as:  {0} or {1}
	 * If the Integer value inside the {Integer} expression is greater than
	 * the number of supplied bindings, then the value is replaced with "?"
	 * The value substituted from the bindings array is always the String 
	 * derived from String.valueOf(object).
	 * 
	 * @param key
	 * @return
	 */
    String translateFull(String key, String locale, String... bindings);
    
    
    
    /**
     * Retrieves the String found with the key property for the current locale.
     * The key is derived by taking the clazz Objects simple class name 
     * (i.e. clazz.getClass().getSimpleName() and then appending an 
     * underscore along with the value of the index.
     * For example, if clazz is an object of <code>Foo</code> and 
     * index is 1, then the key will be: <code>Foo_1</code>
     * 
     * 
     * @param clazz
     * @param index
     * @return
     */
    String translate(Object clazz, int index);
    
    
    
    /**
     * Retrieves the String found with the key property for the current locale.
     * The key is derived by taking the clazz Objects simple class name 
     * (i.e. clazz.getClass().getSimpleName() and then appending an 
     * underscore along with the value of the index.
     * For example, if clazz is an object of <code>Foo</code> and 
     * index is 1, then the key will be: <code>Foo_1</code>
     * 
     * Upon finding find the String, replace all {Integer} values with their
	 * associated String found in the bindings arrays.
	 * Note, in the {Integer} expression, the "Integer" should be replaced
	 * with an Integer value such as:  {0} or {1}
	 * If the Integer value inside the {Integer} expression is greater than
	 * the number of supplied bindings, then the value is replaced with "?"
	 * The value substituted from the bindings array is always the String 
	 * derived from String.valueOf(object).
     * 
     * @param clazz
     * @param index
     * @return
     */
    String translate(Object clazz, int index, String... bindings);
    
    
    
    boolean setLocale(Locale locale);
    
    
    
    boolean resetLocale();
    
    
    String getLocale();
    
    
}
