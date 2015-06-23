package de.ovgu.featureide.featurehouse.ui.handlers;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.ui.javaeditor.JavaEditor;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ltk.ui.refactoring.RefactoringWizardOpenOperation;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.ResourceUtil;
import org.eclipse.ui.texteditor.ITextEditor;

import de.ovgu.featureide.core.CorePlugin;
import de.ovgu.featureide.core.IFeatureProject;
import de.ovgu.featureide.featurehouse.refactoring.RenameTypeRefactoring;
import de.ovgu.featureide.featurehouse.refactoring.RenameMethodRefactoring;
import de.ovgu.featureide.featurehouse.refactoring.RenameRefactoring;
import de.ovgu.featureide.featurehouse.refactoring.RenameRefactoringWizard;
import de.ovgu.featureide.fm.ui.handlers.base.ASelectionHandler;
import de.ovgu.featureide.fm.ui.handlers.base.SelectionWrapper;


public class RenameHandler extends ASelectionHandler {

	@Override
	protected void singleAction(Object element) 
	{
		try {
			IFeatureProject featureProject = getFeatureProject();
			if (featureProject == null)
				return;

			RenameRefactoring refactoring;
			if (element instanceof IMethod) {
				IMethod method = (IMethod) element;

				if (method.isConstructor())
					refactoring = new RenameTypeRefactoring(method.getDeclaringType(), featureProject);
				else
					refactoring = new RenameMethodRefactoring(method, featureProject);

			} else if (element instanceof IType)
				refactoring = new RenameTypeRefactoring((IType) element, featureProject);
			else
				return;

			RenameRefactoringWizard refactoringWizard = new RenameRefactoringWizard(refactoring);
			RefactoringWizardOpenOperation op = new RefactoringWizardOpenOperation(refactoringWizard);
			op.run(getShell(), "Rename-Refactoring");
		} catch (JavaModelException e) {
		} catch (InterruptedException e) {
		}
	}

	private IFeatureProject getFeatureProject()
	{
		
//		final IResource res = (IResource) SelectionWrapper.checkClass(element, IResource.class);
//		final IFeatureProject featureProject = CorePlugin.getFeatureProject(res);
		
		
		IEditorInput fileEditorInput = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor().getEditorInput();
		final IFile file = ResourceUtil.getFile(fileEditorInput);
		if (file == null) return null;
		
		return CorePlugin.getFeatureProject(file);
	}
	
	protected Shell getShell() 
	{
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if (window == null) return null;
		return window.getShell();
	}   

//	@Override
//	public Object execute(ExecutionEvent event) throws ExecutionException {
//		final ISelection selection = HandlerUtil.getCurrentSelection(event);
//		if (selection instanceof ITextSelection) {
//		    ITextSelection selection1 = (ITextSelection) selection;
//		    System.out.println(selection1.getOffset());
//		    selection1.
//		}
//		return null;
//	}
	

}
