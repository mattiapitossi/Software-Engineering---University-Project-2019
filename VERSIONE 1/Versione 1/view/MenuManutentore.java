package view;

import java.util.regex.Pattern;
import model.CategoriaSensori;
import model.ListaCategoriaSensori;
import model.UnitaImmobiliare;
import utility.InputDati;
import utility.MyMenu;

public class MenuManutentore {
    final private static String TITOLO = "Sistema domotico";
    final private static String [] VOCIMENU = {"Crea categoria sensore", "Crea nuova categoria attuatore", "Crea nuovo sensore (richiede la presenza di almeno una categoria)", "Crea nuovo attuatore (richiede la presenza di almeno una categoria)","Crea nuova unita' immobiliare","Descrivi unita' immobiliare" };
    final private static String MESS_USCITA = "Vuoi tornare al menu precedente ?";
    final private static String ERRORE_FUNZIONE = "La funzione non rientra tra quelle disponibili !";
    final private static String MESS_ALTRA_OPZIONE = "Selezionare un'altra opzione.";
    private int alreadyCreatedUnit = 0;
    private UnitaImmobiliare unitaImmobiliare;
    


    public void esegui(){
      MyMenu menuMain = new MyMenu(TITOLO, VOCIMENU);
      boolean fineProgramma = false;
      do{
        int selezione = menuMain.scegli();
        fineProgramma = eseguiFunzioneScelta(selezione);
	    } while (!fineProgramma);
    }
    

    public boolean eseguiFunzioneScelta(int numFunzione) 
    {
   
      switch (numFunzione) {
        case 0: // Esci
          return InputDati.yesOrNo(MESS_USCITA);
          //break; // ! Superfluo e non solo ... (non compila)
    
        case 1: // Crea nuova categoria sensore
            String nomeCategoriaSensori;
            do{
                nomeCategoriaSensori = InputDati.leggiStringaNonVuota("Inserisci il nome della categoria dei sensori: ");
                if(ListaCategoriaSensori.getInstance().alreadyExists(nomeCategoriaSensori)){
                    System.out.println("Attenzione! Il nome di questa categoria è già presente!");
                }
            }while(ListaCategoriaSensori.getInstance().alreadyExists(nomeCategoriaSensori));
            String descrizioneCategoriaSensori = InputDati.leggiStringa("Inserisci una descrizione (facoltativa): ");
            //per questa versione una singola variabile fisica
            String variabileFisicaLetta = InputDati.leggiStringaNonVuota("Inserisci la variabile fisica che verrà rilevata: ");
            CategoriaSensori categoriaCreata = new CategoriaSensori(nomeCategoriaSensori, descrizioneCategoriaSensori, variabileFisicaLetta);
            ListaCategoriaSensori.getInstance().addToList(nomeCategoriaSensori, categoriaCreata);
            
            break;
    
        case 2: // Crea nuova categoria attuatore
          String nomeCategoriaAttuatori;

          break;

        case 3: 
        /*
         Crea nuovo sensore (solo se esiste almeno una categoria di sensore 
         e può essere associato solo a stanze che non abbiano già il medesimo sensore)
        */
            //espressione regolare per il formato richiesto
            final Pattern pattern = Pattern.compile("[A-Za-z]+_[A-Za-z]+");
            String nomeSensore;
            do{
                nomeSensore = InputDati.leggiStringaNonVuota("Inserisci nome sensore (formato: nome_categoriadelsensore): ");
                if (!pattern.matcher(nomeSensore).matches()) {
                    System.out.println("Il nome del sensore non è nel formato corretto!");
                }
            } while(!pattern.matcher(nomeSensore).matches());
            break;

        case 4: // Crea nuovo attuatore (solo se esiste almeno una categoria di attuatore)
          break;

        case 5: // Crea nuova unita' immobiliare (unica per questa versione, verificare che non sia già presente)
          boolean finitoStanze = false;
          boolean finitoArtefatti = false;
          if(alreadyCreatedUnit == 1){
            System.out.println("Unita' immobiliare gia' creata!");
          } else {
            unitaImmobiliare = new UnitaImmobiliare();
            String nomeUnita = InputDati.leggiStringa("Inserisci nome unita' immobiliare: ");
            unitaImmobiliare.setNomeUnita(nomeUnita);
            if(unitaImmobiliare.aggiungiStanza(InputDati.leggiStringa("Inserisci il nome della prima stanza: "))) 
              System.out.println("Stanza aggiunta correttamente");
            do{
              String nomeStanzeSuccessive = InputDati.leggiStringa("Inserisci il nome della stanza(fine per uscire): ");
              if(nomeStanzeSuccessive.equals("fine")) finitoStanze = true;
              else if(unitaImmobiliare.aggiungiStanza(nomeStanzeSuccessive)) 
                System.out.println("Stanza aggiunta correttamente");
            } while(!finitoStanze);
            if(unitaImmobiliare.aggiungiArtefatto(InputDati.leggiStringa("Inserisci il nome del primo artefatto: "))) 
            System.out.println("Artefatto aggiunto correttamente");
            do{
              String nomeArtefattiSuccessivi = InputDati.leggiStringa("Inserisci il nome dell'artefatto(fine per uscire): ");
              if(nomeArtefattiSuccessivi.equals("fine")) finitoArtefatti = true;
              else if(unitaImmobiliare.aggiungiArtefatto(nomeArtefattiSuccessivi)) 
                System.out.println("Artefatto aggiunto correttamente");
            } while(!finitoArtefatti);
            alreadyCreatedUnit = 1;
            System.out.println("La creazione dell'unita' immobiliare e' completata!");
          }
          
          break;

        case 6: // Descrivi unità immobiliare (verificare che sia stata creata e che ci sia almeno un sensore/attuatore) 
          if(alreadyCreatedUnit == 1){
            //seleziona stanze
            unitaImmobiliare.toStringListaStanze();
            int choice = InputDati.leggiIntero("Seleziona numero della stanza da associare ad un sensore: ", 1, unitaImmobiliare.arrayStanzeSize()+1);
            System.out.println("Hai scelto " + unitaImmobiliare.getElementInListaStanze(choice-1));
          } else {
            System.out.println("Prima devi creare un'unita' immobiliare!");;
          }
          break;
    
        default: // Se i controlli nella classe Menu sono corretti, questo non viene mai eseguito !
          System.out.println(ERRORE_FUNZIONE);
          System.out.println(MESS_ALTRA_OPZIONE);
          break;
      }
  
      return false;
  
    }
    
}