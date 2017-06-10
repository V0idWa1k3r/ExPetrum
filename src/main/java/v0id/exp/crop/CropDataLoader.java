package v0id.exp.crop;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Lists;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.FMLCommonHandler;
import v0id.api.core.logging.LogLevel;
import v0id.api.exp.data.ExPMisc;
import v0id.api.exp.tile.crop.CropData;
import v0id.api.exp.tile.crop.EnumCrop;
import v0id.exp.ExPetrum;

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
						String val = data.get("crop").getAsString();
						if (EnumCrop.valueOf(val) == null)
						{
							ExPMisc.modLogger.log(LogLevel.Warning, "Found a JSON of unknown crop %s! That crop will be injected and attempted to be loaded.", val);
							EnumHelper.addEnum(EnumCrop.class, val, new Class[0], new Object[0]);
						}
						
						CropData cropData = serializer.fromJson(data, CropData.class);
						cropData.crop.setData(cropData);
						ExPMisc.modLogger.log(LogLevel.Debug, "Parsed crop %s from %s", cropData.crop.name(), f.getAbsolutePath());
					}
					else
					{
						ExPMisc.modLogger.log(LogLevel.Warning, "%s is a JSON file and is located within ExPetrum's crop data folder but does not contain a valid crop definition!", f.getAbsolutePath());
						continue;
					}
				}
				catch (JsonSyntaxException ex)
				{
					ExPMisc.modLogger.log(LogLevel.Error, "%s seems to be a JSON file but contains syntax errors!", ex, f.getAbsolutePath());
					continue;
				}
				catch (JsonParseException ex)
				{
					ExPMisc.modLogger.log(LogLevel.Error, "%s is not a valid JSON file, it will be skipped!", ex, f.getAbsolutePath());
					continue;
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
			int read = 0;
			List<Pair<String, String>> jsonData = Lists.newArrayList();
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
				jsonData.add(Pair.of(entry.getName(), builder.toString()));
			}
		}
		catch (Exception ex)
		{
			FMLCommonHandler.instance().raiseException(ex, "ExPetrum was unable to create crop data at your config folder! This is a severe error!", true);
		}
	}
}
