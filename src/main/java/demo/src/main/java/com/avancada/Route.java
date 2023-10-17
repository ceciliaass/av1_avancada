package demo.src.main.java.com.avancada;

//import de.tudresden.sumo.objects.SumoStringList;

public class Route {
    private String Id;
    private String edges;

    public Route(String Id, String edges){
        this.Id = Id;
        this.edges = edges;
    }

    public String getIdItinerary() {
		return this.Id;
	}

    public String getEdges() {
		return this.edges;
	}
}
