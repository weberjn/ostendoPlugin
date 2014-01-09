/**
 * 
 */
package de.jwi.ostendoplugin.idl;

import java.util.List;


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
 * @author Juergen Weber
 * created 13.03.2011
 *
 */
public class IDL implements Comparable<IDL>
{
	public String name;

	public List <IDLInterface> idlInterfaces;
	
	public IDL(String name, List <IDLInterface> idlInterfaces)
	{
		super();
		this.name = name;
		this.idlInterfaces = idlInterfaces;
		
		for (IDLInterface idlInterface: this.idlInterfaces)
		{
			idlInterface.idl = this;
		}
	}

	
	@Override
	public String toString()
	{
		return name;
	}

	

	@Override
	public boolean equals(Object obj)
	{
		if (obj == null)
		{
			return false;
		}
		if (!(obj instanceof IDL))
		{
			return false;
		}

		return name.equals(((IDL)obj).name);
	}


	@Override
	public int compareTo(IDL o)
	{
		return name.compareTo(o.name);
	}
	
	
}
