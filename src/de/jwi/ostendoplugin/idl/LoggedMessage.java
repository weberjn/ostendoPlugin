/**
 * 
 */

package de.jwi.ostendoplugin.idl;

import java.io.File;
import java.io.IOException;

import org.omg.CORBA.ORB;

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
 * @author Juergen Weber created 02.03.2012
 * 
 */
public class LoggedMessage
{
	public File file;
	public Integer messageType;
	public String op;
	
	
	public LoggedMessage(File file)
	{
		this.file = file;
	}

	public String toString()
	{
		return file.toString();
	}

	public int getMessageType()
	{
		if (messageType == null)
		{
			try
			{
				messageType = new Integer(MessageTypeChecker.getMessageType(file));
			}
			catch (IOException e)
			{
				throw new RuntimeException(e.getMessage(), e);
			}
		}
		return messageType.intValue();
	}
	
	public boolean isRequest()
	{
		int type = getMessageType();
		return type == MessageTypeChecker.REQUEST;
	}

	public boolean isReply()
	{
		int type = getMessageType();
		return type == MessageTypeChecker.REPLY;
	}

	
	public String getOperation(ORB orb)
	{
		int type = getMessageType();
		if (type != MessageTypeChecker.REQUEST)
		{
			throw new RuntimeException("Not Request");
		}
		if (op == null)
		{
			try
			{
				op = MessageTypeChecker.getOperation(orb, file);
			}
			catch (IOException e)
			{
				throw new RuntimeException(e.getMessage(), e);
			}
		}
		return op;
	}
	
}
