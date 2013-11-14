package it.privato.Giovanni;
public class Main {

	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new Finestra();
			}
		});
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			public void run() {
				System.out.println("Sto per chiudere...");
			}
		}, "Shutdown-thread"));
}
}
