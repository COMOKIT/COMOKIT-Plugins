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
import org.eclipse.ui.internal.ide.IDEWorkbenchPlugin;

import msi.gama.application.workspace.WorkspacePreferences;
import msi.gama.runtime.GAMA;
import msi.gama.runtime.exceptions.GamaRuntimeException;
import msi.gama.util.GamaListFactory;
import msi.gama.util.IList;
import msi.gama.util.IMap;
import msi.gaml.operators.System;

public class CreateCaseStudy extends AbstractHandler{

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		if (DefineCOMOKITLocation.COMOKIT_PATH == null) {
			GamaRuntimeException.error("COMOKIT Template is not found, use the \"Locate COMOKIT\" item in the COMOKIT model to set the path to COMOKIT", null);
		}
		File f = new File(DefineCOMOKITLocation.COMOKIT_PATH );
		if (!(f.exists() && f.isDirectory())) {
			GamaRuntimeException.error("COMOKIT Template is not found, use the \"Locate COMOKIT\" item in the COMOKIT model to set the path to COMOKIT", null);
		}
		File folder = new File(f.getParent() + "\\COMOKIT Template Project");
		File workspace = new File(WorkspacePreferences.getSelectedWorkspaceRootLocation());
		if (!folder.exists())
		{
			GamaRuntimeException.error("Problem in the copy of the comokit template in the current workspace", null);
			
		}
		IList l = GamaListFactory.create();
		l.add(System.enterValue(GAMA.getRuntimeScope(), "Name of the new case stuty", "New case study"));
		IMap<String,Object> result = System.userInput(GAMA.getRuntimeScope(), l);
		if (result == null) return null;
		String projectName = (String) result.get("Name of the new case stuty");
		if (projectName == "") {
			GamaRuntimeException.error("The case study name should not be nil", null);
		}
		File destination = new File(WorkspacePreferences.getSelectedWorkspaceRootLocation() + "\\"+ projectName);
		if (!destination.isDirectory()) {
			GamaRuntimeException.error("Problem with the name of the new project", null);
		}
		if (destination.exists()) {
			GamaRuntimeException.error(destination + " already exists", null);
		}
		
		
		try {
			FileUtils.copyDirectory(folder, destination);
		} catch (IOException e1) {
			GamaRuntimeException.error("Problem in the copy of the comokit template in the current workspace: " + e1, null);
			
		}
		IPath path = new Path(destination.getAbsolutePath() + "\\.project");
		IProjectDescription description;
		try {
			description = IDEWorkbenchPlugin.getPluginWorkspace().loadProjectDescription(path);
			final IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
			
			project.create(description, null);
			project.open(IResource.BACKGROUND_REFRESH, null);
			
		} catch (CoreException e) {
			GamaRuntimeException.error("Problem in the opening of the" + projectName + " project: " + e, null);
			
		}
		
		return null;
	}

}
