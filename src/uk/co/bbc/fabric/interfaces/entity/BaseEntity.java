package uk.co.bbc.fabric.interfaces.entity;

import java.math.BigInteger;

public abstract class BaseEntity {
	public abstract  BigInteger getOnairEntityId();
	public abstract String getEntityStatus();
}
