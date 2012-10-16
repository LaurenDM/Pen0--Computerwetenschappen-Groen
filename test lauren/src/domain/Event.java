package domain;



/**
 * Deze klasse stelt een event voor. Elke event die op de timeline gezet wil
 * worden moet deze klasse extenden. Omdat het een klasse en geen interface is
 * zal er voor elke event best een inner klasse gemaakt worden die Event extend
 * in de klasse die de informatie over de event heeft.
 * 
 * @invar executionTime != null
 * 
 * @author SWOP 10 *
 */
public abstract class Event implements Comparable<Event> {
	private final TimeStamp executionTime;

	/**
	 * De constructor van Event.
	 * 
	 * @param executionTime
	 * 		  Het tijdstip van de uitvoeringstijd
	 */
	public Event(TimeStamp executionTime) {
		this.executionTime = executionTime;
	}

	/**
	 * Een methode om de uitvoeringstijd op te vragen.
	 * 
	 * @return executionTime
	 * 		   De uitvoeringstijd
	 */
	public TimeStamp getExecutionTime() {
		return executionTime;
	}

	public abstract void execute();

	
	/**
	 * Een toString voor Event.
	 */
	@Override
	public String toString() {
		return getClass().getSimpleName()+","+ getExecutionTime();
	}
}
