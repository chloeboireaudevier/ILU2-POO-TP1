package histoire;
import villagegaulois.*;
import personnages.*;

public class ScenarioCasDegrade {
	public static void main(String[] args) {
		Etal etal = new Etal();
		Gaulois gaulois = null;
		etal.acheterProduit(3,gaulois);
		System.out.println("Fin du test");
	}
}
