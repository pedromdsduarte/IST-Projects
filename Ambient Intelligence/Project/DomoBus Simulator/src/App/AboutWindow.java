package App;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;



public class AboutWindow extends Dialog {

	protected Object result;
	protected Shell shlAbout;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public AboutWindow(Shell parent, int style) {
		super(parent, style);
		setText("About");
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();
		shlAbout.open();
		shlAbout.layout();
		Display display = getParent().getDisplay();
		while (!shlAbout.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shlAbout = new Shell(getParent(), SWT.CLOSE | SWT.TITLE);
		shlAbout.setImage(SWTResourceManager.getImage(AboutWindow.class, "/resources/domobus.png"));
		shlAbout.setSize(450, 159);
		shlAbout.setText("About");
		
		Label lblDomobus = new Label(shlAbout, SWT.NONE);
		lblDomobus.setBounds(65, 10, 150, 20);
		lblDomobus.setText("DomoBus\u00A9 Simulator ");
		
		Label lblMadeByPedro = new Label(shlAbout, SWT.NONE);
		lblMadeByPedro.setBounds(65, 36, 208, 20);
		lblMadeByPedro.setText("Made by Pedro Duarte - 78328");
		
		Label lblInstitutoSuperiorTcnico = new Label(shlAbout, SWT.NONE); 	
		lblInstitutoSuperiorTcnico.setBounds(65, 76, 223, 20);
		lblInstitutoSuperiorTcnico.setText("Instituto Superior T\u00E9cnico, Lisbon");
		
		Label lbltecnicoImg = new Label(shlAbout, SWT.NONE);
		lbltecnicoImg.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		lbltecnicoImg.setImage(SWTResourceManager.getImage(AboutWindow.class, "/resources/ist.png"));
		lbltecnicoImg.setAlignment(SWT.CENTER);
		lbltecnicoImg.setBounds(313, 10, 86, 86);

	}
}
