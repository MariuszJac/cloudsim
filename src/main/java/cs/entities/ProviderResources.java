package cs.entities;

public class ProviderResources {
	private int coresNumber;
	private int ramCapacity; //in GB
	
	public ProviderResources(int coresNumber, int ramCapacity) {
		super();
		this.coresNumber = coresNumber;
		this.ramCapacity = ramCapacity;
	}

	public int getCoresNumber() {
		return coresNumber;
	}

	public void setCoresNumber(int coresNumber) {
		this.coresNumber = coresNumber;
	}

	public int getRamCapacity() {
		return ramCapacity;
	}

	public void setRamCapacity(int ramCapacity) {
		this.ramCapacity = ramCapacity;
	}
	
}
