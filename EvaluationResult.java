public class EvaluationResult {
	public final Move move;
	public final double value;

	public EvaluationResult(Move move, double value) {
		this.move = move;
		this.value = value;
	}
}