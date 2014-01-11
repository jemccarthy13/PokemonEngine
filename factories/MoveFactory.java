package factories;

import moves.SandAttack;
import moves.Scratch;
import moves.Tackle;
import data_structures.Move;

public class MoveFactory {
	public static Move PLACEHOLDER = new Move("NONE", 0);
	public static Scratch SCRATCH = new Scratch();
	public static Tackle TACKLE = new Tackle();
	public static SandAttack SANDATTACK = new SandAttack();
}