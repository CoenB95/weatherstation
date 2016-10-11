package weatherstation;

import java.time.LocalDateTime;

public class Period {

	private LocalDateTime startDate;
	private LocalDateTime endDate;

	public Period(LocalDateTime d1,LocalDateTime d2) {
		startDate = d1;
		endDate = d2;
	}
	

	public LocalDateTime getStartDate() {
		return startDate;
	}

	public LocalDateTime getEndDate() {
		return endDate;
	}
}
