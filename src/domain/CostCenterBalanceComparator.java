package domain;

import java.util.Comparator;

public class CostCenterBalanceComparator implements Comparator<CostCenter> {

	@Override
	public int compare(CostCenter k1, CostCenter k2) {
		return Double.compare(k2.getBalance(), k1.getBalance());
	}

}
