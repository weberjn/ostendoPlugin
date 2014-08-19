
package de.jwi.ostendoplugin.views;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.filesystem.IFileSystem;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IStorageEditorInput;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import de.jwi.ostendoplugin.Activator;
import de.jwi.ostendoplugin.idl.IDL;
import de.jwi.ostendoplugin.idl.IDLExtractor;
import de.jwi.ostendoplugin.idl.IDLInterface;
import de.jwi.ostendoplugin.idl.IDLOperation;
import de.jwi.ostendoplugin.idl.LoggedMessage;
import de.jwi.ostendoplugin.idl.OstendoCaller;
import de.jwi.ostendoplugin.idl.OstendoOutputInput;
import de.jwi.ostendoplugin.idl.StringInput;
import de.jwi.ostendoplugin.idl.StringStorage;

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
public class OstendoView extends ViewPart
{

	public static final String ID = "de.jwi.ostendoplugin.views.OstendoView";

	private Activator plugin;

	private TreeViewer idlViewer;

	private TreeViewer logdirViewer;

	private Action runAction;

	private Action addIDLAction;

	private Action delIDLAction;

	private Action addLogdirAction;

	private Action addFilePatternAction;

	private Action delLogdirAction;

	private Action refreshLogdirAction;

	private Action helpAction;

	private Action doubleClickAction;

	LoggedMessage currentRequest;


	LoggedMessage currentReply;

	private boolean isLogSelected;

	private IMemento memento;

	private IDLInterface selectedIdlInterface;


	public void createPartControl(Composite parent)
	{
		PlatformUI.getWorkbench().getHelpSystem()
				.setHelp(parent, Activator.PLUGIN_ID + ".help");

		SashForm sash = new SashForm(parent, SWT.VERTICAL);

		idlViewer = new TreeViewer(sash, SWT.SINGLE | SWT.H_SCROLL
				| SWT.V_SCROLL);

		logdirViewer = new TreeViewer(sash, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL);

		idlViewer.setContentProvider(new IDLContentProvider());
		idlViewer.setLabelProvider(new IDLLabelProvider(this));
		idlViewer.addSelectionChangedListener(new ISelectionChangedListener()
		{

			@Override
			public void selectionChanged(SelectionChangedEvent event)
			{
				IStructuredSelection selection = (IStructuredSelection) event
						.getSelection();

				idlSelectionChanged(selection);

			}
		});

		logdirViewer.setContentProvider(new LogViewerTreeContentProvider());
		logdirViewer.setLabelProvider(new LogViewerLabelProvider(this));

		logdirViewer
				.addSelectionChangedListener(new ISelectionChangedListener()
				{

					@Override
					public void selectionChanged(SelectionChangedEvent event)
					{
						IStructuredSelection selection = (IStructuredSelection) event
								.getSelection();

						logSelectionChanged(selection);
					}
				});

		upDateIDLViewer();
		upDateLogdirViewer();

		makeActions();
		hookContextMenu();
		// hookDoubleClickAction();
		contributeToActionBars();
	}


	private void upDateLogdirViewer()
	{
		Object[] logDirsArray = plugin.logdirs.toArray();
		logdirViewer.setInput(logDirsArray);
	}


	private void upDateIDLViewer()
	{
		Object[] idlsArray = plugin.idls.toArray();
		idlViewer.setInput(idlsArray);
	}


	private IStorageEditorInput createStorage()
	{
		String typeId = selectedIdlInterface.name;

		File idl = new File(selectedIdlInterface.idl.name);

		OstendoOutputInput input = OstendoCaller.callOstendo(typeId, idl,
				currentRequest.file, currentReply != null ? currentReply.file : null);
		return input;
	}


	private IStorageEditorInput createStorage2() throws IOException
	{
		File f = new File("D:/eclipseworkspace/Ostendo/handleNested.xml");
		BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream(f)));

		StringWriter sw = new StringWriter();
		String s = null;

		while ((s = br.readLine()) != null)
		{
			sw.append(s).append('\n');
		}

		br.close();

		s = sw.toString();

		StringStorage ss = new StringStorage(s);

		StringInput si = new StringInput(ss);

		return si;
	}


	private void openEditor()
	{
		IWorkbenchPage page = getSite().getPage();

		try
		{
			String n = currentRequest.file.getName()+".xml";
			
//			n="D:\\eclipseworkspace\\OstendoPlugin\\x.xml";
			
			n = ".xml";
			
			IEditorDescriptor editorDescriptor = IDE
					.getEditorDescriptor(n);
			String id2 = editorDescriptor.getId();

			IStorageEditorInput input;
			input = createStorage();

			IDE.openEditor(page, input, id2);
		}
		catch (PartInitException e)
		{
			e.printStackTrace();
		}
	}


	private void openEditor1()
	{
		IWorkbenchPage page = getSite().getPage();

		File f = new File("D:/eclipseworkspace/Ostendo/handleNested.xml");
		IFileSystem localFileSystem = EFS.getLocalFileSystem();
		IFileStore fileStore = localFileSystem.fromLocalFile(f);

		try
		{
			IEditorDescriptor editorDescriptor = IDE
					.getEditorDescriptor(".xml");
			String id2 = editorDescriptor.getId();

			IDE.openEditorOnFileStore(page, fileStore);
		}
		catch (PartInitException e)
		{
			throw new RuntimeException(e);
		}

	}


	private void hookContextMenu()
	{
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener()
		{
			public void menuAboutToShow(IMenuManager manager)
			{
				OstendoView.this.fillIDLViewerContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(idlViewer.getControl());
		idlViewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, idlViewer);

		MenuManager menuMgr2 = new MenuManager("#PopupMenu");
		menuMgr2.setRemoveAllWhenShown(true);
		menuMgr2.addMenuListener(new IMenuListener()
		{
			public void menuAboutToShow(IMenuManager manager)
			{
				OstendoView.this.fillLogDirViewerContextMenu(manager);
			}
		});
		Menu menu2 = menuMgr2.createContextMenu(logdirViewer.getControl());
		logdirViewer.getControl().setMenu(menu2);
		getSite().registerContextMenu(menuMgr2, logdirViewer);

	}


	private void contributeToActionBars()
	{
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDownMenu(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}


	private void fillLocalPullDownMenu(IMenuManager manager)
	{
		manager.add(runAction);
		manager.add(new Separator());
		manager.add(refreshLogdirAction);
		manager.add(new Separator());
		manager.add(addIDLAction);
		manager.add(addLogdirAction);
		manager.add(new Separator());
		manager.add(helpAction);

		// manager.add(new Separator());
		// manager.add(addFilePatternAction);
	}


	private void fillIDLViewerContextMenu(IMenuManager manager)
	{
		IStructuredSelection selection = (IStructuredSelection) idlViewer
				.getSelection();

		if (!selection.isEmpty())
		{
			Object o = selection.getFirstElement();
			if (o instanceof IDL)
			{
				manager.add(delIDLAction);
			}
		}

		manager.add(addIDLAction);
		manager.add(new Separator());
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}


	private void fillLogDirViewerContextMenu(IMenuManager manager)
	{
		IStructuredSelection selection = (IStructuredSelection) logdirViewer
				.getSelection();

		if (!selection.isEmpty())
		{
			Object o = selection.getFirstElement();

			if (plugin.logdirs.contains(o))
			{
				manager.add(refreshLogdirAction);
				manager.add(delLogdirAction);
			}
		}

		manager.add(runAction);
		manager.add(new Separator());
		manager.add(addLogdirAction);
		manager.add(new Separator());
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}


	private void fillLocalToolBar(IToolBarManager manager)
	{
		manager.add(runAction);
		manager.add(refreshLogdirAction);
		manager.add(addIDLAction);
		manager.add(addLogdirAction);
		manager.add(new Separator());
	}


	private void makeActions()
	{
		runAction = new Action()
		{
			public void run()
			{
				openEditor();
			}
		};

		refreshLogdirAction = new Action()
		{
			public void run()
			{
				logdirViewer.refresh();
			}
		};
		refreshLogdirAction.setText("Refresh LogDirs");
		refreshLogdirAction.setImageDescriptor(AbstractUIPlugin
				.imageDescriptorFromPlugin(Activator.PLUGIN_ID,
						"icons/refresh.gif"));

		runAction.setText("Run");
		runAction.setToolTipText("Run Ostendo");
		runAction.setImageDescriptor(AbstractUIPlugin
				.imageDescriptorFromPlugin(Activator.PLUGIN_ID,
						"icons/lrun_obj.gif"));
		runAction.setEnabled(false);

		addIDLAction = new Action()
		{
			public void run()
			{
				Shell shell = PlatformUI.getWorkbench().getDisplay()
						.getActiveShell();

				FileDialog dialog = new FileDialog(shell, SWT.OPEN);
				String[] filterExtensions = new String[]{"*.idl", "*"};

				dialog.setFilterExtensions(filterExtensions);

				if (plugin.lastIDLDir != null)
				{
					dialog.setFilterPath(plugin.lastIDLDir);
				}

				String filePath = dialog.open();

				if (filePath != null)
				{
					plugin.lastIDLDir = new File(filePath).getParent();

					try
					{
						addIDL(filePath);
						upDateIDLViewer();
					}
					catch (Exception e)
					{
						e.printStackTrace();

						MessageDialog.openInformation(idlViewer.getControl().getShell(),
								"Error", e.getMessage());
						
					}
				}
			}
		};
		addIDLAction.setText("Add IDL");
		addIDLAction.setToolTipText("Add IDL");
		addIDLAction.setImageDescriptor(AbstractUIPlugin
				.imageDescriptorFromPlugin(Activator.PLUGIN_ID,
						"icons/add_file_obj.gif"));

		addLogdirAction = new Action()
		{
			public void run()
			{
				Shell shell = PlatformUI.getWorkbench().getDisplay()
						.getActiveShell();

				DirectoryDialog dialog = new DirectoryDialog(shell, SWT.OPEN);
				System.out.println("lastLogDir: " + plugin.lastLogDir);
				if (plugin.lastLogDir != null)
				{
					dialog.setFilterPath(plugin.lastLogDir);
				}
				String path = dialog.open();
				if (path != null)
				{
					plugin.lastLogDir = path;
					addLogDir(path);
					upDateLogdirViewer();
				}
			}
		};
		addLogdirAction.setText("Add LogDir");
		addLogdirAction.setToolTipText("Add LogDir");
		addLogdirAction.setImageDescriptor(AbstractUIPlugin
				.imageDescriptorFromPlugin(Activator.PLUGIN_ID,
						"icons/add_folder.gif"));

		addFilePatternAction = new Action()
		{
			public void run()
			{
				Shell shell = PlatformUI.getWorkbench().getDisplay()
						.getActiveShell();

				String[] s = {"a1", "b2", "c3"};

				ComboInputDialog comboInputDialog = new ComboInputDialog(shell,
						"tit", "mes", null, s, null);
				comboInputDialog.open();

			}
		};
		addFilePatternAction.setText("Add FilePattern");
		addFilePatternAction.setToolTipText("Add Log File Pattern");

		delIDLAction = new Action()
		{
			public void run()
			{
				ISelection selection1 = idlViewer.getSelection();
				IStructuredSelection selection = ((IStructuredSelection) selection1);

				if (!selection.isEmpty())
				{
					IDL idl = (IDL) selection.getFirstElement();
					removeIDL(idl);
					upDateIDLViewer();
				}
			}
		};
		delIDLAction.setText("Remove IDL");

		delLogdirAction = new Action()
		{
			public void run()
			{
				ISelection selection1 = logdirViewer.getSelection();
				IStructuredSelection selection = ((IStructuredSelection) selection1);

				if (!selection.isEmpty())
				{
					LoggedMessage file = (LoggedMessage) selection
							.getFirstElement();
					removeLogdir(file);
					upDateLogdirViewer();
				}
			}
		};
		delLogdirAction.setText("Remove Log Dir");

		doubleClickAction = new Action()
		{
			public void run()
			{
				ISelection selection = idlViewer.getSelection();
				Object obj = ((IStructuredSelection) selection)
						.getFirstElement();
				showMessage("Double-click detected on " + obj.toString());
			}
		};

		helpAction = new Action()
		{
			public void run()
			{
				PlatformUI.getWorkbench().getHelpSystem()
						.displayHelp(Activator.PLUGIN_ID + ".help");
			}
		};
		helpAction.setText("Help");

	}


	private void hookDoubleClickAction()
	{
		idlViewer.addDoubleClickListener(new IDoubleClickListener()
		{
			public void doubleClick(DoubleClickEvent event)
			{
				doubleClickAction.run();
			}
		});
	}


	private void showMessage(String message)
	{
		MessageDialog.openInformation(idlViewer.getControl().getShell(),
				"Sample View", message);
	}


	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus()
	{
		idlViewer.getControl().setFocus();
	}


	boolean addLogDir(String path)
	{
		File f = new File(path);
		if (!f.exists() || plugin.logdirs.contains(f))
		{
			return false;
		}

		plugin.logdirs.add(new LoggedMessage(f));

		return true;
	}


	boolean addIDL(String path)
	{
		File f = new File(path);
		if (!f.exists())
		{
			return false;
		}
		List<IDLInterface> interfaces = IDLExtractor.getInstance()
				.extractInterfaces(f);
		IDL idl = new IDL(path, interfaces);
		if (plugin.idls.contains(idl))
		{
			return false;
		}
		plugin.idls.add(idl);

		for (IDLInterface ifc : interfaces)
		{
			for (IDLOperation op : ifc.operations)
			{
				plugin.opnames.put(op.name, op);

				Integer n = plugin.opnameInstanceCount.get(op.name);
				if (n == null)
				{
					n = new Integer(1);
				}
				else
				{
					n = new Integer(n.intValue() + 1);
				}
				plugin.opnameInstanceCount.put(op.name, n);
			}
		}
		return true;
	}


	void removeIDL(IDL idl)
	{
		plugin.idls.remove(idl);

		for (IDLInterface ifc : idl.idlInterfaces)
		{
			for (IDLOperation op : ifc.operations)
			{
				plugin.opnames.remove(op);

				Integer n = plugin.opnameInstanceCount.get(op.name);
				if (n.intValue() == 1)
				{
					plugin.opnameInstanceCount.remove(op.name);
				}
				else
				{
					n = new Integer(n.intValue() - 1);
					plugin.opnameInstanceCount.put(op.name, n);
				}
			}
		}
	}


	void removeLogdir(LoggedMessage file)
	{
		plugin.logdirs.remove(file);
	}


	@Override
	public void init(IViewSite site, IMemento memento) throws PartInitException
	{
		super.init(site, memento);

		this.plugin = Activator.getDefault();

		this.memento = memento;

		String s = null;
		String[] s1 = null;

		if (memento != null)
		{
			s = memento.getString("IDLS");
			if (s != null)
			{
				s1 = s.split("\\n");
				for (String s2 : s1)
				{
					addIDL(s2);
				}
			}
			s = memento.getString("LOGDIRS");
			if (s != null)
			{
				s1 = s.split("\\n");
				for (String s2 : s1)
				{
					addLogDir(s2);
				}
			}
			plugin.lastLogDir = memento.getString("LASTLOGDIR");
			plugin.lastIDLDir = memento.getString("LASTIDLDIR");
		}
	}


	@Override
	public void saveState(IMemento memento)
	{
		super.saveState(memento);

		StringWriter sw = new StringWriter();

		for (Iterator<IDL> it = plugin.idls.iterator(); it.hasNext();)
		{
			IDL idl = it.next();
			String s = idl.name;
			sw.append(s);
			if (it.hasNext())
			{
				sw.append("\n");
			}
		}
		memento.putString("IDLS", sw.toString());

		sw = new StringWriter();

		for (Iterator<LoggedMessage> it = plugin.logdirs.iterator(); it
				.hasNext();)
		{
			LoggedMessage lf = it.next();
			File f = lf.file;
			String s = f.getPath();
			sw.append(s);
			if (it.hasNext())
			{
				sw.append("\n");
			}
		}
		memento.putString("LOGDIRS", sw.toString());

		if (plugin.lastLogDir != null)
		{
			memento.putString("LASTLOGDIR", sw.toString());
		}

		if (plugin.lastIDLDir != null)
		{
			memento.putString("LASTIDLDIR", sw.toString());
		}
	}


	private void idlSelectionChanged(IStructuredSelection selection)
	{
		System.out.println("idlSelectionChanged, isLogSelected:"
				+ isLogSelected);

		if (selection.isEmpty())
		{
			selectedIdlInterface = null;
			runAction.setEnabled(false);
			return;
		}

		Object o = selection.getFirstElement();
		if (o instanceof IDL)
		{
			selectedIdlInterface = null;
			runAction.setEnabled(false);
			return;
		}

		if (o instanceof IDLInterface)
		{
			selectedIdlInterface = (IDLInterface) o;
		}
		else
		{
			// IDLOperation
			selectedIdlInterface = ((IDLOperation) o).idlInterface;
		}

		runAction.setEnabled(isLogSelected);
	}


	private void logSelectionChanged(IStructuredSelection selection)
	{
		LoggedMessage message = (LoggedMessage) selection.getFirstElement();

		// de-selected?
		if (message == null)
		{
			runAction.setEnabled(false);
			isLogSelected = false;
			idlViewer.setSelection(new StructuredSelection(new ArrayList()));
			return;
		}
		
		String fileName = message.file.getName();
		String opName = null;
		currentRequest = null;
		currentReply = null;

		IDLOperation currentOperation = null;

		int n = selection.size();

		if (n == 1)
		{
			if (message.isRequest())
			{
				opName = message.getOperation(plugin.orb);
				currentRequest = message;
				currentReply = null;
			}
		}
		else if (n == 2)
		{
			LoggedMessage message2 = (LoggedMessage) selection.toArray()[1];

			if (message.isRequest() && message2.isReply())
			{
				opName = message.getOperation(plugin.orb);
				currentRequest = message;
				currentReply = message2;
			}
			else if (message2.isRequest() && message.isReply())
			{
				opName = message2.getOperation(plugin.orb);
				currentRequest = message2;
				currentReply = message;
			}
		}

		if (currentRequest != null)
		{
			isLogSelected = true;

			currentOperation = plugin.opnames.get(opName);

			if (currentOperation != null)
			{
				Integer ic = plugin.opnameInstanceCount
						.get(currentOperation.name);
				if (ic.intValue() == 1)
				{
					ArrayList l = new ArrayList();
					l.add(currentOperation);
					idlViewer.setSelection(new StructuredSelection(l));
				}
			}
			else
			{
				idlViewer
						.setSelection(new StructuredSelection(new ArrayList()));
			}
		}
		else
		{
			isLogSelected = false;

			idlViewer.setSelection(new StructuredSelection(new ArrayList()));
		}
		System.out.println("logSelectionChanged done, isLogSelected: "
				+ isLogSelected);
	}

}