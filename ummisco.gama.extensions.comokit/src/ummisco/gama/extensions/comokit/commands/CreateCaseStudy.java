package ummisco.gama.extensions.comokit.commands;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.internal.ide.IDEWorkbenchPlugin;

import msi.gama.application.workspace.WorkspacePreferences;
import msi.gama.runtime.GAMA;
import msi.gama.util.GamaListFactory;
import msi.gama.util.IList;
import msi.gama.util.IMap;
import msi.gaml.operators.System;
import ummisco.gama.ui.utils.WorkbenchHelper;

public class CreateCaseStudy extends AbstractHandler{

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		if (DefineCOMOKITLocation.COMOKIT_PATH == null) {
			MessageDialog.openError(WorkbenchHelper.getShell(), "Error", "\"COMOKIT Template is not found, use the \\\"Locate COMOKIT\\\" item in the COMOKIT model to set the path to COMOKIT\"");
			return null;
		}
		File f = new File(DefineCOMOKITLocation.COMOKIT_PATH );
		if (!(f.exists() && f.isDirectory())) {
			
			MessageDialog.openError(WorkbenchHelper.getShell(),"Error",  "COMOKIT Template is not found, use the \"Locate COMOKIT\" item in the COMOKIT model to set the path to COMOKIT");
			return null;
		}
		File folder = new File(f.getParent() + "\\COMOKIT Template Project");
		File workspace = new File(WorkspacePreferences.getSelectedWorkspaceRootLocation());
		if (!folder.exists())
		{
			MessageDialog.openError(WorkbenchHelper.getShell(),"Error",  "Problem in the copy of the comokit template in the current workspace");
			return null;
		}
		IList l = GamaListFactory.create();
		l.add(System.enterValue(GAMA.getRuntimeScope(), "Name of the new case stuty", "New case study"));
		IMap<String,Object> result = System.userInputDialog(GAMA.getRuntimeScope(), l);
		if (result == null) return null;
		String projectName = (String) result.get("Name of the new case stuty");
		if (projectName == "") {
			MessageDialog.openError(WorkbenchHelper.getShell(),"Error",  "The case study name should not be nil");
			return null;
		}
		File destination = new File(WorkspacePreferences.getSelectedWorkspaceRootLocation() + "\\"+ projectName);
		
		if (destination.exists()) {
			MessageDialog.openError(WorkbenchHelper.getShell(),"Error",  destination + " already exists");
			return null;
		}
		
		
		try {
			FileUtils.copyDirectory(folder, destination);
		} catch (IOException e1) {
			MessageDialog.openError(WorkbenchHelper.getShell(),"Error",  "Problem in the copy of the comokit template in the current workspace: " + e1);
			return null;
		}
		IPath path = new Path(destination.getAbsolutePath() + "\\.project");
		IProjectDescription description;
		try {
			description = IDEWorkbenchPlugin.getPluginWorkspace().loadProjectDescription(path);
			final IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
			
			project.create(description, null);
			project.open(IResource.BACKGROUND_REFRESH, null);
			
		} catch (CoreException e) {
			MessageDialog.openError(WorkbenchHelper.getShell(),"Error",  "Problem in the opening of the" + projectName + " project: " + e);
			
		}
		
		return null;
	}

}
