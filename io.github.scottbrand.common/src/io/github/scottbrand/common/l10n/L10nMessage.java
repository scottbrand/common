package io.github.scottbrand.common.l10n;

import io.github.scottbrand.common.ServiceLocator;
import io.github.scottbrand.common.Strings;



/**
 * Static wrapper object around the ITranslationService for lookuping up localized messages based on the simpleName of
 * the class name passed in.
 * 
 * @author Scott Brand
 *
 */
public class L10nMessage
{

    public static final int CANCEL  = 0;
    public static final int CONFIRM = 1;
    public static final int DELETE  = 2;
    public static final int ERROR   = 3;
    public static final int NO      = 4;
    public static final int OK      = 5;
    public static final int SAVE    = 6;
    public static final int WARNING = 7;
    public static final int YES     = 8;
    public static final int INFO    = 9;





    public static final String get(int standard)
    {
        return getInternal(L10nMessage.class, standard, (String) null);
    }





    public static final String get(Class<?> clazz)
    {
        return getInternal(clazz, -1, (String) null);
    }





    public static final String get(Class<?> clazz, String... args)
    {
        return getInternal(clazz, -1, args);
    }





    public static final String get(Class<?> clazz, int index)
    {
        return getInternal(clazz, index, (String) null);
    }





    public static final String get(Class<?> clazz, Enum<?> enumName)
    {
        return getInternal(clazz, enumName, (String) null);
    }





    public static final String get(Class<?> clazz, int index, String... args)
    {
        return getInternal(clazz, index, args);
    }
    
    
    
    public static final String get(Class<?> clazz, Enum<?> enumName, String... args)
    {
        return getInternal(clazz, enumName, args);
    }



    public static final String get(String key)
    {
        if (Strings.isNullOrEmpty(key)) 
            return Strings.EMPTY;
        ITranslationService tService = ServiceLocator.getTranslationService();
        if (tService == null)
            return Strings.EMPTY;
        return tService.translate(key);
    }


    public static final String get(Object clazz)
    {
        return (clazz == null) ? Strings.EMPTY : getInternal(clazz.getClass(), -1, (String) null);
    }





    public static final String get(Object clazz, String... args)
    {
        return (clazz == null) ? Strings.EMPTY : getInternal(clazz.getClass(), -1, args);
    }





    public static final String get(Object clazz, int index)
    {
        return (clazz == null) ? Strings.EMPTY : getInternal(clazz.getClass(), index, (String) null);
    }





    public static final String get(Object clazz, Enum<?> enumName)
    {
        return (clazz == null) ? Strings.EMPTY : getInternal(clazz.getClass(), enumName, (String) null);
    }





    public static final String get(Object clazz, Enum<?> enumName, String... args)
    {
        return (clazz == null) ? Strings.EMPTY : getInternal(clazz.getClass(), enumName, args);
    }





    public static final String get(Object clazz, int index, String... args)
    {
        return (clazz == null) ? Strings.EMPTY : getInternal(clazz.getClass(), index, args);
    }





    private static final String getInternal(Class<?> clazz, int index, String... bindings)
    {
        if (clazz == null)
            return Strings.EMPTY;
        String key = clazz.getSimpleName();
        if (index >= 0)
            key = key + "_" + index;
        ITranslationService tService = ServiceLocator.getTranslationService();
        if (tService == null)
            return Strings.EMPTY;
        return (bindings == null || bindings.length == 0) ? tService.translate(key) : tService.translate(key, bindings);
    }





    private static final String getInternal(Class<?> clazz, Enum<?> enumName, String... bindings)
    {
        if (clazz == null || enumName == null)
            return Strings.EMPTY;

        String key = Strings.concat(clazz.getSimpleName(),Strings.UNDERSCORE,enumName.name());
        ITranslationService tService = ServiceLocator.getTranslationService();
        if (tService == null)
            return Strings.EMPTY;
        return (bindings == null || bindings.length == 0) ? tService.translate(key) : tService.translate(key, bindings);
    }

}
