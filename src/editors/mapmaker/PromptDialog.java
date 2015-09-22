package editors.mapmaker;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Prompt in a dialog
 */
public class PromptDialog {
	static String ans = null;

	static void tell(String paramString1, String paramString2) {
		JDialog localJDialog = new JDialog();
		localJDialog.setLocationRelativeTo(null);
		JPanel localJPanel1 = new JPanel(new FlowLayout());
		JLabel localJLabel = new JLabel(paramString1);

		JPanel localJPanel2 = (JPanel) localJDialog.getContentPane();

		localJPanel2.setLayout(new FlowLayout());
		localJPanel2.add(localJLabel, "Center");
		localJPanel2.add(localJPanel1, "South");

		JButton localJButton = new JButton("OK");
		localJPanel1.add(localJButton);

		localJDialog.pack();
		localJDialog.setVisible(true);
		localJButton.addActionListener(new ActionListener() {
			private final JDialog val$d = new JDialog();

			public void actionPerformed(ActionEvent paramAnonymousActionEvent) {
				PromptDialog.ans = ((JButton) paramAnonymousActionEvent.getSource()).getText();
				this.val$d.dispose();
			}
		});
	}
}