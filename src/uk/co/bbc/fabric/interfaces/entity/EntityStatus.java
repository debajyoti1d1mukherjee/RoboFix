package uk.co.bbc.fabric.interfaces.entity;


public final class EntityStatus {
	/*
	 * CANNOT_PROCESS means that the data is such that without a change in code, it can never be processed.
	 */
	public static final EntityStatus TO_PROCESS = new EntityStatus("TO_PROCESS");
	public static final EntityStatus COMPLETED = new EntityStatus("COMPLETED");
	public static final EntityStatus FAILED = new EntityStatus("FAILED");
	public static final EntityStatus CANNOT_PROCESS = new EntityStatus("CANNOT_PROCESS");
	public static final EntityStatus RETRY_ON_DATA = new EntityStatus("RETRY_ON_DATA");

	// for when not definitely known to be FAILED despite processing problem, and have exhausted max allowed processing attempts
	public static final EntityStatus PARKED = new EntityStatus("PARKED");

	private String value;

	@SuppressWarnings("unused")
	private EntityStatus() {
		this(null);
	}

	public EntityStatus(String value) {
		this.value = value;
	}

	public String name() {
		return value;
	}

	public String getValue() {
		return value;
	}

	void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}

	@Override
	public int hashCode() {
		return value == null ? 0 : value.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || !o.getClass().equals(EntityStatus.class)) {
			return false;
		}
		EntityStatus that = (EntityStatus) o;
		return o.equals(that);
	}

}
