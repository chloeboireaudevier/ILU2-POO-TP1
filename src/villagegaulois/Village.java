package villagegaulois;

import personnages.Chef;
import personnages.Gaulois;

public class Village {
	private String nom;
	private Chef chef;
	private Gaulois[] villageois;
	private int nbVillageois = 0;
	private Marche marche;

	public Village(String nom, int nbVillageoisMaximum, int nbEtals) {
		this.nom = nom;
		villageois = new Gaulois[nbVillageoisMaximum];
		marche = new Marche(nbEtals);
	}
	
	private static class Marche{
		private Etal[] etals;
		
		private Marche(int nbEtals) {
			etals = new Etal[nbEtals];
			for(int i = 0;i<nbEtals;i++) {
				etals[i] = new Etal();//TODO
			}
		}
		
		private void utiliserEtal(int indiceEtal, Gaulois vendeur, String produit, int nbProduit) {
			this.etals[indiceEtal].occuperEtal(vendeur, produit, nbProduit);
		}
		
		private int trouverEtalLibre() {
			int i = 0;
			while(i < this.etals.length && etals[i].isEtalOccupe()) {
				i++;
			}
			if (!etals[i].isEtalOccupe()) {
				return i;
			} else {
				return -1;
			}
			
		}
		
		private Etal[] trouverEtals(String produit) {
			int nbProduits = 0;
			for(int i = 0; i < etals.length; i ++) {
				if (etals[i].isEtalOccupe() && etals[i].contientProduit(produit)) {
					nbProduits++;
				}
			}
			
			Etal[] etalsContenantProduit = new Etal[nbProduits];
			int indice = 0;
			for(int i = 0; i < etals.length; i ++) {
				if (etals[i].isEtalOccupe() && etals[i].contientProduit(produit)) {
					etalsContenantProduit[indice] = etals[i];
					indice++;
				}
			}
			return etalsContenantProduit;
			
		}
		
		private Etal trouverVendeur(Gaulois gaulois) {
			int i = 0;
			while(i<etals.length && etals[i].isEtalOccupe()&& etals[i].getVendeur()!=gaulois) {
				i++;
			}
			if (etals[i].isEtalOccupe()&& etals[i].getVendeur().getNom().equals(gaulois.getNom())) {
				return etals[i];
			}else {
				return null;
			}
		}
		
		private String afficherMarche() {
			StringBuilder leMarche = new StringBuilder();
			int nbNonUtil = 0;
			for(int i = 0; i<etals.length;i++) {
				if(etals[i].isEtalOccupe()) {
					leMarche.append(etals[i].afficherEtal());
					leMarche.append("\n");
				}else {
					nbNonUtil++;
				}
			}
			if (nbNonUtil > 0) {
				leMarche.append("Il reste "+nbNonUtil+" étals non utilisés dans le marché.\n");
			}
			return leMarche.toString();
		}
	}

	public String getNom() {
		return nom;
	}

	public void setChef(Chef chef) {
		this.chef = chef;
	}

	public void ajouterHabitant(Gaulois gaulois) {
		if (nbVillageois < villageois.length) {
			villageois[nbVillageois] = gaulois;
			nbVillageois++;
		}
	}

	public Gaulois trouverHabitant(String nomGaulois) {
		if (nomGaulois.equals(chef.getNom())) {
			return chef;
		}
		for (int i = 0; i < nbVillageois; i++) {
			Gaulois gaulois = villageois[i];
			if (gaulois.getNom().equals(nomGaulois)) {
				return gaulois;
			}
		}
		return null;
	}

	public String afficherVillageois() {
		StringBuilder chaine = new StringBuilder();
		if (nbVillageois < 1) {
			chaine.append("Il n'y a encore aucun habitant au village du chef "
					+ chef.getNom() + ".\n");
		} else {
			chaine.append("Au village du chef " + chef.getNom()
					+ " vivent les légendaires gaulois :\n");
			for (int i = 0; i < nbVillageois; i++) {
				chaine.append("- " + villageois[i].getNom() + "\n");
			}
		}
		return chaine.toString();
	}
	
	public String installerVendeur(Gaulois vendeur, String produit, int nbProduit) {
		int indice = marche.trouverEtalLibre();
		StringBuilder chaine = new StringBuilder();
		chaine.append(vendeur.getNom()+" cherche un endroit pour vendre "+nbProduit+" "+produit+".\n");
		if (indice != -1) {
			marche.utiliserEtal(indice, vendeur, produit, nbProduit);
			chaine.append("Le vendeur "+vendeur.getNom()+" vend des "+produit+" à l'étal n°"+(indice+1)+".\n");
		} else {
			chaine.append("Il n'y a plus de place !\n");
		}
		return chaine.toString();
	}
	
	public String rechercherVendeursProduit(String produit) {
		Etal[] etals = marche.trouverEtals(produit);
		int length = etals.length;
		StringBuilder chaine = new StringBuilder();
		switch (length) {
		case 0:
			chaine.append("Il n'y a pas de vendeur qui propose des "+produit+" au marché.\n");
			break;
		case 1:
			chaine.append("Seul le vendeur "+etals[0].getVendeur().getNom()+" propose des "+produit+" au marché.\n");
			break;
		default:
			chaine.append("Les vendeurs qui proposent des "+produit+" sont :\n");
			for (int i = 0; i< length;i++) {
				chaine.append("- "+etals[i].getVendeur().getNom()+"\n");
			}
			break;
		}
		return chaine.toString();
	}
	
	public Etal rechercherEtal(Gaulois vendeur) {
		return marche.trouverVendeur(vendeur);
	}
	
	public String partirVendeur(Gaulois vendeur) {
		Etal etal = rechercherEtal(vendeur);
		StringBuilder chaine = new StringBuilder();
		if (etal == null) {
			chaine.append("Le vendeur "+vendeur.getNom()+" n'était même pas installé !\n");
			return chaine.toString();
		}else {
			return etal.libererEtal();
		}
	}
	
	public String afficherMarche() {
		StringBuilder chaine = new StringBuilder();
		chaine.append("Dans le marché du village \""+this.nom+"\" possède plusieurs étals :\n");
		chaine.append(marche.afficherMarche());
		return chaine.toString();
	}
}