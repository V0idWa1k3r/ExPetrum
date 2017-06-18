package v0id.exp.crop;

import com.google.common.io.Files;
import com.google.gson.*;
import net.minecraftforge.fml.common.FMLCommonHandler;
import v0id.api.core.logging.LogLevel;
import v0id.api.exp.data.ExPMisc;
import v0id.api.exp.tile.crop.CropData;
import v0id.api.exp.tile.crop.EnumCrop;
import v0id.exp.ExPetrum;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.StandardOpenOption;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class CropDataLoader
{
	public static final Gson serializer = new Gson();
	
	public static void loadAllData()
	{
		File configLocation = new File(new File(ExPetrum.configDirectory, "ExPetrum"), "CropData");
		if (!configLocation.exists() || configLocation.listFiles(f -> f.getName().endsWith(".json")).length < EnumCrop.values().length)
		{
			createAllData(configLocation);
		}
		
		try
		{
			for (File f : configLocation.listFiles(f -> f.getName().endsWith(".json")))
			{
				String fileContents = Files.toString(f, StandardCharsets.UTF_8);
				try
				{
					JsonParser parser = new JsonParser();
					JsonObject data = parser.parse(fileContents).getAsJsonObject();
					if (data.has("crop"))
					{
						CropData cropData = serializer.fromJson(data, CropData.class);
						cropData.crop.setData(cropData);
						ExPMisc.modLogger.log(LogLevel.Debug, "Parsed crop %s from %s", cropData.crop.name(), f.getAbsolutePath());
					}
					else
					{
						ExPMisc.modLogger.log(LogLevel.Warning, "%s is a JSON file and is located within ExPetrum's crop data folder but does not contain a valid crop definition!", f.getAbsolutePath());
					}
				}
				catch (JsonSyntaxException ex)
				{
					ExPMisc.modLogger.log(LogLevel.Error, "%s seems to be a JSON file but contains syntax errors!", ex, f.getAbsolutePath());
				}
				catch (JsonParseException ex)
				{
					ExPMisc.modLogger.log(LogLevel.Error, "%s is not a valid JSON file, it will be skipped!", ex, f.getAbsolutePath());
				}
			}
		}
		catch (Exception ex)
		{
			FMLCommonHandler.instance().raiseException(ex, "ExPetrum was unable to load crop data!", true);
		}
		
		Stream.of(EnumCrop.values()).forEach(c -> { 
			assert c.getData() != null : String.format("ExPetrum was unable to load crop data for %s! Please, delete the CropData folder inside ExPetrum folder in your config folder and restart the game.", c.name());
		});
	}
	
	public static void createAllData(File f)
	{
		f.mkdirs();
		try(ZipInputStream zis = new ZipInputStream(ExPetrum.class.getResourceAsStream("/assets/exp/data/packedCropData.zip")))
		{
			ZipEntry entry;
			int read;
			while ((entry = zis.getNextEntry()) != null)
			{
				StringBuilder builder = new StringBuilder();
				byte[] buffer = new byte[1024];
				while ((read = zis.read(buffer, 0, 1024)) >= 0)
				{
					builder.append(new String(buffer, 0, read));
				}
				
				File dataFile = new File(f, entry.getName());
				if (dataFile.exists())
				{
					continue;
				}
				
				java.nio.file.Files.write(dataFile.toPath(), builder.toString().getBytes(), StandardOpenOption.CREATE_NEW);
			}
		}
		catch (Exception ex)
		{
			FMLCommonHandler.instance().raiseException(ex, "ExPetrum was unable to create crop data at your config folder! This is a severe error!", true);
		}
	}
}
