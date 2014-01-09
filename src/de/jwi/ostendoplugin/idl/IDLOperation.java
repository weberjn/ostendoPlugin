/**
 * 
 */
package de.jwi.ostendoplugin.idl;

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
 * created 12.03.2011
 *
 */
public class IDLOperation
{
	public IDLInterface idlInterface;
	public String name;
	
	public IDLOperation(IDLInterface idlInterface, String name)
	{
		super();
		this.idlInterface = idlInterface;
		this.name = name;
	}

	@Override
	public String toString()
	{
		return name;
	}
	
	
}
