package ummisco.gama.extensions.comokit.commands;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jface.dialogs.MessageDialog;

import msi.gama.kernel.model.IModel;
import msi.gama.lang.gaml.validation.GamlModelBuilder;
import msi.gama.runtime.GAMA;
import msi.gama.runtime.exceptions.GamaRuntimeException;
import msi.gaml.compilation.GamlCompilationError;
import ummisco.gama.ui.utils.WorkbenchHelper;

public class GenerateGISEnvironment extends AbstractHandler{

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		String path = DefineCOMOKITLocation.COMOKIT_PATH + "\\Utilities\\Generate GIS Data.gaml";
		File f = new File(path);
		if (!f.exists()) {
			MessageDialog.openError(WorkbenchHelper.getShell(),"Error",  path + " not found, use the \"Locate COMOKIT\" item in the COMOKIT model to set the path to COMOKIT");
			return null;
		}
		final List<GamlCompilationError> errors = new ArrayList<>();
		final IModel model = GamlModelBuilder.getDefaultInstance().compile(URI.createFileURI(path), errors);
		GAMA.runGuiExperiment("wizard_xp", model);
		return null;
	}

}
