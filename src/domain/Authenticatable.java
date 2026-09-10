package domain;

import java.io.Serializable;

public interface Authenticatable extends Serializable {
	public boolean login(String password);
}
