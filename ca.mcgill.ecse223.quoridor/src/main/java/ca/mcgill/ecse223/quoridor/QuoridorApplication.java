<<<<<<< HEAD

package ca.mcgill.ecse223.quoridor;

import ca.mcgill.ecse223.quoridor.model.Quoridor;
import ca.mcgill.ecse223.quoridor.view.QuoridorView;
=======
package ca.mcgill.ecse223.quoridor;

import ca.mcgill.ecse223.quoridor.model.Quoridor;
>>>>>>> iteration-4

public class QuoridorApplication {

	private static Quoridor quoridor;
<<<<<<< HEAD
	private static QuoridorView view;
	
	public static void main(String[] args) {
		// Start UI
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                view = new QuoridorView();
                view.setVisible(true);
                view.initLoadScreen();
            }
        });
	}
=======
>>>>>>> iteration-4

	public static Quoridor getQuoridor() {
		if (quoridor == null) {
			quoridor = new Quoridor();
		}
 		return quoridor;
	}

}
