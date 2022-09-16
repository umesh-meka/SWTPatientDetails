package com.jguru.swtpatientdetails;

import java.net.http.HttpResponse;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

/*
 * This is simple class to display SWT message bob UI for displaying the API responses.
 *
 * @author      Umesh M
 * @version     1.0
 * @since       1.0
 */

public class ResponseBox {
	private String title;
	private String responseMessage;
	private Shell shell;
	
	public ResponseBox(Shell shell) {
		super();
		this.shell = shell;
	}

	public void successResponse(String responseMessage) {
		this.title = "Success";
		this.responseMessage = responseMessage;
		
		displayMessageBox();
	}
	
	public void waringResponse(String warning) {
		this.title = "Warning";
		this.responseMessage = warning;
		
		displayMessageBox();
	}

	public void failureResponse(HttpResponse<String> response) {
		this.title = "Error";
		this.responseMessage = response.body();
		
		displayMessageBox();
	}
	
	public void exceptionHanlder(Exception e) {
		this.title = "Error";
		this.responseMessage = e.getMessage();
		
		displayMessageBox();
	}
	
	public void displayMessageBox() {
		MessageBox box = new MessageBox(shell, SWT.OK);
		box.setText(title);
		box.setMessage(responseMessage);
		box.open();
	}
}
