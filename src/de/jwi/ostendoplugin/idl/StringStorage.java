/**
 * 
 */

package de.jwi.ostendoplugin.idl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;

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
public class StringStorage implements IStorage
{
	private String string;

	public StringStorage(String input)
	{
		this.string = input;
	}

	public InputStream getContents() throws CoreException
	{
		return new ByteArrayInputStream(string.getBytes());
	}

	public IPath getFullPath()
	{
		return null;
	}

	public Object getAdapter(Class adapter)
	{
		return null;
	}

	public String getName()
	{
		int len = Math.min(5, string.length());
		return string.substring(0, len).concat("..."); 
	}

	public boolean isReadOnly()
	{
		return true;
	}

}
