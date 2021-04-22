package ummisco.gama.extensions.comokit.commands;

import java.io.File;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import msi.gama.application.workspace.WorkspacePreferences;
import msi.gama.runtime.GAMA;
import msi.gama.util.GamaListFactory;
import msi.gama.util.IList;
import msi.gama.util.IMap;
import msi.gama.util.file.GamaFolderFile;
import msi.gaml.operators.System;
import msi.gaml.types.IType;
import msi.gaml.types.Types;


public class DefineCOMOKITLocation extends AbstractHandler{

	static public String COMOKIT_PATH = WorkspacePreferences.getSelectedWorkspaceRootLocation();
	
		@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		IList l = GamaListFactory.create();
		l.add(System.enterValue(GAMA.getRuntimeScope(), "COMOKIT Folder", Types.get(IType.DIRECTORY), new GamaFolderFile(GAMA.getRuntimeScope(), COMOKIT_PATH, false)));
		IMap<String,Object> result = System.userInputDialog(GAMA.getRuntimeScope(), l);
		if (result == null || (result.get("COMOKIT Folder") == null)) return null;
		String path =((GamaFolderFile) result.get("COMOKIT Folder")).getPath(null);
		if (path != null) {
			File f = new File(path);
			if (f.isDirectory()) {
				COMOKIT_PATH = path;
			}
		}
			
		return null;
	}
}
