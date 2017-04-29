package v0id.exp.registry;

import net.minecraftforge.common.AchievementPage;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import v0id.api.exp.data.ExPAchievements;

public class ExPAchievementRegistry extends AbstractRegistry
{

	public ExPAchievementRegistry()
	{
		super();
	}

	@Override
	public void postInit(FMLPostInitializationEvent evt)
	{
		super.postInit(evt);
		ExPAchievements.achievementPageExP = new AchievementPage("ExP");
		AchievementPage.registerAchievementPage(ExPAchievements.achievementPageExP);
	}

}
