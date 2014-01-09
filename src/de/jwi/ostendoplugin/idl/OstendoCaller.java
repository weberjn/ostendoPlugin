/**
 * 
 */

package de.jwi.ostendoplugin.idl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;

import de.jwi.ostendo.Ostendo;


/*
 * OstendoPlugin - An Eclipse Pluing for the Ostendo CORBA IIOP Message Analyzer
 * 
 * Copyright (C) 2011 Juergen Weber
 * 
 * This file is part of OstendoPlugin.
 * 
 * OstendoPlugin is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * OstendoPlugin is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with OstendoPlugin.  If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * @author Juergen Weber created 15.03.2011
 * 
 */
public class OstendoCaller
{
	public static OstendoOutputInput callOstendo(String typeId, File idl, File requestmessage,
			File replymessage)
	{
		String[] args = null;
		
		if (replymessage != null) 
		{
			args = new String[4];
			args[3] = replymessage.toString();
		}
		else
		{
			args = new String[3];
		}
		
		args[0] = typeId;
		args[1] = idl.toString();
		args[2] = requestmessage.toString();
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		
		PrintStream ps = new PrintStream(bos); 
		PrintStream stdout = System.out;

		try
		{
			System.setOut(ps);
			Ostendo.main(args);
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
		finally
		{
			System.setOut(stdout);
		}
		
		byte[] data = bos.toByteArray();
		
		OstendoOutputStorage storage = new OstendoOutputStorage(data, requestmessage.getName()+".xml");
		
		OstendoOutputInput input = new OstendoOutputInput(storage);
		
		return input;
	}
}
