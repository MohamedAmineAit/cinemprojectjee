package ma.amine.cinema.entities;

import java.util.Collection;
import java.util.Date;
import org.springframework.data.rest.core.config.Projection;

@Projection(name="p1",types= {ma.amine.cinema.entities.Projection.class})
public interface ProjectionProj {
	public Long getId();
	public double getPrix();
	public Date getDateProjection();
	public Salle getsalle();
	public Film getFilm();
	public Seance getSeance();
	public Collection<Ticket> getTickets();
}
