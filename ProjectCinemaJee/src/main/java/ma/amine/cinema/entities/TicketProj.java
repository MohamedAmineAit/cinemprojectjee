package ma.amine.cinema.entities;

import org.springframework.data.rest.core.config.Projection;

@Projection(name = "ticketProj",types = Ticket.class)
public interface TicketProj {
	public Long getId();
	public String getNomClient();
	public double getPrix();
	public Integer getCodePayement();
	public boolean isReserve();
	public Place getPlace();

}
