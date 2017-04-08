package test.io.github.scottbrand.common.l10n;

import java.util.Date;
import java.util.Locale;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import io.github.scottbrand.common.ServiceLocator;
import io.github.scottbrand.common.l10n.ITranslationService;
import io.github.scottbrand.common.l10n.L10nMessage;

@Component
public class L10nTest
{
    private volatile ITranslationService translationService;
    
    
    private enum Messages
    {
        MSG1,
        FOR_DISPLAY,
        FOR_DISPLAY_WITH_PARAM
    };
    
    @Activate
    public void activate()
    {
        Locale currentLocale = Locale.getDefault();
        ServiceLocator.getLogger().debug("Testing default locale properties, default Locale is: " + Locale.getDefault());
        ServiceLocator.getLogger().debug("gm = " + translationService.translate("gm"));
        ServiceLocator.getLogger().debug("gn = " + translationService.translate("gn"));
        ServiceLocator.getLogger().debug("su = " + translationService.translate("su"));
        ServiceLocator.getLogger().debug("sd = " + translationService.translate("sd"));
        
        ServiceLocator.getLogger().debug("class message 0 = " + translationService.translate(this, 0));
        ServiceLocator.getLogger().debug("class message 1 = " + translationService.translate(this, 1));
        ServiceLocator.getLogger().debug("class message 2, no parms provided = " + translationService.translate(this, 2));
        ServiceLocator.getLogger().debug("class message 2, parms provided = " + translationService.translate(this, 2,"Scott",new Date().toString()));
        
        Locale.setDefault(Locale.FRANCE);
        ServiceLocator.getLogger().debug("\n\nTesting french locale properties, current Locale is: " + Locale.getDefault());
        translationService.setLocale(Locale.FRENCH);
        
        ServiceLocator.getLogger().debug("gm = " + translationService.translate("gm"));
        ServiceLocator.getLogger().debug("gn = " + translationService.translate("gn"));
        ServiceLocator.getLogger().debug("su = " + translationService.translate("su"));
        ServiceLocator.getLogger().debug("sd = " + translationService.translate("sd"));
        
        ServiceLocator.getLogger().debug("class message 0 = " + translationService.translate(this, 0));
        ServiceLocator.getLogger().debug("class message 1 = " + translationService.translate(this, 1));
        ServiceLocator.getLogger().debug("class message 2, no parms provided = " + translationService.translate(this, 2));
        ServiceLocator.getLogger().debug("class message 2, parms provided = " + translationService.translate(this, 2,"Scott",new Date().toString()));


//        Locale[] locales = Locale.getAvailableLocales();
//        for (Locale l : locales)
//            ServiceLocator.getLogger().debug(l);
        Locale es = new Locale("es");
        Locale.setDefault(es);
        ServiceLocator.getLogger().debug("\n\nTesting spanish locale properties, current Locale is: " + Locale.getDefault());
        translationService.setLocale(es);
        
        ServiceLocator.getLogger().debug("gm = " + translationService.translate("gm"));
        ServiceLocator.getLogger().debug("gn = " + translationService.translate("gn"));
        ServiceLocator.getLogger().debug("su = " + translationService.translate("su"));
        ServiceLocator.getLogger().debug("sd = " + translationService.translate("sd"));
        
        ServiceLocator.getLogger().debug("class message 0 = " + translationService.translate(this, 0));
        ServiceLocator.getLogger().debug("class message 1 = " + translationService.translate(this, 1));
        ServiceLocator.getLogger().debug("class message 2, no parms provided = " + translationService.translate(this, 2));
        ServiceLocator.getLogger().debug("class message 2, parms provided = " + translationService.translate(this, 2,"Scott",new Date().toString()));

        
        Locale.setDefault(Locale.CHINESE);
        ServiceLocator.getLogger().debug("\n\nTesting Chinese locale properties, current Locale is: " + Locale.getDefault());
        translationService.setLocale(Locale.CHINESE);
        
        ServiceLocator.getLogger().debug("gm = " + translationService.translate("gm"));
        ServiceLocator.getLogger().debug("gn = " + translationService.translate("gn"));
        ServiceLocator.getLogger().debug("su = " + translationService.translate("su"));
        ServiceLocator.getLogger().debug("sd = " + translationService.translate("sd"));
        
        ServiceLocator.getLogger().debug("class message 0 = " + translationService.translate(this, 0));
        ServiceLocator.getLogger().debug("class message 1 = " + translationService.translate(this, 1));
        ServiceLocator.getLogger().debug("class message 2, no parms provided = " + translationService.translate(this, 2));
        ServiceLocator.getLogger().debug("class message 2, parms provided = " + translationService.translate(this, 2,"Scott",new Date().toString()));
        
        
        translationService.setLocale(currentLocale);
        ServiceLocator.getLogger().debug("testing bundle L10nMessage: " + L10nMessage.get(this,0));
        ServiceLocator.getLogger().debug("testing bundle L10nMessage with params: " + L10nMessage.get(this,2,"Scott",(new Date()).toString()));
        
        ServiceLocator.getLogger().debug("testing bundle L10nMessage on Enum: locked: " + EnumMsg.LOCKED.display());
        ServiceLocator.getLogger().debug("testing bundle L10nMessage on Enum: systemlocked: " + EnumMsg.SYSTEM_LOCKED.display());
        ServiceLocator.getLogger().debug("testing bundle L10nMessage on Enum: unlocked: " + EnumMsg.UNLOCKED.display());
        
        
        ServiceLocator.getLogger().debug("testing bundle L10nMessage on Enum: MSG1: " + L10nMessage.get(this,Messages.MSG1));
        ServiceLocator.getLogger().debug("testing bundle L10nMessage on Enum: FOR_DISPLAY: " + L10nMessage.get(this,Messages.FOR_DISPLAY));
        ServiceLocator.getLogger().debug("testing bundle L10nMessage on Enum: FOR_DISPLAY_WITH_PARAM: " + L10nMessage.get(this,Messages.FOR_DISPLAY_WITH_PARAM,"Scott"));
        
        
        Locale.setDefault(currentLocale);
        System.exit(0);

    }
    
    
    @Reference
    public void setTranslationService(ITranslationService translationService)
    {
        this.translationService = translationService;
    }
}
