package v0id.core.cfg;

import v0id.core.settings.VCSettings;
import v0id.core.util.cfg.SerializableConfig;

import java.io.File;

public class CVCfg extends SerializableConfig<VCSettings>
{
	public CVCfg(File f)
	{
		super(f);
	}

	@Override
	public VCSettings provideInstance()
	{
		return new VCSettings();
	}
	
}
