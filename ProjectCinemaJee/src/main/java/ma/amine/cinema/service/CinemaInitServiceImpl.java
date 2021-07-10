package ma.amine.cinema.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ma.amine.cinema.dao.CategoryRepository;
import ma.amine.cinema.dao.CinemaRepository;
import ma.amine.cinema.dao.FilmRepository;
import ma.amine.cinema.dao.PlaceRepository;
import ma.amine.cinema.dao.ProjectionRepository;
import ma.amine.cinema.dao.SalleRepository;
import ma.amine.cinema.dao.SeanceRepository;
import ma.amine.cinema.dao.TicketRepository;
import ma.amine.cinema.dao.VilleRepository;
import ma.amine.cinema.entities.Categorie;
import ma.amine.cinema.entities.Cinema;
import ma.amine.cinema.entities.Film;
import ma.amine.cinema.entities.Place;
import ma.amine.cinema.entities.Projection;
import ma.amine.cinema.entities.Salle;
import ma.amine.cinema.entities.Seance;
import ma.amine.cinema.entities.Ticket;
import ma.amine.cinema.entities.Ville;

@Service
@Transactional
public class CinemaInitServiceImpl implements ICinemaInitService {
	@Autowired
	private VilleRepository villeRepository;
	@Autowired
	private CinemaRepository cinemaRepository;
	@Autowired
	private SalleRepository salleRepository;
	@Autowired
	private PlaceRepository placeRepository;
	@Autowired
	private SeanceRepository seanceRepository;
	@Autowired
	private FilmRepository filmRepository;
	@Autowired
	private ProjectionRepository projectionRepository;
	@Autowired
	private CategoryRepository categoryRepository;
	@Autowired
	private TicketRepository ticketRepository;

	@Override
	public void initVilles() {
		Stream.of("Casablanca", "Marrakech", "Rabat", "Tanger").forEach(v -> {
			Ville ville = new Ville();
			ville.setName(v);
			villeRepository.save(ville);
		});

	}

	@Override
	public void initCinemas() {
		villeRepository.findAll().forEach(vi -> {
			Stream.of("MegaRama", "Imax", "Founoun", "Chahrazad").forEach(nameCinema -> {
				Cinema cinema = new Cinema();
				cinema.setName(nameCinema);
				cinema.setNombreSalles(3 + (int) (Math.random() * 7));
				cinema.setVille(vi);
				cinemaRepository.save(cinema);
			});
		});

	}

	@Override
	public void initSalles() {
		cinemaRepository.findAll().forEach(cinema -> {
			for (int i = 0; i < cinema.getNombreSalles(); i++) {
				Salle salle = new Salle();
				salle.setName("Salle " + (i + 1));
				salle.setCinema(cinema);
				salle.setNombrePlace(15 + (int) (Math.random() * 20));
				salleRepository.save(salle);
			}
		});
	}

	@Override
	public void initPlaces() {
		salleRepository.findAll().forEach(salle -> {
			for (int i = 0; i < salle.getNombrePlace(); i++) {
				Place place = new Place();
				place.setNumero(i + 1);
				place.setSalle(salle);
				placeRepository.save(place);
			}
		});
	}

	@Override
	public void initSeances() {
		DateFormat dateFormat = new SimpleDateFormat("HH:mm");
		Stream.of("12:00", "15:00", "17:00", "19:00", "21:00").forEach(s -> {
			Seance seance = new Seance();
			try {
				seance.setHeurDebut(dateFormat.parse(s));
				seanceRepository.save(seance);
			} catch (ParseException e) {
				// TODO Auto-generata catch block e.printStackTrace();
			}
		});

	}

	@Override
	public void initCategories() {
		Stream.of("Nistoire", "Actions", "Fiction", "Drama").forEach(cat -> {
			Categorie categorie = new Categorie();
			categorie.setName(cat);
			categoryRepository.save(categorie);
		});

	}

	@Override
	public void initFilms() {
		double[] durees = new double[] { 1, 1.5, 2, 2.5, 3 };
		List<Categorie> categories = categoryRepository.findAll();
		Stream.of("Titanic", "Avengers", "Envole-moi", "Sous les étoiles de Paris").forEach(titreFilm -> {
			Film film = new Film();
			film.setTitre(titreFilm);
			film.setDuree(durees[new Random().nextInt(durees.length)]);
			film.setPhoto(titreFilm.replaceAll(" ", "") + ".jpg");
			film.setCategorie(categories.get(new Random().nextInt(categories.size())));
			filmRepository.save(film);
		});

	}

	@Override
	public void initProjections() {
		double[] prices = new double[] { 30, 59, 60, 70, 90, 100 };
		List<Film> films=filmRepository.findAll();
		villeRepository.findAll().forEach(ville -> {
			ville.getCinemas().forEach(cinema -> {
				cinema.getSalles().forEach(salle -> {
					int index=new Random().nextInt(films.size());
					Film film = films.get(index);
						seanceRepository.findAll().forEach(seance -> {
							Projection projection = new Projection();
							projection.setDateProjection(new Date());
							projection.setFilm(film);
							projection.setPrix(prices[new Random().nextInt(prices.length)]);
							projection.setSalle(salle);
							projection.setSeance(seance);
							projectionRepository.save(projection);
						});
				});
			});
		});
	}

	@Override
	public void initTickets() {
		projectionRepository.findAll().forEach(p -> {
			p.getSalle().getPlaces().forEach(place -> {
				Ticket ticket = new Ticket();
				ticket.setPlace(place);
				ticket.setPrix(p.getPrix());
				ticket.setProjection(p);
				ticket.setReserve(false);
				ticketRepository.save(ticket);
			});
		});

	}

}
