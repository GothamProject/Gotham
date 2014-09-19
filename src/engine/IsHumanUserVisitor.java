package engine;

import model.ComputerUser;
import model.HumanUser;

public class IsHumanUserVisitor implements UserVisitor<Boolean> {

	@Override
	public Boolean visit(ComputerUser computer) {
		return false;
	}

	@Override
	public Boolean visit(HumanUser human) {
		return true;
	}

}
