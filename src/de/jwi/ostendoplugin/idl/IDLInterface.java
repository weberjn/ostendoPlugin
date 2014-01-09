/**
 * 
 */
package de.jwi.ostendoplugin.idl;

import java.util.ArrayList;
import java.util.List;

import org.jacorb.idl.Interface;
import org.jacorb.idl.Operation;

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
 * created 03.03.2011
 *
 */
public class IDLInterface
{
	public String name;

	public IDL idl;
	
	public List <IDLOperation> operations = new ArrayList <IDLOperation>();
	
	public IDLInterface(String name)
	{
		super();
		this.name = name;
	}


	public void addOperation(IDLOperation operation)
	{
		operations.add(operation);
	}
	
	@Override
	public String toString()
	{
		return name;
	}
	
	
}
