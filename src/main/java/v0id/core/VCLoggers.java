package v0id.core;

import v0id.core.logging.LogLevel;
import v0id.core.logging.VoidLogger;
import v0id.core.markers.StaticStorage;
import v0id.core.util.java.Reflector;

@StaticStorage
public class VCLoggers 
{
	public static final VoidLogger loggerMod = VoidLogger.createLogger(VoidApi.class, LogLevel.Finest);
	public static final VoidLogger loggerReflector = VoidLogger.createLogger(Reflector.class, LogLevel.Warning);
	public static final VoidLogger loggerErrors = VoidLogger.createLogger("VoidCoreErrors", LogLevel.Error);
	public static final VoidLogger loggerWarnings = VoidLogger.createLogger("VoidCoreWarnings", LogLevel.Warning);
	public static final VoidLogger loggerDebug = VoidLogger.createLogger("VoidCoreDebug", LogLevel.Debug);
}
