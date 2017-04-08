package test.io.github.scottbrand.common.l10n;

import io.github.scottbrand.common.ServiceLocator;
import io.github.scottbrand.common.l10n.ITranslationService;
import io.github.scottbrand.common.l10n.L10nMessage;



public enum EnumMsg
{
	UNLOCKED
	{
		public String display()
		{
			return L10nMessage.get(EnumMsg.class, UNLOCKED);
		}
	},
	LOCKED
	{
		public String display()
		{
			return ServiceLocator.getInstance().getService(ITranslationService.class).translate("LOCKED");
		}
	},
	SYSTEM_LOCKED
	{
		public String display()
		{
			return ServiceLocator.getInstance().getService(ITranslationService.class).translate("SYSTEMLOCKED");
		}
	};

	public abstract String display();

}
