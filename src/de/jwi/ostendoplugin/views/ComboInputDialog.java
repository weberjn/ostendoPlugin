/**
 * 
 */

package de.jwi.ostendoplugin.views;


import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

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
 * created 27.03.2011
 *
 */

public class ComboInputDialog extends InputDialog
{
	String[] data;
	
	public ComboInputDialog(Shell parentShell, String dialogTitle,
			String message, String initialValue, String[] data,
			IInputValidator validator)
	{
		super(parentShell, dialogTitle, message, initialValue, validator);
		this.data = data;
	}

	@Override
	protected Control createDialogArea(Composite parent)
	{
		Composite composite = (Composite) super.createDialogArea(parent);
		
		ComboViewer comboViewer = new ComboViewer(composite, 0);
		comboViewer.add(data);
		
		return composite;
	}

}
