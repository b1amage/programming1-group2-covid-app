public class Row {
    private String continent;
    private String date;
    private int peopleVaccinated;
    private int newCases;
    private int newDeaths;
    private String location;
    private String isoCode;
    private long population;

    // 3 Constructors
    public Row() {}
    public Row(String continent, String date, int peopleVaccinated, int newCases, int newDeaths, String location, String isoCode, long population) {
        setContinent(continent);
        setDate(date);
        setPeopleVaccinated(peopleVaccinated);
        setNewCases(newCases);
        setNewDeaths(newDeaths);
        setLocation(location);
        setIsoCode(isoCode);
        setPopulation(population);
    }

    public Row(String continent, String date, String peopleVaccinated, String newCases, String newDeaths, String location, String isoCode, String population) {
        setContinent(continent);
        setDate(date);
        setPeopleVaccinated(peopleVaccinated);
        setNewCases(newCases);
        setNewDeaths(newDeaths);
        setLocation(location);
        setIsoCode(isoCode);
        setPopulation(population);
    }

    // Getters and Setters
    public String getContinent() {
        return continent;
    }

    public void setContinent(String continent) {
        if (continent.isEmpty()) {
            this.continent = "Unknown";
        } else {
            this.continent = continent;
        }
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        if (date.isEmpty()) {
            this.date = "Unknown";
        } else {
            this.date = date;
        }
    }

    public int getPeopleVaccinated() {
        return peopleVaccinated;
    }

    public void setPeopleVaccinated(String peopleVaccinated) {
        if (peopleVaccinated.isEmpty() || Integer.parseInt(peopleVaccinated.trim()) < 0) {
            this.peopleVaccinated = 0;
        } else {
            this.peopleVaccinated = Integer.parseInt(peopleVaccinated.trim());
        }
    }

    public void setPeopleVaccinated(int peopleVaccinated) {
        this.peopleVaccinated = peopleVaccinated;
    }

    public int getNewCases() {
        return newCases;
    }

    public void setNewCases(String newCases) {
        if (newCases.isEmpty() || Integer.parseInt(newCases.trim()) < 0) {
            this.newCases = 0;
        } else {
            this.newCases = Integer.parseInt(newCases.trim());
        }
    }

    public void setNewCases(int newCases) {
        this.newCases = newCases;
    }

    public int getNewDeaths() {
        return newDeaths;
    }

    public void setNewDeaths(int newDeaths) {
        this.newDeaths = newDeaths;
    }

    public void setNewDeaths(String newDeaths) {
        if (newDeaths.isEmpty() || Integer.parseInt(newDeaths.trim()) < 0) {
            this.newDeaths = 0;
        } else {
            this.newDeaths = Integer.parseInt(newDeaths.trim());
        }
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        if (location.isEmpty()) {
            this.location = "Unknown";
        } else {
            this.location = location;
        }
    }

    public String getIsoCode() {
        return isoCode;
    }

    public void setIsoCode(String isoCode) {
        if (isoCode.isEmpty()) {
            this.isoCode = "Unknown";
        } else {
            this.isoCode = isoCode;
        }
    }

    public long getPopulation() {
        return population;
    }

    public void setPopulation(long population) {
        this.population = population;
    }

    public void setPopulation(String population) {
        if (population.isEmpty() || Long.parseLong(population.trim()) < 0) {
            this.population = 0;
        } else {
            this.population = Long.parseLong(population.trim());
        }
    }

    // toString method
    public void display() {
        System.out.println("continent: " + continent);
        System.out.println("data: " + date);
        System.out.println("people_vaccinated: " + peopleVaccinated);
        System.out.println("new_cases: " + newCases);
        System.out.println("new_deaths: " + newDeaths);
        System.out.println("location: " + location);
        System.out.println("iso_code: " + isoCode);
        System.out.println("population: " + population);
        System.out.println("========================================");
    }
}
