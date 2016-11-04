/*
 *   Copyright (C) 2016 GeorgH93
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package at.pcgamingfreaks;

import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

public class Utils
{
	/**
	 * Converts a byte array into a hex string.
	 *
	 * @param bytes The byte array to convert to hex.
	 * @return The hex string matching the given byte array. The chars a-f will be lower case!
	 */
	public static String byteArrayToHex(@Nullable byte[] bytes)
	{
		if(bytes == null || bytes.length == 0) return "";
		StringBuilder hexBuilder = new StringBuilder(bytes.length * 2);
		for(byte b: bytes)
		{
			hexBuilder.append(String.format("%02x", b));
		}
		return hexBuilder.toString();
	}

	/**
	 * Limits the length of a given string to a given amount of characters.
	 *
	 * @param text      The text that should be limited in it's length.
	 * @param maxLength The max amount of characters the text should be limited to.
	 * @return The text in it's limited length.
	 */
	public static String limitLength(@NotNull String text, int maxLength)
	{
		Validate.notNull(text, "The text must not be null.");
		Validate.isTrue(maxLength >= 0, "The max length must not be negative!");
		if(text.length() == 0 || maxLength == 0) return "";
		if(text.length() <= maxLength) return text; // No need to create a new object if the string has not changed
		return text.substring(0, maxLength -1);
	}

	/**
	 * Shows a warning message if the Java version is still 1.7.
	 *
	 * @param logger The logger to output the warning
	 */
	public static void warnOnJava_1_7(@NotNull Logger logger)
	{
		warnOnJava_1_7(logger, 0);
	}

	/**
	 * Shows a warning message if the Java version is still 1.7.
	 *
	 * @param logger The logger to output the warning
	 * @param pauseTime The time in seconds the function should be blocking (in seconds) if Java is outdated. Values below 1 wont block.
	 */
	public static void warnOnJava_1_7(@NotNull Logger logger, int pauseTime)
	{
		Validate.notNull(logger, "The logger must not be null.");
		if (System.getProperty("java.version").startsWith("1.7"))
		{
			logger.warning(ConsoleColor.RED + "You are still using Java 1.7. Java 1.7 ist EOL for over a year now! You should really update to Java 1.8!" + ConsoleColor.RESET);
			logger.info(ConsoleColor.YELLOW + "For now this plugin will still work fine with Java 1.7 but no warranty that this won't change in the future." + ConsoleColor.RESET);
			blockThread(pauseTime);
		}
	}

	/**
	 * Blocks the thread for a given time
	 *
	 * @param pauseTime The time in seconds that the thread should be blocked
	 */
	public static void blockThread(int pauseTime)
	{
		if(pauseTime > 0) // If there is a valid time we pause the current thread for that time
		{
			try
			{
				Thread.sleep(pauseTime * 1000L);
			}
			catch(InterruptedException ignored) {}
		}
	}

	public static boolean stringArrayContains(@NotNull String[] strings, @Nullable String searchFor)
	{
		Validate.notNull(strings);
		for(String s : strings)
		{
			//noinspection ConstantConditions
			if((s != null && s.equals(searchFor)) || (s == null && searchFor == null))
			{
				return true;
			}
		}
		return false;
	}

	public static boolean stringArrayContainsIgnoreCase(@NotNull String[] strings, @Nullable String searchFor)
	{
		Validate.notNull(strings);
		for(String s : strings)
		{
			//noinspection ConstantConditions
			if((s != null && s.equalsIgnoreCase(searchFor)) || (s == null && searchFor == null))
			{
				return true;
			}
		}
		return false;
	}

	@Contract("_, null -> false")
	public static boolean stringArrayContainsAnyIgnoreCase(@NotNull String[] strings, @Nullable String... searchFor)
	{
		if(searchFor != null && searchFor.length > 0)
		{
			for(String str : searchFor)
			{
				if(stringArrayContainsIgnoreCase(strings, str))
				{
					return true;
				}
			}
		}
		return false;
	}

	@Contract("_, null -> false")
	public static boolean stringArrayContainsAny(@NotNull String[] strings, @Nullable String... searchFor)
	{
		if(searchFor != null && searchFor.length > 0)
		{
			for(String str : searchFor)
			{
				if(stringArrayContains(strings, str))
				{
					return true;
				}
			}
		}
		return false;
	}

	public static @NotNull List<String> getAllContainingStrings(@NotNull String[] source, @NotNull String searchFor)
	{
		Validate.notNull(source);
		Validate.notNull(searchFor);
		List<String> result = new LinkedList<>();
		for(String str : source)
		{
			//noinspection ConstantConditions
			if(str != null && str.contains(searchFor))
			{
				result.add(str);
			}
		}
		return result;
	}

	public static @NotNull List<String> getAllContainingStringsIgnoreCase(@NotNull String[] source, @NotNull String searchFor)
	{
		Validate.notNull(source);
		Validate.notNull(searchFor);
		List<String> result = new LinkedList<>();
		for(String str : source)
		{
			//noinspection ConstantConditions
			if(str != null && stringContainsIgnoreCase(str, searchFor))
			{
				result.add(str);
			}
		}
		return result;
	}

	public static boolean stringContainsIgnoreCase(@NotNull String string, @NotNull String searchFor)
	{
		Validate.notNull(string);
		Validate.notNull(searchFor);
		return string.toLowerCase().contains(searchFor.toLowerCase());
	}

	/**
	 * Escapes special characters to allow the string to be placed inside a json string (e.g. to replace a text within an already built json).
	 *
	 * @param string The string to be escaped.
	 * @return The escaped string.
	 */
	public static String escapeJsonString(String string)
	{
		return string.replaceAll("\\\\", "\\\\\\\\").replaceAll("\"", "\\\\\"");
	}
}