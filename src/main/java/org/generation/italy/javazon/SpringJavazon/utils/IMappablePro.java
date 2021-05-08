package org.generation.italy.javazon.SpringJavazon.utils;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

// Quest'interfaccia la svolgeremo nello "stile" da Java 8 in avanti
// La maggior parte degli oggetti e dei metodi che utilizzeremo qua dentro
// fanno parte delle API reflect di Java
// Quindi opereremo direttamente sulla stuttura della classe
// Possiamo accedere direttamente ai metodi, costruttori e propriet� di una classe
// senza conoscerli e li manderemo anche in esecuzione
// Miglioriamo l'automazione e l'astrazione del codice.
public interface IMappablePro {

	// Questo metodo permetter� la conversione automatica da oggetto a mappa
	// Tutte le classi che implementeranno quest'interfaccia avranno questo
	// metodo gi� implementato
	default Map<String, String> toMap() {
		Map<String, String> ris = new HashMap<>();
		
		// Devo venire a conoscenza della classe dell'oggetto su cui viene
		// invocato questo metodo
		// Class � un Generics. non gli specifico il tipo.
		// ? extends IMappablePro significa che questa classe potr� essere
		// di un qualsiasi tipo che estende IMappablePro
		// Gli sto dicendo che estendendo IMappablePro mi garantir� un'uniformit�
		// base di implementazione
		Class<? extends IMappablePro> classe = getClass();
		
		// Una volta che ho recuperato la classe dell'oggetto sono in grado
		// di risalire a tutto ci� che le appartiene
		// Dalla classe recupero un vettore di metodi contenente tutti i metodi 
		// che le appartengono
		Method[] metodi = classe.getMethods();
		
		for (Method metodo : metodi) {
			// Mi serve il nome del metodo. Lo registro in una banale stringa
			String nomeMetodo = metodo.getName();
			
			// Mi interessano solamente i getters
			if (!nomeMetodo.equalsIgnoreCase("getclass") && 
					(nomeMetodo.startsWith("get") || nomeMetodo.startsWith("is"))) {
				// Devo cercare di ottenere il valore restituito da questi metodi
				try {
					// invoke() � un metodo che manda in esecuzione un metodo
					// invoke() tra le parentesi deve avere l'oggetto su cui viene
					// invocato il metodo
					
					// Invoco il metodo get che mi restituisce il valore
					// di quella propriet� -> getUfficio
					Object v = metodo.invoke(this);
					// Devo fare questo perch� se la propriet� � null
					// non posso invocare il toString
					String valore = v == null ? "" : v.toString();				
					
					// getFoo isPromosso
					int indiceDiPartenza = nomeMetodo.startsWith("get") ? 3 : 2;
					
					// con substring eliminer� il get e rimarr� Foo
					// Mi serve in minuscolo quindi toLowerCase foo
					String chiave = nomeMetodo.substring(indiceDiPartenza).toLowerCase();
					
					ris.put(chiave, valore);
				} catch (IllegalAccessException |
						IllegalArgumentException |
						InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
		
		// Voglio avere anche una posizione nella mappa contenente la classe dell'oggetto
		// entities.Dummy
		// getSimpleName ritorna solamente Dummy
		ris.put("Class", classe.getSimpleName());
		
		return ris;
	}
	
	// Da mappa ad oggetto
	default void fromMap(Map<String, String> map) {
		// Vado a registrarmi la classe dell'oggetto su cui vado ad invocare
		// il metodo
		Class<? extends IMappablePro> thisClass = getClass();
		
		// Dalla classe prendo i suoi metodi
		Method[] metodi = thisClass.getMethods();
		
		// Itero i metodi
		for (Method metodo : metodi) {
			// Del metodo prendo solo il nome. Ad esempio getNome oppure setId
			String nomeMetodo = metodo.getName();
			
			if (nomeMetodo.startsWith("set")) {
				// Elimino il set iniziale e metto tutto in minuscolo
				// setNome -> nome
				String chiave = nomeMetodo.substring(3).toLowerCase();
				
				// vado a prendere il valore associato a questa chiave
				// nella mappa
				String valore = map.get(chiave);
				
				// setNome(String nome) -> class java.util.String
				Class<?> tipoParametro = metodo.getParameterTypes()[0];
				// se il tipo � primitivo o se non lo �
				// c'� un'unica particolarit�: il char
				// Nella stringa contenuta nel valore della mappa il char
				// � salvato cos�: "A"
				// a me serve invece cos�: 'A'
				try {
					if (tipoParametro.equals(boolean.class)) {
						metodo.invoke(this, valore.equals("1") || valore.equals("true"));
					} else if (tipoParametro.equals(char.class)) {
					// Invoco il metodo setter su questo oggetto
					// charAt(0) serve per prendere il carattere contenuto nella stringa
					// "A" -> String -> vettore di char grandezza 1
					// 'A'
						metodo.invoke(this, valore.charAt(0));
					} else if (tipoParametro.isPrimitive()) {
						// Io sono pigro, non voglio fare un if/else oppure uno switch
						// Glielo faccio prendere a lui il tipo Boxato
						// se il parametro � int lui andr� a cercare il tipo boxato
						// Integer per poi prendere il metodo parseInt e glielo faccio
						// invocare da solo
						// Array.newInstance(tipoParametro, 1) crea un vettore di grandezza 1
						// contenente il tipo primitivo
						// Array.get(vettore, posizione) prende da un vettore l'elemento a quella posizione
						// e lo restituisce di tipo boxato
						// int[] numeri = {0};
						// Array.newInstance(int.class, 1);
						// Array.get -> int.class = Integer.class
 						Class<?> tipoBoxato = Array.get(Array.newInstance(tipoParametro, 1), 0).getClass();
						
 						// Dal tipo boxato vado a prendere i metodi
						Method[] metodiBoxati = tipoBoxato.getMethods();
					
						// Itero i metodi del tipo boxato
						for (Method metodoBoxato : metodiBoxati) {
							// cerco quello che inizia con parse
							if (metodoBoxato.getName().startsWith("parse") &&
									metodoBoxato.getParameterCount() == 1) {
								// invoco il metodo setter sull'oggetto e come parametro
								// gli passo il valore restuito dal metodo boxato invocato
								// su null (metodo statico, non dell'oggetto) e gli passo
								// il valore nella mappa
								// metodo -> setQualcosa
								// meotodoBoxato -> Integer.parseInt(Stringa)
								metodo.invoke(this, metodoBoxato.invoke(null, valore));
								break;
							}
						}
					} else {
						metodo.invoke(this, valore);
					}					
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					e.printStackTrace();
				}
				
			}
		}
	}
	
	static <T extends IMappablePro> T fromMap(Class<T> type, Map<String, String> map) {
        T ris = null;

        try {
            Constructor<T> constructor = type.getConstructor();
            ris = constructor.newInstance();
            ris.fromMap(map);
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
                | IllegalArgumentException | InvocationTargetException e) {
            // Do Nothing. idgaf. Just return null
            System.err.println("Manca il costruttore senza parametri, impossibile istanziare l'oggetto");
        }

        return ris;
    }
	
}