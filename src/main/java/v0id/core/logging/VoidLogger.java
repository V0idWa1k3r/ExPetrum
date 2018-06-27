package v0id.core.logging;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import v0id.core.markers.Default;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@SuppressWarnings("WeakerAccess")
public class VoidLogger
{
	public OutputStream[] getOuts()
	{
		return outs;
	}

	private final OutputStream[] outs;

	public LogLevel getDefaultLogLevel()
	{
		return defaultLogLevel;
	}

	private LogLevel defaultLogLevel = LogLevel.Output;

	public Map<LogLevel, Boolean> getOutputLevels()
	{
		return outputLevels;
	}

	private final Map<LogLevel, Boolean> outputLevels = Maps.newHashMap();

	public String getQualifier()
	{
		return qualifier;
	}

	private String qualifier;
	
	@Default
	public VoidLogger()
	{
		this.outs = new OutputStream[]{ System.out };
	}
	
	public VoidLogger(OutputStream[] outputStreams)
	{
		this.outs = outputStreams;
	}
	
	public void log(LogLevel level, String message, Object... objects)
	{
		if (this.isLoggingAllowed(level))
		{
			Throwable t = null;
			String msg;
			if (objects != null && objects.length > 0)
			{
				if (objects[0] instanceof Throwable)
				{
					Object[] formatData = new Object[objects.length - 1];
					System.arraycopy(objects, 1, formatData, 0, formatData.length);
					msg = String.format(message, formatData);
					t = (Throwable) objects[0];
				}
				else
				{
					msg = String.format(message, objects);
				}
			}
			else
			{
				msg = message;
			}
			
			if (!Strings.isNullOrEmpty(this.qualifier))
			{
				msg = "[" + this.qualifier + "] " + msg;
			}
			
			msg = String.format("[%s] [%s] %s", LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss")), level.name(), msg);
			byte[] msgData = msg.getBytes(StandardCharsets.UTF_8);
			for (OutputStream os : this.outs)
			{
				try
				{
					os.write(msgData);
					os.write('\n');
					os.flush();
					if (t != null)
					{
						try (PrintWriter pw = new PrintWriter(os))
						{
							t.printStackTrace(pw);
						}
					}
				}
				catch (IOException ex)
				{
					System.err.println("Could not log into OS object " + os + "!");
					ex.printStackTrace();
				}
			}
		}
	}
	
	public VoidLogger setQualifier(String s)
	{
		this.qualifier = s;
		return this;
	}
	
	public VoidLogger setupLoggingFor(LogLevel level, boolean allowed)
	{
		this.outputLevels.put(level, allowed);
		return this;
	}
	
	public VoidLogger setLevel(LogLevel level)
	{
		this.defaultLogLevel = level;
		return this;
	}
	
	public boolean isLoggingAllowed(LogLevel level)
	{
		if (this.defaultLogLevel == LogLevel.Off || level == LogLevel.Off)
		{
			return false;
		}
		
		if (this.outputLevels.containsKey(level))
		{
			return this.outputLevels.get(level);
		}
		else
		{
			return level.ordinal() >= this.defaultLogLevel.ordinal();
		}
	}
	
	public static VoidLogger createLogger(Class<?> invoker, LogLevel defaultLogLevel)
	{
		return new VoidLogger().setQualifier(invoker.getSimpleName()).setLevel(defaultLogLevel);
	}
	
	public static VoidLogger createLogger(LogLevel defaultLogLevel)
	{
		return new VoidLogger().setLevel(defaultLogLevel);
	}
	
	public static VoidLogger createLogger(String prefix, LogLevel defaultLogLevel)
	{
		return new VoidLogger().setQualifier(prefix).setLevel(defaultLogLevel);
	}
}
