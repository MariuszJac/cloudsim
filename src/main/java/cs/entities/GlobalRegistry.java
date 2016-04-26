package cs.entities;

import java.util.ArrayList;

public class GlobalRegistry {
	private static ArrayList<ProviderInt> listProviders = new ArrayList<ProviderInt>();

	public static void addProvider(ProviderInt provider) {
		listProviders.add(provider);
	}
	
	public static ArrayList<ProviderInt> getListProviders() {
		return listProviders;
	}

}
